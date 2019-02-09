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


    public Lift(){
        frontLiftMotor = new WPI_TalonSRX(Constants.FRONT_LIFT_MOTOR_ID);
        backLiftMotor = new WPI_TalonSRX(Constants.BACK_LIFT_MOTOR_ID);
        limits = new SensorCollection(frontLiftMotor);
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

    public void distanceAlign(){
        
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
                        liftStates = LiftStates.STOP;
                    }
                    
                }
                break;

            case EN:
                //State: EN -> STOP
                if(liftIsDeployed.toggle() == true || limits.isFwdLimitSwitchClosed()){
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
