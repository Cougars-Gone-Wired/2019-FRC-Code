package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Controllers {
	enum ControllerNames {
		
	}

	//Manipulator
	private Joystick manipulatorStick;
	
	//Hatch
    private Toggle hatchArmGrabToggle;
	private Toggle hatchArmSchemeToggle;

	private int dPad;

	private boolean hatchArmGrabButton;
	
	private boolean hatchArmSchemeButton;
	
    private boolean lowerHatchArmButton;
	private boolean raiseHatchArmButton; 
	
    private boolean hatchArmFloorButton;
    private boolean hatchArmVertButton;
    private boolean hatchArmInsideButton;

	//Cargo
	private double cargoArmAxis;

	private double cargoArmIntakeAxis;
	private double cargoArmOuttakeAxis;
	
	private boolean cargoArmTopButton;
	private boolean cargoArmBottomButton;
	private boolean cargoArmCargoShipButton;
	private boolean cargoArmRocketButton;
	
	//Mobility
	private Joystick mobilityStick;

	private double driveSpeedAxis;
	private double driveTurnAxis;

	private Toggle driveToggle;
	private Toggle ultrasonicToggle;

	//Lift
	private boolean liftDeployButton;
	private boolean liftStopButton;
	private boolean liftWithdrawFromStairButton;
	public enum LiftStates {
        LOCK, STOP, EN, EC
	}
	LiftStates liftStates;
	
	public Controllers() {
		manipulatorStick = new Joystick(Constants.MANIPULATOR_CONTROLLER_PORT);
		mobilityStick = new Joystick(Constants.MOBILITY_CONTROLLER_PORT);

        hatchArmGrabToggle = new Toggle(manipulatorStick, Constants.HATCH_ARM_GRAB_BUTTON);
        hatchArmSchemeToggle = new Toggle(manipulatorStick, Constants.HATCH_ARM_SCHEME_BUTTON);
		driveToggle= new Toggle(mobilityStick, Constants.DRIVE_TOGGLE_BUTTON);
		ultrasonicToggle = new Toggle(mobilityStick, Constants.ULTRASONIC_TOGGLE_BUTTON);
	}

	public void setControllerValues() {

		//Hatch Arm
		hatchArmGrabButton = hatchArmGrabToggle.toggle();
        dPad = manipulatorStick.getPOV();
        
        hatchArmSchemeButton = hatchArmSchemeToggle.toggle();

        lowerHatchArmButton = manipulatorStick.getRawAxis(Constants.HATCH_ARM_AXIS) > Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD;
        raiseHatchArmButton = manipulatorStick.getRawAxis(Constants.HATCH_ARM_AXIS) < -Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD;

        hatchArmFloorButton = manipulatorStick.getPOV() == 270;
        hatchArmVertButton = manipulatorStick.getPOV() == 0;
		hatchArmInsideButton = manipulatorStick.getPOV() == 90;

		//Cargo
		//cargoArmAxis = manipulatorStick.getRawAxis(Constants.CARGO_ARM_AXIS);
		cargoArmIntakeAxis = manipulatorStick.getRawAxis(Constants.CARGO_INTAKE_AXIS);
		cargoArmOuttakeAxis = manipulatorStick.getRawAxis(Constants.CARGO_OUTTAKE_AXIS);
		cargoArmTopButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_TOP_BUTTON);
		cargoArmBottomButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_BOTTOM_BUTTON);
		cargoArmCargoShipButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_CARGO_SHIP_BUTTON);
		cargoArmRocketButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_ROCKET_BUTTON);
		
		//Mobility
		driveSpeedAxis = mobilityStick.getRawAxis(Constants.DRIVE_SPEED_AXIS);
		driveTurnAxis = mobilityStick.getRawAxis(Constants.DRIVE_TURN_AXIS);

		//Lift
		//liftDeployButton = liftToggleDeployer.toggle();
		/*if(manipulatorStick.getRawButton(3) && liftToggleDeployer.toggle()){
			liftDeployButton = true;
			liftToggleDeployer.toggle();
		}*/
		liftDeployButton = mobilityStick.getRawButton(Constants.LIFT_DEPLOY_BUTTON);
		liftStopButton = mobilityStick.getRawButton(Constants.LIFT_STOP_BUTTON);
		liftWithdrawFromStairButton = mobilityStick.getRawButton(Constants.LIFT_WITHDRAW_FROM_STAIR_BUTTON);

	}

	public void initialize() {
		hatchArmGrabToggle.initialize();
		hatchArmSchemeToggle.initialize();
		driveToggle.initialize();
		ultrasonicToggle.initialize();
	}

	public void rumble(Joystick controller, double intensity) {
		controller.setRumble(RumbleType.kLeftRumble, intensity);
		controller.setRumble(RumbleType.kRightRumble, intensity);
	}

	//Manipulator
	public int getDPad() {
        return dPad;
    }

    public boolean getHatchArmSchemeButton() {
        return hatchArmSchemeButton;
    }
    
    public boolean getHatchArmGrabButton() {
        return hatchArmGrabButton;
    }
    
    public boolean getLowerHatchArmButton() {
        return lowerHatchArmButton;
    }
    
    public boolean getRaiseHatchArmButton() {
        return raiseHatchArmButton;
    }

    public boolean getHatchArmFloorButton() {
        return hatchArmFloorButton;
    }

    public boolean getHatchArmVertButton() {
        return hatchArmVertButton;
    }

    public boolean getHatchArmInsideButton() {
        return hatchArmInsideButton;
	}
	
		//Cargo
	public double getCargoArmAxis() {
        return cargoArmAxis;
    }

    public double getCargoArmIntakeAxis() {
        return cargoArmIntakeAxis;
    }

    public double getCargoArmOuttakeAxis() {
        return cargoArmOuttakeAxis;
    }

    public boolean getCargoArmTopButton() {
        return cargoArmTopButton;
    }

    public boolean getCargoArmBottomButton() {
        return cargoArmBottomButton;
    }

    public boolean getCargoArmCargoShipButton() {
        return cargoArmCargoShipButton;
    }

    public boolean getCargoArmRocketButton() {
        return cargoArmRocketButton;
    }

	//Mobility
	public double getDriveSpeedAxis() {
		return driveSpeedAxis;
	}

	public double getDriveTurnAxis() {
		return driveTurnAxis;
	}
	
	public boolean getDriveToggleValue() {
		return driveToggle.toggle();
	}

	public Toggle getDriveToggle() {
		return driveToggle;
	}

	public boolean getUltrasonicToggleValue() {
		return ultrasonicToggle.toggle();
	}

	public Toggle getUltrasonicToggle() {
		return ultrasonicToggle;
	}

	public void setDriveToggle() {
		driveToggle.setDriveToggleValue(SmartDashboard.getBoolean("StartCargoSide", true));
	}

	public boolean isLiftDeployButton() {
		return liftDeployButton;
	}

	public boolean isLiftStopButton() {
		return liftStopButton;
	}

	public boolean isLiftWithdrawFromStairButton() {
		return liftWithdrawFromStairButton;
	}
}
