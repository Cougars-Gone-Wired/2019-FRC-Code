package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class CargoManip {

    //for using a joystick to control the arm
    public enum MovementStates {
        MOVING_TOWARDS_TOP, MOVING_TOWARDS_FLOOR, NOT_MOVING
    }
    
    public enum LocationStates {
        AT_FLOOR, BETWEEN_SHIP_AND_FLOOR, AT_SHIP, BETWEEN_ROCKET_AND_SHIP, AT_ROCKET, BETWEEN_TOP_AND_ROCKET, AT_TOP
    }

    public enum DestinationStates {
        TO_FLOOR, TO_BETWEEN_SHIP_AND_FLOOR, TO_SHIP, TO_BETWEEN_ROCKET_AND_SHIP, AT_ROCKET, TO_ROCKET, TO_BETWEEN_TOP_AND_ROCKET, TO_TOP
    }

    public enum IntakeStates {
        INTAKING, OUTTAKING, NOT_MOVING
    }

    /*public enum LimitSwitchStates {
        TOP_LIMIT, BOTTOM_LIMIT, CARGO_SHIP, ROCKET, NO_SWITCH
    }*/

    private MovementStates movementState;
    private LocationStates locationState;
    private DestinationStates destinationState;
    private IntakeStates intakeState;
    //private LimitSwitchStates limitSwitchState;
    
    private WPI_TalonSRX armMotor;
    private WPI_TalonSRX intakeMotor;

    private SensorCollection armLimitSwitches;

    private DigitalInput limitSwitchCargoShip;
    private DigitalInput limitSwitchRocket;

    private double manualSpeed; // To be used and is explained in the armMove() method
    private double buttonSpeed;

    // A bunch of variables that make very complicated boolean logic easier to call upon in the armMove() method
    private boolean topSwitch; 
    private boolean rocketSwitch;
    private boolean cargoShipSwitch;
    private boolean floorSwitch;

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
        movementState = MovementStates.NOT_MOVING;
        locationState = LocationStates.AT_TOP;
        destinationState = DestinationStates.TO_TOP;
        intakeState = IntakeStates.NOT_MOVING;
        //limitSwitchState = LimitSwitchStates.NO_SWITCH;
        armMotor.set(0);
        intakeMotor.set(0);
    }

    //move the arm using a joystick

    public void armMove(double armAxis, boolean topButton, boolean rocketButton, boolean cargoShipButton, boolean floorButton) {
        manualSpeed = armAxis * Constants.CARGO_ARM_MOVE_SPEED; // Sets manualSpeed to be used later.  It is seperate from armAxis because we need correctly use the true value in order to mvoe 
        buttonSpeed = Constants.CARGO_ARM_MOVE_SPEED;

        trackLocation();

        if (Math.abs(armAxis) > Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD) {

        } else {
            switch (destinationState) {
            }
        }
    }

    public void trackLocation() {
        topSwitch = armLimitSwitches.isFwdLimitSwitchClosed();
        rocketSwitch = !limitSwitchCargoShip.get();
        cargoShipSwitch = !limitSwitchRocket.get();
        floorSwitch = armLimitSwitches.isRevLimitSwitchClosed();

        switch (locationState) {
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

    public void armMoveManual(double armAxis) {
        manualSpeed = armAxis * Constants.CARGO_ARM_MOVE_SPEED;

        trackLocation();

        switch (movementState) {
            case MOVING_TOWARDS_TOP:
                if (armAxis < Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD || topSwitch) {
                    armMotor.set(0);
                    movementState = MovementStates.NOT_MOVING;
                } else {
                    armMotor.set(manualSpeed);
                }
                break;
            case MOVING_TOWARDS_FLOOR:
                if (armAxis > -Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD || armLimitSwitches.isRevLimitSwitchClosed()) {
                    armMotor.set(0);
                    movementState = MovementStates.NOT_MOVING;
                } else {
                    armMotor.set(manualSpeed);
                }
                break;
            case NOT_MOVING:
                if (armAxis > Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD) {
                    armMotor.set(manualSpeed);
                    movementState = MovementStates.MOVING_TOWARDS_TOP;
                } else if (armAxis < -Constants.CARGO_ARM_MOVE_AXIS_THRESHHOLD) {
                    armMotor.set(manualSpeed);
                    movementState = MovementStates.MOVING_TOWARDS_FLOOR;
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
        topSwitch = armLimitSwitches.isFwdLimitSwitchClosed();
        rocketSwitch = !limitSwitchCargoShip.get();
        cargoShipSwitch = !limitSwitchRocket.get();
        floorSwitch = armLimitSwitches.isRevLimitSwitchClosed();
        SmartDashboard.putBoolean("Cargo Arm Top Position", topSwitch);
        SmartDashboard.putBoolean("Cargo Arm Floor Position", floorSwitch);
        SmartDashboard.putBoolean("Cargo Arm Cargo Ship Position", cargoShipSwitch);
        SmartDashboard.putBoolean("Cargo Arm Rocket Position", rocketSwitch);
        // SmartDashboard.putBoolean("Cargo Ship Position", armState.equals(ArmStates.CARGO_SHIP));
        // SmartDashboard.putBoolean("Rocket Position", armState.equals(ArmStates.ROCKET));
        // SmartDashboard.putNumber("Arm Encoder Value", encoderValue);
        /*if (armState.equals(ArmStates.TO_TOP) || armState.equals(ArmStates.TO_BOTTOM) || armState.equals(ArmStates.TO_CARGO_SHIP) || armState.equals(ArmStates.TO_ROCKET)) {
            SmartDashboard.putBoolean("Moving", true);
        }
        else {
            SmartDashboard.putBoolean("Moving", false);
        }*/
        //This is a useless switch statement. I kept it just in case
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