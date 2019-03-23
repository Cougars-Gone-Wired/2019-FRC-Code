package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Drive.DriveModes;

//import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Lift {
    public enum LiftStates {
        LOCK, READY_TO_BACK_UP_FROM_STAIR, BACKING_UP_FROM_STAIR, STOP, MOVING_IN, MOVING_OUT
    }
    public enum Lift2States {
        NOT_MOVING, GOING_DOWN, GOING_UP
    }
    
    LiftStates liftState;
    Lift2States currentLift2State = Lift2States.NOT_MOVING;
    private WPI_TalonSRX frontLiftMotor;
    private WPI_TalonSRX backLiftMotor;
    private SensorCollection limits;
    private DigitalInput limit;

    private boolean readyToBackUpFromStairs;
    private boolean doneBackingUp;

    //private Ultrasonic ultraLeft;
    //private Ultrasonic ultraRight;

    //private boolean aligned;
    //boolean alignedDistance;
    //boolean alignedAngle;
    //private double distanceAtLift;
    
    public Lift(){
        frontLiftMotor = new WPI_TalonSRX(Constants.FRONT_LIFT_MOTOR_ID);
        backLiftMotor = new WPI_TalonSRX(Constants.BACK_LIFT_MOTOR_ID);
        frontLiftMotor.setNeutralMode(NeutralMode.Brake);
        backLiftMotor.setNeutralMode(NeutralMode.Brake);
        frontLiftMotor.configOpenloopRamp(Constants.RAMP_TIME);
        backLiftMotor.configOpenloopRamp(Constants.RAMP_TIME);

        limits = new SensorCollection(backLiftMotor);
        limit = new DigitalInput(Constants.LIFT_SWITCH_PORT);

        frontLiftMotor.setInverted(false);
        //frontLiftMotor.follow(backLiftMotor);
        //frontLiftMotor.setInverted(InvertType.OpposeMaster);
        readyToBackUpFromStairs = false;

        //ultraLeft = new Ultrasonic(Constants.ULTRASONIC_HATCH_LEFT_PORT);
        //ultraRight = new Ultrasonic(Constants.ULTRASONIC_HATCH_RIGHT_PORT);

        //aligned = false;
        //alignedDistance = false;
        //alignedAngle = false;
        //distanceAtLift = Constants.DISTANCE_AT_LIFT;

        initialize();
    }

    public void initialize() {
        frontLiftMotor.set(0);
        backLiftMotor.set(0);
        doneBackingUp = false;
        liftState = LiftStates.STOP;
        //liftState = LiftStates.LOCK;
        //liftState = LiftStates.READY_TO_BACK_UP_FROM_STAIR;
    }

    public void lift(boolean liftDeployButton, boolean liftRetractButton, boolean liftStopButton, Toggle liftWithdrawFromStairButton, Drive drive, Joystick mobilityStick){
        switch(liftState){
            case LOCK:
                //State: LOCK -> MOVING_IN || STOP (@ 20sec. left in match)
                if(Timer.getMatchTime() <= 20){
                    liftState = LiftStates.READY_TO_BACK_UP_FROM_STAIR;
                    
                }
                break;

            case READY_TO_BACK_UP_FROM_STAIR:
                if(liftWithdrawFromStairButton.toggle()){
                    drive.backUpFromStairs();
                    readyToBackUpFromStairs = true;
                    liftWithdrawFromStairButton.setOutput(false);
                    liftState = LiftStates.BACKING_UP_FROM_STAIR;
                }
                break;

            case BACKING_UP_FROM_STAIR:
                if(drive.driveMode != DriveModes.BACKING_UP){
                    //Rumble controller when ready to deploy lift. (Fully backed up.)
                    mobilityStick.setRumble(RumbleType.kLeftRumble, 0.8);
                    mobilityStick.setRumble(RumbleType.kRightRumble, 0.8);
                    doneBackingUp = true;
                    liftState = LiftStates.STOP;
                }

                break;

            case MOVING_IN:
                //State: MOVING_IN -> STOP
                if(!liftDeployButton || liftStopButton || limits.isFwdLimitSwitchClosed()){
                    readyToBackUpFromStairs = false;
                    backLiftMotor.set(0);
                    frontLiftMotor.set(0);
                    liftState = LiftStates.STOP;
                }
                break;

            case STOP:
                if(liftDeployButton && !limits.isFwdLimitSwitchClosed() && !liftStopButton){
                //State: STOP -> MOVING_IN
                    frontLiftMotor.set(Constants.LIFT_SPEED);
                    backLiftMotor.set( -Constants.LIFT_SPEED );
                    liftState = LiftStates.MOVING_IN;
                } else if(liftRetractButton && !limits.isRevLimitSwitchClosed() && !liftStopButton){
                //State: STOP -> MOVING_OUT
                    frontLiftMotor.set(-Constants.LIFT_SPEED);
                    backLiftMotor.set(Constants.LIFT_SPEED);
                    liftState = LiftStates.MOVING_OUT;
                }

                break;

            case MOVING_OUT:
                //State: MOVING_OUT -> STOP
                if(!liftRetractButton || liftStopButton || limits.isRevLimitSwitchClosed()){
                    readyToBackUpFromStairs = false;
                    frontLiftMotor.set(0);
                    backLiftMotor.set(0);
                    liftState = LiftStates.STOP;
                }
                break;

            default:
                //State: Emergency -> MOVING_IN
                frontLiftMotor.set(Constants.LIFT_SPEED);
                liftState = LiftStates.MOVING_IN;
                break;
        }
    }

    public void lift2(boolean downButton, boolean upButton) {
        switch(currentLift2State) {
            case NOT_MOVING:
                if (downButton && !upButton) {
                    frontLiftMotor.set(-Constants.LIFT_SPEED);
                    backLiftMotor.set(Constants.LIFT_SPEED);
                    currentLift2State = Lift2States.GOING_DOWN;
                } else if (upButton && !downButton) {
                    frontLiftMotor.set(Constants.LIFT_SPEED);
                    backLiftMotor.set(-Constants.LIFT_SPEED);
                    currentLift2State = Lift2States.GOING_UP;
                }
                break;
            case GOING_DOWN:
                if (!downButton || upButton || limits.isRevLimitSwitchClosed()) {
                    frontLiftMotor.set(0);
                    backLiftMotor.set(0);
                    currentLift2State = Lift2States.NOT_MOVING;
                }
                break;
            case GOING_UP:
                if (!upButton || downButton || limits.isRevLimitSwitchClosed()) {
                    frontLiftMotor.set(0);
                    backLiftMotor.set(0);
                    currentLift2State = Lift2States.NOT_MOVING;
                }
                break;
        }
    }

    public void showDashboard() {
        //SmartDashboard.putBoolean("Ready to Back Up", readyToBackUpFromStairs);
        //SmartDashboard.putBoolean("Backing Up", doneBackingUp);
        //SmartDashboard.putBoolean("Lift Fwd Limit", limits.isFwdLimitSwitchClosed());
        // SmartDashboard.putBoolean("Lift Rev Limit", limits.isRevLimitSwitchClosed());
        // SmartDashboard.putNumber("FrontLiftCurrent", frontLiftMotor.getOutputCurrent());
        // SmartDashboard.putNumber("BackLiftCurrent", backLiftMotor.getOutputCurrent());
        // SmartDashboard.putNumber("FrontLitfVoltage", frontLiftMotor.getMotorOutputVoltage());
        // SmartDashboard.putNumber("BackLiftVoltage", backLiftMotor.getMotorOutputVoltage());
        SmartDashboard.putBoolean("At Limit", limit.get());
    }

    // __    ___    ___   _____
    // | \    |    /        |  
    // |  |   |     ---     |  
    // | /    |       /     |  
    // ```   ```   ```      `  

    //prior to calling this class, the drivers must run up against the stair.

    /*
    public double distanceRatio(Ultrasonic left, Ultrasonic right){
        double ratio;
        //This method converts the distance between the two ultrasonic sensors into a compact ratio. Ratio will never be less than ZERO or greater than TWO.
        // Left:  33inches
        // -----  --------  =  0.942857...              Also: ratio < 1 means a greater RIGHT distance
        // Right: 35inches      ^ Returned value              ratio > 1 means a greater LEFT distance

        ratio = (left.getImperialUltrasonicValue() / right.getImperialUltrasonicValue());
        if(ratio >= 2){         //If the raw ratio is 2 or greater (Left distance is 2 or greater times larger than Right distance), set it to max adjust right.
            ratio = 1.99999999;
        }

        return ratio;
    }
    */

    /*
    public boolean distanceAlign(){
        double ultraLeftDistance;
        double ultraRightDistance;
        double leftTankSpeed;
        double rightTankSpeed;        

      //Align Angle
        if(alignedAngle == false){
            double ratio = distanceRatio(ultraLeft, ultraRight);

            if (ratio > 1){ //Left Father
                leftTankSpeed = (2-ratio); //Figure out a way to return these
                rightTankSpeed = (-leftTankSpeed); 

                alignedAngle = false;

            } else if (ratio < 1){  //Right Farther
                rightTankSpeed = (1-ratio);
                leftTankSpeed = (-rightTankSpeed);

                alignedAngle = false;
            } else {
                leftTankSpeed = 0;
                rightTankSpeed = 0;

                alignedAngle = true;
            }
        }

      //Align Distance
        else if(alignedDistance == false){
            ultraLeftDistance = ultraLeft.getImperialUltrasonicValue();
            ultraRightDistance = ultraRight.getImperialUltrasonicValue();

            if(ultraLeftDistance > distanceAtLift || ultraRightDistance > distanceAtLift){ //Farther than target
                //Move forward (speed variable based on distance)


            } else if (ultraLeftDistance == Constants.ULTRASONIC_THRESHOLD){ //At threshold
                //Back up at moderate pace

            } else if (ultraLeftDistance < distanceAtLift || ultraRightDistance < distanceAtLift) { //Closer than target
                //Move Backard (speed variable based on distance)

            } else {  //At target
                //Stop Motos, set alignDistance true, and initialize another angle check.

                alignedDistance = true;
                alignedAngle = false;
            }
        }

      //Return
        if(alignedDistance == true && alignedAngle == true){  //Aligned in both manners?
            return true;
        } else {
            return false;
        }
        
    }
    */

    public LiftStates getLiftState() {
        return liftState;
    }

    public double getFrontLiftMotorVoltage() {
        return frontLiftMotor.getMotorOutputVoltage();
    }

    public double getFrontLiftMotorCurrent() {
        return frontLiftMotor.getOutputCurrent();
    }

    public double getBackLiftMotorVoltage() {
        return backLiftMotor.getMotorOutputVoltage();
    }

    public double getBackLiftMotorCurrent() {
        return backLiftMotor.getOutputCurrent();
    }

    public boolean isReadyToBackUpFromStairs(){
        return readyToBackUpFromStairs;
    }

    public boolean isTopLimit(){
        return limits.isRevLimitSwitchClosed();
    }
}
