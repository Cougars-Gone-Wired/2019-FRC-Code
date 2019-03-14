package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    HatchArmManualMoveStates hatchArmManualMoveState;
    HatchArmMoveStates hatchArmMoveState;
    
    private WPI_TalonSRX hatchArmMoveMotor;
    private SensorCollection moveLimitSwitches;
    private DigitalInput moveMidSwitch;
    private double speed;

    private boolean floorSwitch;
    private boolean verticalSwitch;
    private boolean initialSwitch;

    public HatchArm() {
        hatchArmMoveMotor = new WPI_TalonSRX(Constants.HATCH_ARM_MOVE_MOTOR_ID);
        hatchArmMoveMotor.setNeutralMode(NeutralMode.Brake);
        hatchArmMoveMotor.configOpenloopRamp(Constants.RAMP_TIME);
        
        moveLimitSwitches = new SensorCollection(hatchArmMoveMotor);
        moveMidSwitch = new DigitalInput(Constants.MID_SWITCH_PORT);
        initialize();
    }

    public void initialize() {
        hatchArmMoveMotor.set(0);
        hatchArmManualMoveState = HatchArmManualMoveStates.NOT_MOVING;
        hatchArmMoveState = HatchArmMoveStates.INSIDE;
    }

    public void hatchArmManualMove(double armAxis) { // This is for testing and for setting the hatch arm to the inside state before match
        speed = armAxis * Constants.HATCH_ARM_MOVE_SPEED;

        floorSwitch = moveLimitSwitches.isFwdLimitSwitchClosed();
        verticalSwitch = !moveMidSwitch.get();
        initialSwitch = moveLimitSwitches.isRevLimitSwitchClosed();

        switch(hatchArmManualMoveState) {
            case NOT_MOVING:
                if (armAxis > Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD) { // Start moving towards the floor state
                    hatchArmMoveMotor.set(speed);
                    hatchArmManualMoveState = HatchArmManualMoveStates.MOVING_TOWARDS_FLOOR;
                } else if (armAxis < -Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD) { // Start moving towards the initial state
                    hatchArmMoveMotor.set(speed);
                    hatchArmManualMoveState = HatchArmManualMoveStates.MOVING_TOWARDS_INITIAL;
                }
                break;
            case MOVING_TOWARDS_FLOOR:
                if (((-Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD < armAxis) && (armAxis < Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD)) || floorSwitch) { // Stop
                    hatchArmMoveMotor.set(0);
                    hatchArmManualMoveState = HatchArmManualMoveStates.NOT_MOVING;
                } else if (armAxis > Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD) { // Switch Direction
                    hatchArmMoveMotor.set(speed);
                    hatchArmManualMoveState = HatchArmManualMoveStates.MOVING_TOWARDS_INITIAL;
                } else {
                    hatchArmMoveMotor.set(speed);
                }
                break;
            case MOVING_TOWARDS_INITIAL:
                if (((-Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD < armAxis) && (armAxis < Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD)) || initialSwitch) { // Stop
                    hatchArmMoveMotor.set(0);
                    hatchArmManualMoveState = HatchArmManualMoveStates.NOT_MOVING;
                } else if (armAxis < -Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD) { // Switch Direction
                    hatchArmMoveMotor.set(speed);
                    hatchArmManualMoveState = HatchArmManualMoveStates.MOVING_TOWARDS_FLOOR;
                } else {
                    hatchArmMoveMotor.set(speed);
                }
                break;
        }
    }

    public void hatchArmMove(boolean lowerHatchArmButton, boolean raiseHatchArmButton) { // This is the main movement method.  NOTE: It will act wierd if you don't start the hatch arm in the inside state
        floorSwitch = moveLimitSwitches.isFwdLimitSwitchClosed();
        verticalSwitch = !moveMidSwitch.get();
        initialSwitch = moveLimitSwitches.isRevLimitSwitchClosed();

        switch (hatchArmMoveState) {
            case INSIDE:
                if (lowerHatchArmButton) { // Start moving towards the vertical state from this postiion
                    hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                }
                break;
            case VERT:
                if (raiseHatchArmButton) { // Start moving towards the initial state from this postiion
                    hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                } else if (lowerHatchArmButton) { // Start moving towards the floor state from this postiion
                    hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.VERT_TO_FLOOR;
                }
                break;
            case FLOOR:
                if (raiseHatchArmButton) { // Start moving towards the vertical state from this postiion
                    hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                }
                break;
            case FLOOR_TO_VERT:
                if (verticalSwitch) { // Stop moving when the vertical state has been reached
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.VERT;
                } else if (lowerHatchArmButton) { // Switch directions
                    hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.VERT_TO_FLOOR;
                } else if (initialSwitch) { // This is redundant and is only used in case the midswitch fails.  Stop moving when the inside state has been reached
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.INSIDE;
                }
                break;
            case INSIDE_TO_VERT:
                if (verticalSwitch) { // Stop moving when the vertical state has been reached
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.VERT;
                } else if (raiseHatchArmButton) { // Switch directions
                    hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.VERT_TO_INSIDE;
                } else if (floorSwitch) { // This is redundant and is only used in case the midswitch fails or, the more likely possibility, someone forgets to start the robot with the hatch arm in the inside position.  Stop moving when the floor state has been reached
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.FLOOR; 
                }
                break;
            case VERT_TO_FLOOR:
                if (initialSwitch) { // Stop moving when the floor state has been reached
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.FLOOR;
                } else if (raiseHatchArmButton) { // Switch directions
                    hatchArmMoveMotor.set(-Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.FLOOR_TO_VERT;
                }
                break;
            case VERT_TO_INSIDE:
                if (floorSwitch) { // Stop moving when the inside state has been reached
                    hatchArmMoveMotor.set(0);
                    hatchArmMoveState = HatchArmMoveStates.INSIDE;
                } else if (lowerHatchArmButton) { // Switch directions
                    hatchArmMoveMotor.set(Constants.HATCH_ARM_MOVE_SPEED);
                    hatchArmMoveState = HatchArmMoveStates.INSIDE_TO_VERT;
                }
                break;
        }
    }
    
    public void displayValues() {
        //SmartDashboard.putBoolean("Hatch Arm Floor Position", floorSwitch);
        //SmartDashboard.putBoolean("Hatch Arm Vertical Position", verticalSwitch);
        //SmartDashboard.putBoolean("Hatch Arm Initial Position", initialSwitch);
    }

    public double getHatchArmMoveMotorVoltage() {
        return hatchArmMoveMotor.getMotorOutputVoltage();
    }

    public double getHatchArmMoveMotorCurrent() {
        return hatchArmMoveMotor.getOutputCurrent();
    }
}