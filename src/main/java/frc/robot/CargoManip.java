package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class CargoManip {

    //for using a joystick to control the arm
    public enum ArmStatesManual {
        MOVING_UP, MOVING_DOWN, NOT_MOVING
    }

    //for using buttons to control the arm
    // public enum ArmStates {
    //     TOP, ROCKET, CARGO_SHIP, BOTTOM, TO_TOP, TO_ROCKET, TO_CARGO_SHIP, TO_BOTTOM
    // }
    
    public enum ArmStates {
        FLOOR, CARGO_SHIP, ROCKET, TOP, FLOOR_TO_CARGO_SHIP, CARGO_SHIP_TO_ROCKET, ROCKET_TO_TOP, CARGO_SHIP_TO_FLOOR, ROCKET_TO_CARGO_SHIP, TOP_TO_ROCKET
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

    private SensorCollection armLimitSwitches;

    private DigitalInput limitSwitchCargoShip;
    private DigitalInput limitSwitchRocket;

    private double speed; // To be used and is explained in the armMove() method

    // A bunch of variables that make very complicated boolean logic easier to call upon in the armMove() method
    private boolean topSwitch; 
    private boolean rocketSwitch;
    private boolean cargoShipSwitch;
    private boolean floorSwitch;
    private boolean isAxisUp;
    private boolean isAxisDown;

    //private int encoderValue;

    public CargoManip() {
        armMotor = new WPI_TalonSRX(Constants.CARGO_ARM_MOTOR_ID);
        intakeMotor = new WPI_TalonSRX(Constants.CARGO_INTAKE_MOTOR_ID);
        armMotor.setNeutralMode(NeutralMode.Brake);
        armMotor.configOpenloopRamp(Constants.RAMP_TIME);
        intakeMotor.setNeutralMode(NeutralMode.Brake);
        intakeMotor.configOpenloopRamp(Constants.RAMP_TIME);

        armLimitSwitches = new SensorCollection(armMotor);
        limitSwitchCargoShip = new DigitalInput(Constants.CARGO_SHIP_LIMIT_SWITCH_ID);
        limitSwitchRocket = new DigitalInput(Constants.ROCKET_LIMIT_SWITCH_ID);
        //cargoShipLimitSwitch = true;
        //rocketLimitSwitch = true;
        armLimitSwitches.setQuadraturePosition(0, 0);
        //encoderValue = armLimitSwitches.getQuadraturePosition();
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
        speed = armAxis * Constants.CARGO_ARM_MOVE_SPEED;
        switch (armStateM) {
            case MOVING_UP:
                if (armAxis < Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD || armLimitSwitches.isFwdLimitSwitchClosed()) {
                    armMotor.set(0);
                    armStateM = ArmStatesManual.NOT_MOVING;
                } else {
                    armMotor.set(speed);
                }
                break;
            case MOVING_DOWN:
                if (armAxis > -Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD || armLimitSwitches.isRevLimitSwitchClosed()) {
                    armMotor.set(0);
                    armStateM = ArmStatesManual.NOT_MOVING;
                } else {
                    armMotor.set(speed);
                }
                break;
            case NOT_MOVING:
                if (armAxis > Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD) {
                    armMotor.set(speed);
                    armStateM = ArmStatesManual.MOVING_UP;
                } else if (armAxis < -Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD) {
                    armMotor.set(speed);
                    armStateM = ArmStatesManual.MOVING_DOWN;
                }
                break;
        }
    }

    public void armMove(double armAxis) {
        speed = armAxis * Constants.CARGO_ARM_MOVE_SPEED; // Sets speed to be used later.  It is seperate from armAxis because we need correctly use the true value in order to mvoe 

        topSwitch = armLimitSwitches.isFwdLimitSwitchClosed();
        rocketSwitch = !limitSwitchCargoShip.get();
        cargoShipSwitch = !limitSwitchRocket.get();
        floorSwitch = armLimitSwitches.isRevLimitSwitchClosed();

        isAxisUp = armAxis > Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD;
        isAxisDown = armAxis < -Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD;

        switch (armState) {
            case FLOOR:
                if (isAxisUp) {
                    armMotor.set(speed);
                    armState = ArmStates.FLOOR_TO_CARGO_SHIP;
                }
                break;
            case CARGO_SHIP:
                if (isAxisUp && !isAxisDown) {
                    armMotor.set(speed);
                    armState = ArmStates.CARGO_SHIP_TO_ROCKET;
                } else if (isAxisDown && !isAxisUp) {
                    armMotor.set(speed);
                    armState = ArmStates.CARGO_SHIP_TO_FLOOR;
                }
                break;
            case ROCKET:
                if (isAxisUp && !isAxisDown) {
                    armMotor.set(speed);
                    armState = ArmStates.ROCKET_TO_TOP;
                } else if (isAxisDown && !isAxisUp) {
                    armMotor.set(speed);
                    armState = ArmStates.ROCKET_TO_CARGO_SHIP;
                }
                break;
            case TOP:
                if (isAxisDown) {
                    armMotor.set(speed);
                    armState = ArmStates.TOP_TO_ROCKET;
                }
                break;
            case FLOOR_TO_CARGO_SHIP:
                if (topSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.TOP;
                } else if (isAxisDown && !isAxisUp) {
                    armMotor.set(speed);
                    armState = ArmStates.CARGO_SHIP_TO_FLOOR;
                } else if (cargoShipSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
                break;
            case CARGO_SHIP_TO_ROCKET:
                if (topSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.TOP;
                } else if (isAxisDown && !isAxisUp) {
                    armMotor.set(speed);
                    armState = ArmStates.ROCKET_TO_CARGO_SHIP;
                } else if (rocketSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.ROCKET;
                }
                break;
            case ROCKET_TO_TOP:
                if (topSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.TOP;
                } else if (isAxisDown && !isAxisUp) {
                    armMotor.set(speed);
                    armState = ArmStates.TOP_TO_ROCKET;
                }
                break;
            case CARGO_SHIP_TO_FLOOR:
                if (floorSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.FLOOR;
                } else if (isAxisUp && !isAxisDown) {
                    armMotor.set(speed);
                    armState = ArmStates.FLOOR_TO_CARGO_SHIP;
                }
                break;
            case ROCKET_TO_CARGO_SHIP:
                if (floorSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.FLOOR;
                } else if (isAxisUp && !isAxisDown) {
                    armMotor.set(speed);
                    armState = ArmStates.CARGO_SHIP_TO_ROCKET;
                } else if (cargoShipSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
                break;
            case TOP_TO_ROCKET:
                if (floorSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.FLOOR;
                } else if (isAxisUp && !isAxisDown) {
                    armMotor.set(speed);
                    armState = ArmStates.ROCKET_TO_TOP;
                } else if (rocketSwitch) {
                    armMotor.set(0);
                    armState = ArmStates.ROCKET;
                }
                break;
        }
    }

    //For moving the arm using buttons and an encoder
    /*public void armMove(boolean topButton, boolean bottomButton, boolean cargoShipButton, boolean rocketButton) {
        topLimitSwitch = armLimitSwitches.isFwdLimitSwitchClosed();
        bottomLimitSwitch = armLimitSwitches.isRevLimitSwitchClosed();
        encoderValue = armLimitSwitches.getQuadraturePosition();
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
                armLimitSwitches.setQuadraturePosition(0, 0);
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
    }*/ 

    public void sensorLight() {
        // encoderValue = armLimitSwitches.getQuadraturePosition();
        //cargoShipLimitSwitch = !limitSwitchCargoShip.get();
        //rocketLimitSwitch = !limitSwitchRocket.get();
        // SmartDashboard.putBoolean("Top Position", armState.equals(ArmStates.TOP));
        // SmartDashboard.putBoolean("Bottom Position", armState.equals(ArmStates.BOTTOM));
        SmartDashboard.putBoolean("Cargo Ship Position", limitSwitchCargoShip.get());
        SmartDashboard.putBoolean("Rocket Position", limitSwitchRocket.get());
        // SmartDashboard.putBoolean("Cargo Ship Position", armState.equals(ArmStates.CARGO_SHIP));
        // SmartDashboard.putBoolean("Rocket Position", armState.equals(ArmStates.ROCKET));
        // SmartDashboard.putNumber("Arm Encoder Value", encoderValue);
        // if (armState.equals(ArmStates.TO_TOP) || armState.equals(ArmStates.TO_BOTTOM) || armState.equals(ArmStates.TO_CARGO_SHIP) || armState.equals(ArmStates.TO_ROCKET)) {
        //     SmartDashboard.putBoolean("Moving", true);
        // }
        // else {
        //     SmartDashboard.putBoolean("Moving", false);
        // }
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

    public double getIntakeMotorVoltage() {
        return intakeMotor.getMotorOutputVoltage();
    }

    public double getIntakeMotorCurrent() {
        return intakeMotor.getOutputCurrent();
    }

    public double getArmMotorVoltage() {
        return armMotor.getMotorOutputVoltage();
    }

    public double getArmMotorCurrent() {
        return armMotor.getOutputCurrent();
    }
}