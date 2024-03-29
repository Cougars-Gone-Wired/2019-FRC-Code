package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class CargoManip {

    // for using a joystick to move the arm
    public enum MovementStates {
        MOVING_TOWARDS_TOP, MOVING_TOWARDS_FLOOR, NOT_MOVING
    }

    // for Jacob's code for using buttons to move the arm
    public enum LocationStates {
        AT_FLOOR, BETWEEN_SHIP_AND_FLOOR, AT_SHIP, BETWEEN_ROCKET_AND_SHIP, AT_ROCKET, BETWEEN_TOP_AND_ROCKET, AT_TOP
    }

    public enum DestinationStates {
        TO_FLOOR, TO_BETWEEN_SHIP_AND_FLOOR, TO_SHIP, TO_BETWEEN_ROCKET_AND_SHIP, TO_ROCKET, TO_BETWEEN_TOP_AND_ROCKET, TO_TOP;
        public static DestinationStates[] INDEXED_DESTINATION_STATES = new DestinationStates[] {TO_FLOOR, TO_BETWEEN_SHIP_AND_FLOOR, TO_SHIP, TO_BETWEEN_ROCKET_AND_SHIP, TO_ROCKET, TO_BETWEEN_TOP_AND_ROCKET, TO_TOP};
    }

    // for Gavyn's code for using buttons to move the arm
    /*public enum EncoderStates {
        TOP_LIMIT, BOTTOM_LIMIT, CARGO_SHIP, AT_ROCKET, NO_SWITCH
    }*/

    // for the intake
    public enum IntakeStates {
        INTAKING, OUTTAKING, NOT_MOVING
    }

    private MovementStates movementState;
    private LocationStates locationState;
    private DestinationStates destinationState;
    // private LimitSwitchStates limitSwitchState;
    private IntakeStates intakeState;
    
    private WPI_TalonSRX armMotor, intakeMotor;

    private SensorCollection armLimitSwitches;

    private DigitalInput limitSwitchCargoShip, limitSwitchRocket;

    // for the movement motor in the manualArmMove(manualArmAxis) method
    private double manualSpeed; 

    // for the trackLocation() method (and other methods for the moment)
    private boolean topSwitch, rocketSwitch, cargoShipSwitch, floorSwitch;

    // private int encoderValue;

    public CargoManip() {

        armMotor = new WPI_TalonSRX(Constants.CARGO_ARM_MOTOR_ID);
        armMotor.setNeutralMode(NeutralMode.Brake);
        armMotor.configOpenloopRamp(Constants.RAMP_TIME);
        intakeMotor = new WPI_TalonSRX(Constants.CARGO_INTAKE_MOTOR_ID);
        intakeMotor.setNeutralMode(NeutralMode.Brake);
        intakeMotor.configOpenloopRamp(Constants.RAMP_TIME);

        armLimitSwitches = new SensorCollection(armMotor);
        limitSwitchCargoShip = new DigitalInput(Constants.CARGO_SHIP_LIMIT_SWITCH_ID);
        limitSwitchRocket = new DigitalInput(Constants.ROCKET_LIMIT_SWITCH_ID);

        //cargoShipLimitSwitch = true;
        //rocketLimitSwitch = true;
        //armLimitSwitches.setQuadraturePosition(0, 0);
        //encoderValue = armLimitSwitches.getQuadraturePosition();

        initialize();

    }
    
    public void initialize() {

        movementState = MovementStates.NOT_MOVING;
        locationState = LocationStates.AT_TOP;
        destinationState = DestinationStates.TO_TOP;
        intakeState = IntakeStates.NOT_MOVING;
        //limitSwitchState = LimitSwitchStates.NO_SWITCH;

        armMotor.set(0.0);
        intakeMotor.set(0.0);

    }

    // Move the arm using buttons(built in manual override)
    public void armMove(double armAxis, boolean topButton, boolean rocketButton, boolean cargoShipButton, boolean floorButton) {

        trackLocation();

        if (armAxis > Constants.CARGO_ARM_MOVE_AXIS_DEADZONE || armAxis < -Constants.CARGO_ARM_MOVE_AXIS_DEADZONE) { // manual override

            armMoveManual(armAxis);
            destinationState = DestinationStates.INDEXED_DESTINATION_STATES[locationState.ordinal()];

        } else {

            // if one destination button is pressed, set destinationState to a value which corresponds to the name of the button
            if (floorButton && !cargoShipButton && !rocketButton && !topButton) {
                destinationState = DestinationStates.TO_FLOOR;
            } else if (cargoShipButton && !floorButton && !rocketButton && !topButton) {
                destinationState = DestinationStates.TO_SHIP;
            } else if (rocketButton && !floorButton && !cargoShipButton && !topButton) {
                destinationState = DestinationStates.TO_ROCKET;
            } else if (topButton && !floorButton && !cargoShipButton && !rocketButton) {
                destinationState = DestinationStates.TO_TOP;
            }

            // basically, move towards the destination
            switch (movementState) {

                case NOT_MOVING:
                    if (destinationState.ordinal() > locationState.ordinal()) {
                        armMotor.set(Constants.CARGO_ARM_MOVE_SPEED);
                        movementState = MovementStates.MOVING_TOWARDS_TOP;
                    } else if (destinationState.ordinal() < locationState.ordinal()) {
                        armMotor.set(-Constants.CARGO_ARM_MOVE_SPEED);
                        movementState = MovementStates.MOVING_TOWARDS_FLOOR;
                    }
                    break;

                case MOVING_TOWARDS_FLOOR:
                    if (destinationState.ordinal() == locationState.ordinal() || floorSwitch) {
                        armMotor.set(0.0);
                        movementState = MovementStates.NOT_MOVING;
                    } else if (destinationState.ordinal() > locationState.ordinal()) {
                        armMotor.set(Constants.CARGO_ARM_MOVE_SPEED);
                        movementState = MovementStates.MOVING_TOWARDS_TOP;
                    }
                    break;

                case MOVING_TOWARDS_TOP:
                    if (destinationState.ordinal() == locationState.ordinal() || topSwitch) {
                        armMotor.set(0.0);
                        movementState = MovementStates.NOT_MOVING;
                    } else if (destinationState.ordinal() < locationState.ordinal()) {
                        armMotor.set(-Constants.CARGO_ARM_MOVE_SPEED);
                        movementState = MovementStates.MOVING_TOWARDS_FLOOR;
                    }
                    break;

            }
        }
    }

    public void trackLocation() {
        topSwitch = armLimitSwitches.isFwdLimitSwitchClosed();
        rocketSwitch = !limitSwitchRocket.get();
        cargoShipSwitch = !limitSwitchCargoShip.get();
        floorSwitch = armLimitSwitches.isRevLimitSwitchClosed();

        switch (locationState) {

            /*set locationState to a certain state if the limitSwitch that corresponds to said state 
            is pressed and the cargo arm was between two limit switches during the last cycle*/
            case BETWEEN_TOP_AND_ROCKET:
            case BETWEEN_SHIP_AND_FLOOR:
            case BETWEEN_ROCKET_AND_SHIP:
                if (topSwitch) {
                    locationState = LocationStates.AT_TOP;
                } else if (rocketSwitch) {
                    locationState = LocationStates.AT_ROCKET;
                } else if (cargoShipSwitch) {
                    locationState = LocationStates.AT_SHIP;
                } else if (floorSwitch) {
                    locationState = LocationStates.AT_FLOOR;
                }
                break;

            /*Look my state diagram or attempt to comprehend the following:
            set locationState to a certain state if the limitSwitch that corresponds to said state is pressed 
            OR set locationState to an in-between value which depends on the current location state's corresponding 
            limit switch not being pressed and (in half of the following cases) on which direction the arm is moving*/
            case AT_TOP:
                if (!topSwitch) {
                    locationState = LocationStates.BETWEEN_TOP_AND_ROCKET;
                } else if (rocketSwitch) {
                    locationState = LocationStates.AT_ROCKET;
                } else if (cargoShipSwitch) {
                    locationState = LocationStates.AT_SHIP;
                } else if (floorSwitch) {
                    locationState = LocationStates.AT_FLOOR;
                }
                break;

            case AT_ROCKET:
                if (!rocketSwitch && movementState == MovementStates.MOVING_TOWARDS_FLOOR) {
                    locationState = LocationStates.BETWEEN_ROCKET_AND_SHIP;
                } if (!rocketSwitch && movementState == MovementStates.MOVING_TOWARDS_TOP) {
                    locationState = LocationStates.BETWEEN_TOP_AND_ROCKET;
                } else if (topSwitch) {
                    locationState = LocationStates.AT_TOP;
                } else if (cargoShipSwitch) {
                    locationState = LocationStates.AT_SHIP;
                } else if (floorSwitch) {
                    locationState = LocationStates.AT_FLOOR;
                }
                break;

            case AT_SHIP:
                if (!cargoShipSwitch && movementState == MovementStates.MOVING_TOWARDS_FLOOR) {
                    locationState = LocationStates.BETWEEN_SHIP_AND_FLOOR;
                } if (!cargoShipSwitch && movementState == MovementStates.MOVING_TOWARDS_TOP) {
                    locationState = LocationStates.BETWEEN_ROCKET_AND_SHIP;
                } else if (topSwitch) {
                    locationState = LocationStates.AT_TOP;
                } else if (rocketSwitch) {
                    locationState = LocationStates.AT_ROCKET;
                } else if (floorSwitch) {
                    locationState = LocationStates.AT_FLOOR;
                }
                break;

            case AT_FLOOR:
                if (!floorSwitch) {
                    locationState = LocationStates.BETWEEN_ROCKET_AND_SHIP;
                } else if (topSwitch) {
                    locationState = LocationStates.AT_TOP;
                } else if (rocketSwitch) {
                    locationState = LocationStates.AT_ROCKET;
                } else if (cargoShipSwitch) {
                    locationState = LocationStates.AT_SHIP;
                }
                break;

        }
    }

    // for manually moving with the joystick
    public void armMoveManual(double manualArmAxis) {

        manualSpeed = manualArmAxis * Constants.CARGO_ARM_MOVE_SPEED;

        switch (movementState) {
            case MOVING_TOWARDS_TOP:
                if (manualArmAxis < Constants.CARGO_ARM_MOVE_AXIS_DEADZONE || armLimitSwitches.isFwdLimitSwitchClosed()) {
                    armMotor.set(0.0);
                    movementState = MovementStates.NOT_MOVING;
                } else {
                    armMotor.set(manualSpeed);
                }
                break;
            case MOVING_TOWARDS_FLOOR:
                if (manualArmAxis > -Constants.CARGO_ARM_MOVE_AXIS_DEADZONE || armLimitSwitches.isRevLimitSwitchClosed()) {
                    armMotor.set(0.0);
                    movementState = MovementStates.NOT_MOVING;
                } else {
                    armMotor.set(manualSpeed);
                }
                break;
            case NOT_MOVING:
                if (manualArmAxis > Constants.CARGO_ARM_MOVE_AXIS_DEADZONE) {
                    armMotor.set(manualSpeed);
                    movementState = MovementStates.MOVING_TOWARDS_TOP;
                } else if (manualArmAxis < -Constants.CARGO_ARM_MOVE_AXIS_DEADZONE) {
                    armMotor.set(manualSpeed);
                    movementState = MovementStates.MOVING_TOWARDS_FLOOR;
                }
                break;
        }
    }

    // for the dashboard class
    public void showDashboard() {
        // encoderValue = armLimitSwitches.getQuadraturePosition();
        topSwitch = armLimitSwitches.isFwdLimitSwitchClosed();
        rocketSwitch = !limitSwitchRocket.get();
        cargoShipSwitch = !limitSwitchCargoShip.get();
        floorSwitch = armLimitSwitches.isRevLimitSwitchClosed();

        SmartDashboard.putBoolean("Cargo Arm Top Position", topSwitch);
        SmartDashboard.putBoolean("Cargo Arm Floor Position", floorSwitch);
        SmartDashboard.putBoolean("Cargo Arm Cargo Ship Position", cargoShipSwitch);
        SmartDashboard.putBoolean("Cargo Arm Rocket Position", rocketSwitch);
        
        // SmartDashboard.putBoolean("Cargo Ship Position", armState.equals(ArmStates.CARGO_SHIP));
        // SmartDashboard.putBoolean("Rocket Position", armState.equals(ArmStates.AT_ROCKET));
        // SmartDashboard.putNumber("Arm Encoder Value", encoderValue);
        /*if (armState.equals(ArmStates.AT_TOP) || armState.equals(ArmStates.BOTTOM) || armState.equals(ArmStates.CARGO_SHIP) || armState.equals(ArmStates.AT_ROCKET)) {
            SmartDashboard.putBoolean("Moving", true);
        }
        else {
            SmartDashboard.putBoolean("Moving", false);
        }*/
        //This is a useless switch statement. I kept it just in case.
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
            case AT_ROCKET:
            if (rocketLimitSwitch) {
                limitSwitchState = LimitSwitchStates.NO_SWITCH;
            }
            break;
            case NO_SWITCH:
            if (!cargoShipLimitSwitch) {
                limitSwitchState = LimitSwitchStates.CARGO_SHIP;
            }
            if (!rocketLimitSwitch) {
                limitSwitchState = LimitSwitchStates.AT_ROCKET;
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
    
    // for intaking and outtaking
    public void intakeMove(double intakeAxis, double outtakeAxis) {
        switch (intakeState) {
            case INTAKING:
                if (intakeAxis < Constants.CARGO_INTAKE_AXIS_DEADZONE || outtakeAxis > Constants.CARGO_OUTTAKE_AXIS_DEADZONE) {
                    intakeMotor.set(0.0);
                    intakeState = IntakeStates.NOT_MOVING;
                }
                break;
            case OUTTAKING:
                if (outtakeAxis < Constants.CARGO_OUTTAKE_AXIS_DEADZONE || intakeAxis > Constants.CARGO_INTAKE_AXIS_DEADZONE) {
                    intakeMotor.set(0.0);
                    intakeState = IntakeStates.NOT_MOVING;
                }
                break;
            case NOT_MOVING:
                if (intakeAxis > Constants.CARGO_INTAKE_AXIS_DEADZONE && outtakeAxis < Constants.CARGO_OUTTAKE_AXIS_DEADZONE) {
                    intakeMotor.set(Constants.CARGO_ARM_INTAKE_SPEED);
                    intakeState = IntakeStates.INTAKING;
                }
                if (outtakeAxis > Constants.CARGO_OUTTAKE_AXIS_DEADZONE && intakeAxis < Constants.CARGO_INTAKE_AXIS_DEADZONE) {
                    intakeMotor.set(-Constants.CARGO_ARM_OUTTAKE_SPEED);
                    intakeState = IntakeStates.OUTTAKING;
                }
                break;
        }
    }

    // for logging
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

    //For moving the arm using buttons and an encoder
    /*public void armMove(boolean topButton, boolean bottomButton, boolean cargoShipButton, boolean rocketButton) {
        topLimitSwitch = armLimitSwitches.isFwdLimitSwitchClosed();
        bottomLimitSwitch = armLimitSwitches.isRevLimitSwitchClosed();
        encoderValue = armLimitSwitches.getQuadraturePosition();
        switch (armState) {
            case AT_TOP:
            if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.BOTTOM;
            }
            else if (rocketButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_ROCKET;
            }
            else if (cargoShipButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.CARGO_SHIP;
            }
            break;
            case AT_ROCKET:
            if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.BOTTOM;
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_TOP;
            }
            else if (cargoShipButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_TOP;
            }
            break;
            case CARGO_SHIP:
            if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.BOTTOM;
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_TOP;
            }
            else if (rocketButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_ROCKET;
            }
            break;
            case BOTTOM:
            if (cargoShipButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.CARGO_SHIP;
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_TOP;
            }
            else if (rocketButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_ROCKET;
            }
            break;
            case AT_TOP:
            //check if reached top
            if (topLimitSwitch) {
                armMotor.set(0);
                armLimitSwitches.setQuadraturePosition(0, 0);
                armState = ArmStates.AT_TOP;
            }
            //check if a different button has been pressed
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.BOTTOM;
            }
            else if (cargoShipButton) {
                //check which side of the cargo ship position the arm is on
                if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.CARGO_SHIP;
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
                    armState = ArmStates.AT_ROCKET;
                }
                else if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.AT_ROCKET;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.AT_ROCKET;
                }
            }
            break;
            case AT_ROCKET:
            if (encoderValue == Constants.ROCKET_ENCODER_VALUE_GOAL) {
                armMotor.set(0);
                armState = ArmStates.AT_ROCKET;
            }
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.BOTTOM;
            }
            else if (cargoShipButton) {
                if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.CARGO_SHIP;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_TOP;
            }
            break;
            case CARGO_SHIP:
            if (encoderValue == Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                armMotor.set(0);
                armState = ArmStates.CARGO_SHIP;
            }
            else if (bottomButton) {
                armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.BOTTOM;
            }
            else if (rocketButton) {
                if (encoderValue > Constants.ROCKET_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.AT_ROCKET;
                }
                else if (encoderValue < Constants.ROCKET_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.AT_ROCKET;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.AT_ROCKET;
                }
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_TOP;
            }
            break;
            case BOTTOM:
            if (bottomLimitSwitch) {
                armMotor.set(0);
                armState = ArmStates.BOTTOM;
            }
            else if (topButton) {
                armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                armState = ArmStates.AT_TOP;
            }
            else if (cargoShipButton) {
                if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.CARGO_SHIP;
                }
                else if (encoderValue < Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.CARGO_SHIP;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.CARGO_SHIP;
                }
            }
            else if (rocketButton) {
                if (encoderValue > Constants.ROCKET_ENCODER_VALUE_GOAL) {
                    armMotor.set(Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.AT_ROCKET;
                }
                else if (encoderValue > Constants.CARGO_SHIP_ENCODER_VALUE_GOAL) {
                    armMotor.set(-Constants.CARGO_ARM_MOTOR_SPEED);
                    armState = ArmStates.AT_ROCKET;
                }
                else {
                    armMotor.set(0);
                    armState = ArmStates.AT_ROCKET;
                }
            }
        } 
    }*/ 
}