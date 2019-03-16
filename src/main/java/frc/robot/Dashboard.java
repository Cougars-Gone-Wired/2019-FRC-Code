package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
    private Drive drive;
    //private HatchArm hatchArm;
    //private CargoManip cargoManip;
    private Lift lift;
    //private Logging logging;

    public int refreshCount = 0;

    public Dashboard(Robot robot) {
        drive = robot.getDrive();
        //hatchArm = robot.getHatchArm();
        //cargoManip = robot.getCargoManip();
        lift = robot.getLift();
        //logging = robot.getLogging();
    }
    
    public void showEnabledValues() {
        drive.showDriveModes();
        drive.showLimeLightSpeeds();
        // hatchArm.displayValues();
        // cargoManip.showDashboard();
        lift.showDashboard();
        //logging.showDashboard();
    }

    public void showDisabledValues() {
        refreshDashboard();
    }

    public void refreshDashboard() {
        if(SmartDashboard.getBoolean("Refresh", false)) {
           drive.disabledDashboard();
           SmartDashboard.putBoolean("Save Logger", false);
           //logging.showDashboard();
        }

        refreshCount++;
        if(refreshCount >= 50) {
            SmartDashboard.putBoolean("Refresh", false);
            refreshCount = 0;
        }
    }
}