package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class Lift {
    public enum States {
        LOCK, STOP, EN, EC
    }

    States state;
    private WPI_TalonSRX liftMoto;
    private WPI_TalonSRX liftMotoBack;
    private Toggle liftIsDeployed;
    private SensorCollection limits;

    public boolean lftLimitTop;
    public boolean lftLimitBottom;

    public Lift(Joystick stick, int buttonNumber){
        liftMoto = new WPI_TalonSRX(Constants.LIFT_MOTO_FRONT_ID);
        liftMotoBack = new WPI_TalonSRX(Constants.LIFT_MOTO_BACK_ID);
        liftIsDeployed = new Toggle(stick, buttonNumber);
        limits = new SensorCollection(liftMoto);

        
    }

    public void initialize() {
        liftMoto.set(0);
        state = States.LOCK;
    }

    public void lift(){
        lftLimitTop = limits.isFwdLimitSwitchClosed();
        lftLimitBottom = limits.isRevLimitSwitchClosed();

        switch(state){
            case LOCK:
                //State: LOCK -> EN || STOP (@ 20sec. left in match)
                if(Timer.getMatchTime() <= 20){
                    liftIsDeployed.setOutput(false);
                    if(!lftLimitTop) {
                        liftMoto.set(Constants.LIFT_SPEED_AND_REVERSE_FACTOR);
                        liftMotoBack.set(-Constants.LIFT_SPEED_AND_REVERSE_FACTOR);
                        state = States.EN;
                    } else {
                        state = States.STOP;
                    }
                    
                }
                break;

            case EN:
                //State: EN -> STOP
                if(liftIsDeployed.toggle() == true || lftLimitTop){
                    liftMoto.set(0);
                    state = States.STOP;
                }
                break;

            case STOP:

                if(liftIsDeployed.toggle() == true){
                    if(!lftLimitTop){
                    //State: STOP -> EN
                        liftMoto.set(Constants.LIFT_SPEED_AND_REVERSE_FACTOR);
                        liftMotoBack.set( -Constants.LIFT_SPEED_AND_REVERSE_FACTOR );
                        state = States.EN;
                    } else {
                    //State: STOP -> EC
                        liftMoto.set(-Constants.LIFT_SPEED_AND_REVERSE_FACTOR);
                        liftMotoBack.set(Constants.LIFT_SPEED_AND_REVERSE_FACTOR);
                        state = States.EC;
                    }
                }
                break;

            case EC:
                //State: EC -> STOP
                if(liftIsDeployed.toggle() == true || lftLimitBottom){
                    liftMoto.set(0);
                    state = States.STOP;
                }
                break;

            default:
                //State: Emergency -> EN
                liftMoto.set(Constants.LIFT_SPEED_AND_REVERSE_FACTOR);
                state = States.EN;
                break;
        }
    }





    // __    ___    ___   _____
    // | \    |    /        |
    // |  |   |     ---     |
    // | /    |       /     |
    // ```   ```   ```      `

    public void dist(){
        
    }
}
