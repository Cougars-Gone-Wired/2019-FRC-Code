package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Timer;

public class Lift {
    public enum LiftStates {
        LOCK, STOP, EN, EC
    }
    
    LiftStates liftStates;
    private WPI_TalonSRX frontLiftMotor;
    private WPI_TalonSRX backLiftMotor;
    private SensorCollection limits;

    private Ultrasonic ultraLeft;
    private Ultrasonic ultraRight;

    //private boolean aligned;
    boolean alignedDistance;
    boolean alignedAngle;
    private double distanceAtLift;
    
    public Lift(){
        frontLiftMotor = new WPI_TalonSRX(Constants.FRONT_LIFT_MOTOR_ID);
        backLiftMotor = new WPI_TalonSRX(Constants.BACK_LIFT_MOTOR_ID);
        limits = new SensorCollection(frontLiftMotor);

        ultraLeft = new Ultrasonic(Constants.ULTRASONIC_HATCH_LEFT_PORT);
        ultraRight = new Ultrasonic(Constants.ULTRASONIC_HATCH_RIGHT_PORT);

        //aligned = false;
        alignedDistance = false;
        alignedAngle = false;
        distanceAtLift = Constants.DISTANCE_AT_LIFT;
    }

    public void initialize() {
        frontLiftMotor.set(0);
        liftStates = LiftStates.LOCK;
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

    public void lift(Toggle liftIsDeployed){
        switch(liftStates){
            case LOCK:
                //State: LOCK -> EN || STOP (@ 20sec. left in match)
                if(Timer.getMatchTime() <= 20){
                    liftIsDeployed.setOutput(false);
                    if(!limits.isFwdLimitSwitchClosed()) {
                        frontLiftMotor.set(Constants.LIFT_SPEED);
                        backLiftMotor.set(-Constants.LIFT_SPEED);
                        liftStates = LiftStates.EN;
                    } else {
                        frontLiftMotor.set(0);
                        backLiftMotor.set(0);
                        liftStates = LiftStates.STOP;
                    }
                    
                }
                break;

            case EN:
                //State: EN -> STOP
                if(liftIsDeployed.toggle() == true || limits.isFwdLimitSwitchClosed()){
                    backLiftMotor.set(0);
                    frontLiftMotor.set(0);
                    liftStates = LiftStates.STOP;
                }
                break;

            case STOP:

                if(liftIsDeployed.toggle() == true){
                    if(!limits.isFwdLimitSwitchClosed()){
                    //State: STOP -> EN
                        frontLiftMotor.set(Constants.LIFT_SPEED);
                        backLiftMotor.set( -Constants.LIFT_SPEED );
                        liftStates = LiftStates.EN;
                    } else {
                    //State: STOP -> EC
                        frontLiftMotor.set(-Constants.LIFT_SPEED);
                        backLiftMotor.set(Constants.LIFT_SPEED);
                        liftStates = LiftStates.EC;
                    }
                }
                break;

            case EC:
                //State: EC -> STOP
                if(liftIsDeployed.toggle() == true || limits.isRevLimitSwitchClosed()){
                    frontLiftMotor.set(0);
                    backLiftMotor.set(0);
                    liftStates = LiftStates.STOP;
                }
                break;

            default:
                //State: Emergency -> EN
                frontLiftMotor.set(Constants.LIFT_SPEED);
                liftStates = LiftStates.EN;
                break;
        }
    }
}
