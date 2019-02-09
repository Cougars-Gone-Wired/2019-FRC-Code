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


    public Lift(){
        frontLiftMotor = new WPI_TalonSRX(Constants.FRONT_LIFT_MOTOR_ID);
        backLiftMotor = new WPI_TalonSRX(Constants.BACK_LIFT_MOTOR_ID);
        limits = new SensorCollection(frontLiftMotor);

        ultraLeft = new Ultrasonic(Constants.ULTRASONIC_HATCH_LEFT_PORT);
        ultraRight = new Ultrasonic(Constants.ULTRASONIC_HATCH_RIGHT_PORT);
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

    public double distanceRatio(Ultrasonic left, Ultrasonic right){
        double ratio;
        //This method converts the distance between the two ultrasonic sensors into a compact ratio.
        // Left:  33inches
        // -----  --------  =  0.942857...              Also: ratio < 1 means a greater RIGHT distance
        // Right: 35inches      ^ Returned value              ratio > 1 means a greater LEFT distance

        ratio = (left.getImperialUltrasonicValue() / right.getImperialUltrasonicValue());
        return ratio;
    }

    public void distanceAlign(){
        double ratio = distanceRatio(ultraLeft, ultraRight);


    }

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
