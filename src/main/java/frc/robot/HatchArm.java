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
        INSIDE, VERT, FLOOR, FLOOR_TO_VERT, INSIDE_TO_VERT, VERT_TO_FLOOR, VERT_TO_INSIDE
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
        SmartDashboard.putBoolean("hatchlimit", grabLimitSwitch.get());
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
                if (!grabLimitSwitch.get()) {
                    hatchArmGrabMotor.set(0);
                    hatchArmGrabState = HatchArmGrabStates.IN;
                } else if (!hatchArmGrabToggle) {
                    hatchArmGrabMotor.set(-hatchArmGrabMotor.get());
                    hatchArmGrabState = HatchArmGrabStates.TO_OUT;
                }
                break;
            case TO_OUT:
                if (!grabLimitSwitch.get()) {
                    hatchArmGrabMotor.set(0);
                    hatchArmGrabState = HatchArmGrabStates.OUT;
                } else if (hatchArmGrabToggle) {
                    hatchArmGrabMotor.set(-hatchArmGrabMotor.get());
                    hatchArmGrabState = HatchArmGrabStates.TO_IN;
                }
                break;
            case TO_IN_INTER:
                if (grabLimitSwitch.get()) {
                    hatchArmGrabState = HatchArmGrabStates.TO_IN;
                }
                break;
            case TO_OUT_INTER:
                if (grabLimitSwitch.get()) {
                    hatchArmGrabState = HatchArmGrabStates.TO_OUT;
                }
                break;
        }
    }

    public enum Grab2States {
        NOT_MOVING, GOING_IN, GOING_OUT
    }
    Grab2States currentGrab2State = Grab2States.NOT_MOVING;

    public void grab2(boolean inButton, boolean outButton) {
        switch(currentGrab2State) {
            case NOT_MOVING:
                if (inButton && !outButton) {
                    hatchArmGrabMotor.set(Constants.HATCH_ARM_GRAB_SPEED);
                    currentGrab2State = Grab2States.GOING_IN;
                } else if (outButton && !inButton) {
                    hatchArmGrabMotor.set(-Constants.HATCH_ARM_GRAB_SPEED);
                    currentGrab2State = Grab2States.GOING_OUT;
                }
                break;
            case GOING_IN:
                if (!inButton || outButton) {
                    hatchArmGrabMotor.set(0);
                    currentGrab2State = Grab2States.NOT_MOVING;
                }
                break;
            case GOING_OUT:
                if (!outButton || inButton) {
                    hatchArmGrabMotor.set(0);
                    currentGrab2State = Grab2States.NOT_MOVING;
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

    public void hatchArmMove(boolean lowerHatchArmButton, boolean raiseHatchArmButton) {
        moveMidSwitchValue = !moveMidSwitch.get();
        SmartDashboard.putString("State", hatchArmMoveState.toString());
        SmartDashboard.putBoolean("MidSwitch", moveMidSwitchValue);
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
            case INSIDE_TO_VERT:
                if (moveMidSwitchValue) {
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.VERT;
                } else if (raiseHatchArmButton) {
                    hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                }
                break;
            case VERT_TO_FLOOR:
                if (moveLimitSwitches.isRevLimitSwitchClosed()) {
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.FLOOR;
                } else if (raiseHatchArmButton) {
                    hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                }
                break;
            case VERT_TO_INSIDE:
                if (moveLimitSwitches.isFwdLimitSwitchClosed()) {
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.INSIDE;
                } else if (lowerHatchArmButton) {
                    hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                }
                break;
        }
    }

    public double getHatchArmMoveMotorVoltage() {
        return hatchArmMoveMotor.getMotorOutputVoltage();
    }

    public double getHatchArmMoveMotorCurrent() {
        return hatchArmMoveMotor.getOutputCurrent();
    }

    public double getHatchArmGrabMotorVoltage() {
        return hatchArmGrabMotor.getMotorOutputVoltage();
    }

    public double getHatchArmGrabMotorCurrent() {
        return hatchArmGrabMotor.getOutputCurrent();
    }
}