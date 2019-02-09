package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {

    Joystick manipulatorStick;

    private double arm;

    private boolean intake;
    private boolean outtake;

    private boolean top;
    private boolean bottom;
    private boolean cargoShip;
    private boolean rocket;

    public Controllers() {
        manipulatorStick = new Joystick(Constants.MANIPULATOR_STICK_PORT);
    }



    public void setControllerValues() {
        arm = manipulatorStick.getRawAxis(Constants.ARM_AXIS);
        intake = manipulatorStick.getRawButton(Constants.INTAKE_BUTTON);
        outtake = manipulatorStick.getRawButton(Constants.OUTTAKE_BUTTON);
        top = manipulatorStick.getRawButton(Constants.TOP_BUTTON);
        bottom = manipulatorStick.getRawButton(Constants.BOTTOM_BUTTON);
        cargoShip = manipulatorStick.getRawButton(Constants.CARGO_SHIP_BUTTON);
        rocket = manipulatorStick.getRawButton(Constants.ROCKET_BUTTON);
    }

    /**
     * @return the arm
     */
    public double getArm() {
        return arm;
    }

    public boolean getIntake() {
        return intake;
    }

    public boolean getOuttake() {
        return outtake;
    }

        /**
     * @return the top
     */
    public boolean getTop() {
        return top;
    }

    /**
     * @return the bottom
     */
    public boolean getBottom() {
        return bottom;
    }

    /**
     * @return the cargoShip
     */
    public boolean getCargoShip() {
        return cargoShip;
    }

    /**
     * @return the rocket
     */
    public boolean getRocket() {
        return rocket;
    }
}