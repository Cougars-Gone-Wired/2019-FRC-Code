package frc.robot;

public class Dashboard {
    private Drive drive;
    private HatchArm hatchArm;
    private CargoManip cargoManip;
    private Lift lift;
    private Logging logging;

    public int refreshCount = 0;

    public Dashboard(Robot robot) {
        drive = robot.getDrive();
        hatchArm = robot.getHatchArm();
        cargoManip = robot.getCargoManip();
        lift = robot.getLift();
        logging = robot.getLogging();
    }
    
    public void showEnabledValues() {
        drive.showDashboard();
        hatchArm.displayValues();
        cargoManip.showDashboard();
        lift.isReadyToBackUpFromStairs();
        lift.isTopLimit();
        logging.showDashboard();
    }

    public void showDisabledValues() {
        drive.refreshDashboard();
    }
}