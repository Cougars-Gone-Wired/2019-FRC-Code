
package frc.robot;
import edu.wpi.first.wpilibj.Joystick;

public class Controllers {

    private Joystick manipulatorStick;

    private double cargoArmAxis;

    private boolean cargoArmIntakeButton;
    private boolean cargoArmOuttakeButton;

    private boolean cargoArmTopButton;
    private boolean cargoArmBottomButton;
    private boolean cargoArmCargoShipButton;
    private boolean cargoArmRocketButton;

    public Controllers() {
        manipulatorStick = new Joystick(Constants.MANIPULATOR_CONTROLLER_PORT);
    }

    public void setControllerValues() {
        cargoArmAxis = manipulatorStick.getRawAxis(Constants.CARGO_ARM_AXIS);
        cargoArmIntakeButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_INTAKE_BUTTON);
        cargoArmOuttakeButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_OUTTAKE_BUTTON);
        cargoArmTopButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_TOP_BUTTON);
        cargoArmBottomButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_BOTTOM_BUTTON);
        cargoArmCargoShipButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_CARGO_SHIP_BUTTON);
        cargoArmRocketButton = manipulatorStick.getRawButton(Constants.CARGO_ARM_ROCKET_BUTTON);
    }

    public double getCargoArmAxis() {
        return arm;
    }

    public boolean getCargoArmIntakeButton() {
        return intake;
    }

    public boolean getCargoArmOuttakeButton() {
        return outtake;
    }

    public boolean getTop() {
        return top;
    }

    public boolean getBottom() {
        return bottom;
    }

    public boolean getCargoShip() {
        return cargoShip;
    }

    public boolean getRocket() {
        return rocket;
    }
}