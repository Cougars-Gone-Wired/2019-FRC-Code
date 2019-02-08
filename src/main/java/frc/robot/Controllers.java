package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Controllers {

	//Manipulator
	private Joystick manipulatorStick;
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
	
	//Mobility
	private Joystick mobilityStick;

	private double driveSpeedAxis;
	private double driveTurnAxis;

	private Toggle driveToggle;
	private Toggle ultrasonicToggle;

	//Lift
	private boolean liftIsDeployed;
	private Toggle liftToggleDeployer;
	
	public Controllers() {
		manipulatorStick = new Joystick(Constants.MANIPULATOR_CONTROLLER_PORT);
		mobilityStick = new Joystick(Constants.MOBILITY_CONTROLLER_PORT);

        hatchArmGrabToggle = new Toggle(manipulatorStick, Constants.HATCH_ARM_GRAB_BUTTON);
        hatchArmSchemeToggle = new Toggle(manipulatorStick, Constants.HATCH_ARM_SCHEME_BUTTON);
		driveToggle= new Toggle(mobilityStick, Constants.DRIVE_TOGGLE_BUTTON);
		ultrasonicToggle = new Toggle(mobilityStick, Constants.ULTRASONIC_TOGGLE_BUTTON);

		//Lift
		liftToggleDeployer = new Toggle(mobilityStick, Constants.LIFT_TOGGLER_BUTTON);
	}

	public void setControllerValues() {
		//Manipulator
		hatchArmGrabButton = hatchArmGrabToggle.toggle();
        dPad = manipulatorStick.getPOV();
        
        hatchArmSchemeButton = hatchArmSchemeToggle.toggle();

        lowerHatchArmButton = manipulatorStick.getRawAxis(Constants.HATCH_ARM_AXIS) > Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD;
        raiseHatchArmButton = manipulatorStick.getRawAxis(Constants.HATCH_ARM_AXIS) < -Constants.HATCH_ARM_MOVE_AXIS_THRESHHOLD;

        hatchArmFloorButton = manipulatorStick.getPOV() == 270;
        hatchArmVertButton = manipulatorStick.getPOV() == 0;
		hatchArmInsideButton = manipulatorStick.getPOV() == 90;
		
		//Mobility
		driveSpeedAxis = mobilityStick.getRawAxis(Constants.DRIVE_SPEED_AXIS);
		driveTurnAxis = mobilityStick.getRawAxis(Constants.DRIVE_TURN_AXIS);

		//Lift
		liftIsDeployed = liftToggleDeployer.toggle();
	}

	public void initialize() {
		hatchArmGrabToggle.initialize();
		hatchArmSchemeToggle.initialize();
		driveToggle.initialize();
		ultrasonicToggle.initialize();
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

	public boolean isLiftIsDeployed() {
		return liftIsDeployed;
	}

	public Toggle getLiftToggleDeployer() {
		return liftToggleDeployer;
	}
}
