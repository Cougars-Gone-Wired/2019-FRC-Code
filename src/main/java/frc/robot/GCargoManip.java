package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CargoManip {


    //OOPS ONE SECOND.
    

    /*public enum ArmStates {
        MOVING_UP, MOVING_DOWN, NOT_MOVING
    }*/

    public enum ArmStates {
        TOP, ROCKET, CARGO_SHIP, BOTTOM, TO_TOP, TO_ROCKET, TO_CARGO_SHIP, TO_BOTTOM
    }

    public enum IntakeStates {
        INTAKING, OUTTAKING, NOT_MOVING
    }

    /*public enum LimitSwitchStates {
        TOP_LIMIT, BOTTOM_LIMIT, CARGO_SHIP, ROCKET, NO_SWITCH
    }*/

    //private ArmStates armState;
    private ArmStates armState;
    private IntakeStates intakeState;
    //private LimitSwitchStates limitSwitchState;
    
    private WPI_TalonSRX armMotor;
    private WPI_TalonSRX intakeMotor;

    private SensorCollection armSensors;

    //private DigitalInput limitSwitchCargoShip;
    //private DigitalInput limitSwitchRocket;

    private boolean topLimitSwitch;
    private boolean bottomLimitSwitch;
    //private boolean cargoShipLimitSwitch;
    //private boolean rocketLimitSwitch;

    private int encoderValue;

    public CargoManip() {
        armMotor = new WPI_TalonSRX(Constants.CARGO_ARM_MOTOR_ID);
        intakeMotor = new WPI_TalonSRX(Constants.CARGO_ARM_INTAKE_MOTOR_ID);
        armSensors = new SensorCollection(armMotor);
        //limitSwitchCargoShip = new DigitalInput(Constants.CARGO_SHIP_LIMIT_SWITCH_ID);
        //limitSwitchRocket = new DigitalInput(Constants.ROCKET_LIMIT_SWITCH_ID);
        topLimitSwitch = false;
        bottomLimitSwitch = false;
        //cargoShipLimitSwitch = true;
        //rocketLimitSwitch = true;
        armSensors.setQuadraturePosition(0, 0);
        encoderValue = armSensors.getQuadraturePosition();
        initialize();
    }
    
    public void initialize() {
        armState = ArmStates.TOP;
        intakeState = IntakeStates.NOT_MOVING;
        //limitSwitchState = LimitSwitchStates.NO_SWITCH;
        armMotor.set(0);
        intakeMotor.set(0);
    }

    /*public void armMove(double armAxis) {
        switch (armState) {
            case MOVING_UP:
            if (armAxis < 0.15 || armSensors.isFwdLimitSwitchClosed()) {
                armMotor.set(0);
                armState = ArmStates.NOT_MOVING;
            }
            else {
                armMotor.set(armAxis);
            }
            break;
            case MOVING_DOWN:
            if (armAxis > -0.15 || armSensors.isRevLimitSwitchClosed()) {
                armMotor.set(0);
                armState = ArmStates.NOT_MOVING;
            }
            else {
                armMotor.set(armAxis);
            }
            break;
            case NOT_MOVING:
            if (armAxis > 0.15 && !armSensors.isFwdLimitSwitchClosed()) {
                armMotor.set(armAxis);
                armState = ArmStates.MOVING_UP;
            }
            if (armAxis < -0.15 && !armSensors.isRevLimitSwitchClosed()) {
                armMotor.set(armAxis);
                armState = ArmStates.MOVING_DOWN;
            }
            break;
        }
    }*/

    public void armMove(boolean topButton, boolean bottomButton, boolean cargoShipButton, boolean rocketButton) {
        topLimitSwitch = armSensors.isFwdLimitSwitchClosed();
        bottomLimitSwitch = armSensors.isRevLimitSwitchClosed();
        encoderValue = armSensors.getQuadraturePosition();
        switch (armState) {
            case TOP:
            if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (rocketButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_ROCKET;
            }
            else if (cargoShipButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_CARGO_SHIP;
            }
            break;
            case ROCKET:
            if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_TOP;
            }
            else if (cargoShipButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_TOP;
            }
            break;
            case CARGO_SHIP:
            if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_TOP;
            }
            else if (rocketButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_ROCKET;
            }
            break;
            case BOTTOM:
            if (cargoShipButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_CARGO_SHIP;
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_TOP;
            }
            else if (rocketButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_ROCKET;
            }
            break;
            case TO_TOP:
            if (topLimitSwitch) {
                armMotor.set(0);
                armState = ArmStates.TOP;
            }
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (cargoShipButton) {
                if (encoderValue > Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
            }
            else if (rocketButton) {
                if (encoderValue > Constants.ROCKET_GOAL_ENCODER_VALUE) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else if (encoderValue > Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.ROCKET;
                }
            }
            break;
            case TO_ROCKET:
            if (encoderValue == Constants.ROCKET_GOAL_ENCODER_VALUE) {
                armMotor.set(0);
                armState = ArmStates.ROCKET;
            }
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (cargoShipButton) {
                if (encoderValue > Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_TOP;
            }
            break;
            case TO_CARGO_SHIP:
            if (encoderValue == Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                armMotor.set(0);
                armState = ArmStates.CARGO_SHIP;
            }
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (rocketButton) {
                if (encoderValue > Constants.ROCKET_GOAL_ENCODER_VALUE) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else if (encoderValue < Constants.ROCKET_GOAL_ENCODER_VALUE) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.ROCKET;
                }
            }
            break;
            case TO_BOTTOM:
            if (bottomLimitSwitch) {
                armMotor.set(0);
                armState = ArmStates.BOTTOM;
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_TOP;
            }

            else if (cargoShipButton) {
                if (encoderValue > Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
            }
            else if (rocketButton) {
                if (encoderValue > Constants.ROCKET_GOAL_ENCODER_VALUE) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else if (encoderValue > Constants.CARGO_SHIP_GOAL_ENCODER_VALUE) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.ROCKET;
                }
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_TOP;
            }
        }
    } 

    public void sensorLight() {
        encoderValue = armSensors.getQuadraturePosition();
        //cargoShipLimitSwitch = !limitSwitchCargoShip.get();
        //rocketLimitSwitch = !limitSwitchRocket.get();
        SmartDashboard.putBoolean("Top Position", armState.equals(ArmStates.TOP));
        SmartDashboard.putBoolean("Bottom Position", armState.equals(ArmStates.BOTTOM));
        SmartDashboard.putBoolean("Cargo Ship Position", armState.equals(ArmStates.CARGO_SHIP));
        SmartDashboard.putBoolean("Rocket Position", armState.equals(ArmStates.ROCKET));
        SmartDashboard.putNumber("Arm Encoder Value", encoderValue);
        if (armState.equals(ArmStates.TO_TOP) || armState.equals(ArmStates.TO_BOTTOM) || armState.equals(ArmStates.TO_CARGO_SHIP) || armState.equals(ArmStates.TO_ROCKET)) {
            SmartDashboard.putBoolean("Moving", true);
        }
        else {
            SmartDashboard.putBoolean("Moving", false);
        }
        /*switch (limitSwitchState) {
            case TOP_LIMIT:
            if (!topLimitSwitch) {
                limitSwitchState = LimitSwitchStates.NO_SWITCH;
            }
            break;
            case BOTTOM_LIMIT:
            if (!bottomLimitSwitch) {
                limitSwitchState = LimitSwitchStates.NO_SWITCH;
            }
            break;
            case CARGO_SHIP:
            if (cargoShipLimitSwitch) {
                limitSwitchState = LimitSwitchStates.NO_SWITCH;
            }
            break;
            case ROCKET:
            if (rocketLimitSwitch) {
                limitSwitchState = LimitSwitchStates.NO_SWITCH;
            }
            break;
            case NO_SWITCH:
            if (!cargoShipLimitSwitch) {
                limitSwitchState = LimitSwitchStates.CARGO_SHIP;
            }
            if (!rocketLimitSwitch) {
                limitSwitchState = LimitSwitchStates.ROCKET;
            }
            if (topLimitSwitch) {
                limitSwitchState = LimitSwitchStates.TOP_LIMIT;
            }
            if (bottomLimitSwitch) {
                limitSwitchState = LimitSwitchStates.BOTTOM_LIMIT;
            }
            break;
        }*/
    }
    
    public void intakeMove(boolean intakeButton, boolean outtakeButton) {
        switch (intakeState) {
            case INTAKING:
            if (!intakeButton || outtakeButton) {
                intakeMotor.set(0);
                intakeState = IntakeStates.NOT_MOVING;
            }
            break;
            case OUTTAKING:
            if (!outtakeButton || intakeButton) {
                intakeMotor.set(0);
                intakeState = IntakeStates.NOT_MOVING;
            }
            break;
            case NOT_MOVING:
            if (intakeButton && !outtakeButton) {
                intakeMotor.set(1);
                intakeState = IntakeStates.INTAKING;
            }
            if (outtakeButton && !intakeButton) {
                intakeMotor.set(-1);
                intakeState = IntakeStates.OUTTAKING;
            }
            break;
        }
    }
}