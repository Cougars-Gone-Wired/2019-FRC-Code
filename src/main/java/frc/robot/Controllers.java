package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Controllers {

	//Manipulator
	private Joystick manipulatorStick;
	
	//Hatch
    private Toggle hatchArmGrabToggle;

	private int dPad;

	private boolean hatchArmGrabButton;
	private boolean hatchGrabInButton;
	private boolean hatchGrabOutButton;
	
	private boolean hatchArmSchemeButton;
	
    private boolean lowerHatchArmButton;
	private boolean raiseHatchArmButton; 


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
	// private Toggle ultrasonicToggle;
	private Toggle driveFineToggle;

	//Lift
	private boolean liftDeployButton;
	private boolean liftRetractButton;
	private boolean liftStopButton;
	private boolean liftWithdrawFromStairButton;
	
	public Controllers() {
		manipulatorStick = new Joystick(Constants.MANIPULATOR_CONTROLLER_PORT);
		mobilityStick = new Joystick(Constants.MOBILITY_CONTROLLER_PORT);

        hatchArmGrabToggle = new Toggle(manipulatorStick, Constants.HATCH_ARM_GRAB_BUTTON);
		driveToggle= new Toggle(mobilityStick, Constants.DRIVE_TOGGLE_BUTTON);
		// ultrasonicToggle = new Toggle(mobilityStick, Constants.ULTRASONIC_TOGGLE_BUTTON);
		driveFineToggle = new Toggle(mobilityStick, Constants.DRIVE_FINE_BUTTON);
	}

	public void setControllerValues() {

		//Hatch Arm
		hatchArmGrabButton = hatchArmGrabToggle.toggle();
		dPad = manipulatorStick.getPOV();
		hatchGrabInButton = manipulatorStick.getRawButton(Constants.HATCH_GRAB_IN_BUTTON);
		hatchGrabOutButton = manipulatorStick.getRawButton(Constants.HATCH_GRAB_OUT_BUTTON);

        raiseHatchArmButton = -manipulatorStick.getRawAxis(Constants.HATCH_ARM_AXIS) > Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD;
        lowerHatchArmButton = -manipulatorStick.getRawAxis(Constants.HATCH_ARM_AXIS) < -Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD;

		//Cargo
		cargoArmAxis = -manipulatorStick.getRawAxis(Constants.CARGO_ARM_AXIS);
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
		liftRetractButton = mobilityStick.getRawButton(Constants.LIFT_RETRACT_BUTTON);
		liftStopButton = mobilityStick.getRawButton(Constants.LIFT_STOP_BUTTON);
		liftWithdrawFromStairButton = mobilityStick.getRawButton(Constants.LIFT_WITHDRAW_FROM_STAIR_BUTTON);

	}

	public void initialize() {
		hatchArmGrabToggle.initialize();
		driveToggle.initialize();
		// ultrasonicToggle.initialize();
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
	
	public boolean isHatchGrabInButton() {
		return hatchGrabInButton;
	}

	public boolean isHatchGrabOutButton() {
		return hatchGrabOutButton;
	}

    public boolean getLowerHatchArmButton() {
        return lowerHatchArmButton;
    }
    
    public boolean getRaiseHatchArmButton() {
        return raiseHatchArmButton;
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
	public Joystick getMobilityStick() {
		return mobilityStick;
	}

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

	// public boolean getUltrasonicToggleValue() {
	// 	return ultrasonicToggle.toggle();
	// }

	// public Toggle getUltrasonicToggle() {
	// 	return ultrasonicToggle;
	// }

	public boolean getDriveFineToggleValue() {
		return driveFineToggle.toggle();
	}

	public Toggle getDriveFineToggle() {
		return driveFineToggle;
	}

	public void setDriveToggle() {
		driveToggle.setDriveToggleValue(SmartDashboard.getBoolean("StartCargoSide", true));
	}

	public boolean isLiftDeployButton() {
		return liftDeployButton;
	}

	public boolean isLiftRetractButton() {
		return liftRetractButton;
	}

	public boolean isLiftStopButton() {
		return liftStopButton;
	}

	public boolean isLiftWithdrawFromStairButton() {
		return liftWithdrawFromStairButton;
	}
}
