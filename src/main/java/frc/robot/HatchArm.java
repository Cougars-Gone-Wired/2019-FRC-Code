package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.SensorCollection;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class HatchArm {
    private enum HatchArmManualMoveStates {
        NOT_MOVING, MOVING_TOWARDS_FLOOR, MOVING_TOWARDS_INITIAL
    }
    private enum HatchArmMoveStates {
        INSIDE, VERT, FLOOR, FLOOR_TO_VERT, FLOOR_TO_VERT_TO_INSIDE, INSIDE_TO_VERT, INSIDE_TO_VERT_TO_FLOOR, VERT_TO_FLOOR, VERT_TO_INSIDE
    }
    private enum HatchArmGrabStates {
        IN, OUT, TO_OUT, TO_IN, TO_OUT_INTER, TO_IN_INTER
    }
    
    HatchArmManualMoveStates hatchArmManualMoveState;
    HatchArmMoveStates hatchArmMoveState;
    HatchArmGrabStates hatchArmGrabState;
    private WPI_TalonSRX hatchArmMoveMotor;
    private WPI_TalonSRX hatchArmGrabMotor;
    private SensorCollection moveLimitSwitches;
    private DigitalInput grabLimitSwitch;
    private DigitalInput moveMidSwitch;
    private boolean moveMidSwitchValue;

    public HatchArm() {
        hatchArmMoveMotor = new WPI_TalonSRX(Constants.HATCH_ARM_MOVE_MOTOR_ID);
        hatchArmGrabMotor = new WPI_TalonSRX(Constants.HATCH_ARM_GRAB_MOTOR_ID);
        hatchArmMoveMotor.setNeutralMode(NeutralMode.Brake);
        hatchArmMoveMotor.configOpenloopRamp(Constants.RAMP_TIME);
        hatchArmGrabMotor.setNeutralMode(NeutralMode.Brake);
        //hatchArmGrabMotor.configOpenloopRamp(Constants.RAMP_TIME);
        
        moveLimitSwitches = new SensorCollection(hatchArmMoveMotor);
        grabLimitSwitch = new DigitalInput(Constants.GRAB_SWITCH_PORT);
        moveMidSwitch = new DigitalInput(Constants.MID_SWITCH_PORT);
        initialize();
    }

    public void initialize() {
        hatchArmMoveMotor.set(0);
        hatchArmManualMoveState = HatchArmManualMoveStates.NOT_MOVING;
        hatchArmMoveState = HatchArmMoveStates.INSIDE;
        hatchArmGrabMotor.set(0);
        hatchArmGrabState = HatchArmGrabStates.OUT;
    }

    public void hatchArmGrab(boolean hatchArmGrabToggle) {
        switch (hatchArmGrabState) {
            case IN:
                if (!hatchArmGrabToggle) {
                    hatchArmGrabMotor.set(Constants.HATCH_ARM_GRAB_SPEED);
                    hatchArmGrabState = HatchArmGrabStates.TO_OUT_INTER;
                }
                break;
            case OUT:
                if (hatchArmGrabToggle) {
                    hatchArmGrabMotor.set(Constants.HATCH_ARM_GRAB_SPEED);
                    hatchArmGrabState = HatchArmGrabStates.TO_IN_INTER;
                }
                break;
            case TO_IN:
                if (grabLimitSwitch.get()) {
                    hatchArmGrabMotor.set(0);
                    hatchArmGrabState = HatchArmGrabStates.IN;
                } else if (!hatchArmGrabToggle) {
                    hatchArmGrabMotor.set(-hatchArmGrabMotor.get());
                    hatchArmGrabState = HatchArmGrabStates.TO_OUT;
                }
                break;
            case TO_OUT:
                if (grabLimitSwitch.get()) {
                    hatchArmGrabMotor.set(0);
                    hatchArmGrabState = HatchArmGrabStates.OUT;
                } else if (hatchArmGrabToggle) {
                    hatchArmGrabMotor.set(-hatchArmGrabMotor.get());
                    hatchArmGrabState = HatchArmGrabStates.TO_IN;
                }
                break;
            case TO_IN_INTER:
                if (!grabLimitSwitch.get()) {
                    hatchArmGrabState = HatchArmGrabStates.TO_IN;
                }
                break;
            case TO_OUT_INTER:
                if (!grabLimitSwitch.get()) {
                    hatchArmGrabState = HatchArmGrabStates.TO_OUT;
                }
                break;
        }
    }

    public void hatchArmManualMove(boolean lowerHatchArmButton, boolean raiseHatchArmButton) {
        switch(hatchArmManualMoveState) {
            case NOT_MOVING:
                if (lowerHatchArmButton && !raiseHatchArmButton) {
                    hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmManualMoveState = HatchArmManualMoveStates.MOVING_TOWARDS_FLOOR;
                } else if (raiseHatchArmButton && !lowerHatchArmButton) {
                    hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmManualMoveState = HatchArmManualMoveStates.MOVING_TOWARDS_INITIAL;
                }
                break;
            case MOVING_TOWARDS_FLOOR:
                if (!lowerHatchArmButton && !raiseHatchArmButton || moveLimitSwitches.isFwdLimitSwitchClosed() || moveLimitSwitches.isRevLimitSwitchClosed()) {
                    hatchArmMoveMotor.set(0);
                    hatchArmManualMoveState = HatchArmManualMoveStates.NOT_MOVING;
                } else if (raiseHatchArmButton) {
                    hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmManualMoveState = HatchArmManualMoveStates.MOVING_TOWARDS_INITIAL;
                }
                break;
            case MOVING_TOWARDS_INITIAL:
                if (!lowerHatchArmButton && !raiseHatchArmButton || moveLimitSwitches.isFwdLimitSwitchClosed() || moveLimitSwitches.isRevLimitSwitchClosed()) {
                    hatchArmMoveMotor.set(0);
                    hatchArmManualMoveState = HatchArmManualMoveStates.NOT_MOVING;
                } else if (lowerHatchArmButton) {
                    hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmManualMoveState = HatchArmManualMoveStates.MOVING_TOWARDS_FLOOR;
                }
                break;
        }
    }

    public void hatchArmMove(boolean hatchArmSchemeButton, boolean hatchArmInsideButton, boolean hatchArmVertButton, boolean hatchArmFloorButton, boolean lowerHatchArmButton, boolean raiseHatchArmButton) {
        moveMidSwitchValue = !moveMidSwitch.get();
        SmartDashboard.putString("State", hatchArmMoveState.toString());
        SmartDashboard.putBoolean("Scheme", hatchArmSchemeButton);
        SmartDashboard.putBoolean("MidSwitch", moveMidSwitchValue);
        if (!hatchArmSchemeButton) {
            switch (hatchArmMoveState) {
                case INSIDE:
                    if (hatchArmVertButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                    } else if (hatchArmFloorButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT_TO_FLOOR;
                    }
                    break;
                case VERT:
                    if (hatchArmInsideButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                    } else if (hatchArmFloorButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_FLOOR;
                    }
                    break;
                case FLOOR:
                    if (hatchArmInsideButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT_TO_INSIDE;
                    } else if (hatchArmVertButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                    }
                    break;
                case FLOOR_TO_VERT:
                    if (moveMidSwitchValue) {
                        hatchArmMoveMotor.set(0);
                        hatchArmMoveState = HatchArmMoveStates.VERT;
                    } else if (hatchArmInsideButton) {
                        hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT_TO_INSIDE;
                    } else if (hatchArmFloorButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_FLOOR;
                    }
                    break;
                case FLOOR_TO_VERT_TO_INSIDE:
                    if (moveMidSwitchValue) {
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                    } else if (hatchArmVertButton) {
                        hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                    } else if (hatchArmFloorButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_FLOOR;
                    }
                    break;
                case INSIDE_TO_VERT:
                    if (moveMidSwitchValue) {
                        hatchArmMoveMotor.set(0);
                        hatchArmMoveState = HatchArmMoveStates.VERT;
                    } else if (hatchArmInsideButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                    } else if (hatchArmFloorButton) {
                        hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT_TO_FLOOR;
                    }
                    break;
                case INSIDE_TO_VERT_TO_FLOOR:
                    if (moveMidSwitchValue) {
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_FLOOR;
                    } else if (hatchArmInsideButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                    } else if (hatchArmVertButton) {
                        hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                    }
                    break;
                case VERT_TO_FLOOR:
                    if (moveLimitSwitches.isFwdLimitSwitchClosed() || moveLimitSwitches.isRevLimitSwitchClosed()) {
                        hatchArmMoveMotor.set(0);
                        hatchArmMoveState = HatchArmMoveStates.FLOOR;
                    } else if (hatchArmInsideButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT_TO_INSIDE;
                    } else if (hatchArmVertButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                    }
                    break;
                case VERT_TO_INSIDE:
                    if (moveLimitSwitches.isFwdLimitSwitchClosed() || moveLimitSwitches.isRevLimitSwitchClosed()) {
                        hatchArmMoveMotor.set(0);
                        hatchArmMoveState = HatchArmMoveStates.INSIDE;
                    } else if (hatchArmVertButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                    } else if (hatchArmFloorButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT_TO_FLOOR;
                    }
                    break;
            }
        } else {
            switch (hatchArmMoveState) {
                case INSIDE:
                    if (lowerHatchArmButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                    }
                    break;
                case VERT:
                    if (raiseHatchArmButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                    } else if (lowerHatchArmButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_FLOOR;
                    }
                    break;
                case FLOOR:
                    if (raiseHatchArmButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                    }
                    break;
                case FLOOR_TO_VERT:
                    if (moveMidSwitchValue) {
                        hatchArmMoveMotor.set(0);
                        hatchArmMoveState = HatchArmMoveStates.VERT;
                    } else if (lowerHatchArmButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_FLOOR;
                    }
                    break;
                case FLOOR_TO_VERT_TO_INSIDE:
                    hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                    break;
                case INSIDE_TO_VERT:
                    if (moveMidSwitchValue) {
                        hatchArmMoveMotor.set(0);
                        hatchArmMoveState = HatchArmMoveStates.VERT;
                    } else if (raiseHatchArmButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                    }
                    break;
                case INSIDE_TO_VERT_TO_FLOOR:
                    hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                    break;
                case VERT_TO_FLOOR:
                    if (moveLimitSwitches.isFwdLimitSwitchClosed() || moveLimitSwitches.isRevLimitSwitchClosed()) {
                        hatchArmMoveMotor.set(0);
                        hatchArmMoveState = HatchArmMoveStates.FLOOR;
                    } else if (raiseHatchArmButton) {
                        hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                    }
                    break;
                case VERT_TO_INSIDE:
                    if (moveLimitSwitches.isFwdLimitSwitchClosed() || moveLimitSwitches.isRevLimitSwitchClosed()) {
                        hatchArmMoveMotor.set(0);
                        hatchArmMoveState = HatchArmMoveStates.INSIDE;
                    } else if (lowerHatchArmButton) {
                        hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                        hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                    }
                    break;
            }
        }
    }
}