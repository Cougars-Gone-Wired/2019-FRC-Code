package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CargoManip {

    //for using a joystick to control the arm
    public enum ArmStatesManual {
        MOVING_UP, MOVING_DOWN, NOT_MOVING
    }

    //for using buttons to control the arm
    public enum ArmStates {
        TOP, ROCKET, CARGO_SHIP, BOTTOM, TO_TOP, TO_ROCKET, TO_CARGO_SHIP, TO_BOTTOM
    }

    public enum IntakeStates {
        INTAKING, OUTTAKING, NOT_MOVING
    }

    /*public enum LimitSwitchStates {
        TOP_LIMIT, BOTTOM_LIMIT, CARGO_SHIP, ROCKET, NO_SWITCH
    }*/

    private ArmStates armState;
    private ArmStatesManual armStateM;
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
        intakeMotor = new WPI_TalonSRX(Constants.CARGO_INTAKE_MOTOR_ID);
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
        armStateM = ArmStatesManual.NOT_MOVING;
        intakeState = IntakeStates.NOT_MOVING;
        //limitSwitchState = LimitSwitchStates.NO_SWITCH;
        armMotor.set(0);
        intakeMotor.set(0);
    }

    //move the arm using a joystick
    public void armMoveManual(double armAxis) {
        armAxis = armAxis * 0.5;
        switch (armStateM) {
            case MOVING_UP:
            if (armAxis < 0.15 || armSensors.isFwdLimitSwitchClosed()) {
                armMotor.set(0);
                armStateM = ArmStatesManual.NOT_MOVING;
            }
            else {
                armMotor.set(armAxis);
            }
            break;
            case MOVING_DOWN:
            if (armAxis > -0.15 || armSensors.isRevLimitSwitchClosed()) {
                armMotor.set(0);
                armStateM = ArmStatesManual.NOT_MOVING;
            }
            else {
                armMotor.set(armAxis);
            }
            break;
            case NOT_MOVING:
            if (armAxis > 0.15 && !armSensors.isFwdLimitSwitchClosed()) {
                armMotor.set(armAxis);
                armStateM = ArmStatesManual.MOVING_UP;
            }
            if (armAxis < -0.15 && !armSensors.isRevLimitSwitchClosed()) {
                armMotor.set(armAxis);
                armStateM = ArmStatesManual.MOVING_DOWN;
            }
            break;
        }
    }

    //move the arm using buttons
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
            //check if reached top
            if (topLimitSwitch) {
                armMotor.set(0);
                armSensors.setQuadraturePosition(0, 0);
                armState = ArmStates.TOP;
            }
            //check if a different button has been pressed
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (cargoShipButton) {
                //check which side of the cargo ship position the arm is on
                if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
            }
            else if (rocketButton) {
                //check which side of the rocket position the arm is on
                if (encoderValue > Constants.ROCKET_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
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
            if (encoderValue == Constants.ROCKET_ENCODER_VALUE_GOAL) {
                armMotor.set(0);
                armState = ArmStates.ROCKET;
            }
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (cargoShipButton) {
                if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
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
            if (encoderValue == Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                armMotor.set(0);
                armState = ArmStates.CARGO_SHIP;
            }
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.TO_BOTTOM;
            }
            else if (rocketButton) {
                if (encoderValue > Constants.ROCKET_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else if (encoderValue < Constants.ROCKET_ENCODER_VALUE_GOAL) {
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
                if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_CARGO_SHIP;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
            }
            else if (rocketButton) {
                if (encoderValue > Constants.ROCKET_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.TO_ROCKET;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.ROCKET;
                }
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
        //useless switch statement. kept it just in case
        // From Jake: I kinda messed up the switches, don't be angry pls.
        /*switch (limitSwitchState) {
            case TOP_LIMIT:
            if (!topLimitSwitch && !bottomLimitSwitch) {
                limitSwitchState = LimitSwitchStates.NO_SWITCH;
            }
            break;
            case BOTTOM_LIMIT:
            if (!topLimitSwitch && !bottomLimitSwitch) {
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
            if (topLimitSwitch || bottomLimitSwitch) {
                limitSwitchState = LimitSwitchStates.TOP_LIMIT;
            }
            if (topLimitSwitch || bottomLimitSwitch) {
                limitSwitchState = LimitSwitchStates.BOTTOM_LIMIT;
            }
            break;
        }*/
    }
    
    public void intakeMove(double intakeAxis, double outtakeAxis) {
        switch (intakeState) {
            case INTAKING:
            if (intakeAxis < 0.15 || outtakeAxis > 0.15) {
                intakeMotor.set(0);
                intakeState = IntakeStates.NOT_MOVING;
            }
            break;
            case OUTTAKING:
            if (outtakeAxis < 0.15 || intakeAxis > 0.15) {
                intakeMotor.set(0);
                intakeState = IntakeStates.NOT_MOVING;
            }
            break;
            case NOT_MOVING:
            if (intakeAxis > 0.15 && outtakeAxis < 0.15) {
                intakeMotor.set(Constants.CARGO_ARM_INTAKE_SPEED);
                intakeState = IntakeStates.INTAKING;
            }
            if (outtakeAxis > 0.15 && intakeAxis < 0.15) {
                intakeMotor.set(-Constants.CARGO_ARM_INTAKE_SPEED);
                intakeState = IntakeStates.OUTTAKING;
            }
            break;
        }
    }
}