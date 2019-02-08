package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Toggle {
	// declarations of variables used in this class
	private boolean buttonState;
    private boolean state = false;


	// declarations of things that are assigned in the constructor
	private Joystick stick;
	private int buttonNumber;

	public Toggle(Joystick stick, int buttonNumber) {
		// assigning previously declared things to the things passed into the
		// constructor
		this.stick = stick;
		this.buttonNumber = buttonNumber;
		initialize();
	}

	public void initialize() {
		state = false;
	}

	// method that makes a button state true on one press and false on the next
	// press and so on and so forth
	public boolean toggle() {
		if (stick.getRawButton(buttonNumber)) {
			if (!buttonState) {
				state = !state;
			}
			buttonState = true;
		} else {
			buttonState = false;
		}
		return state;
    }
    
    public void setDriveToggleValue(boolean driveState) {
        if (driveState) {
            state = true;
        }
        else {
            state = false;
        }
	}
	
	public void setOutput(boolean state){
		this.state = state;
	}
}