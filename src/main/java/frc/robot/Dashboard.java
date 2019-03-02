package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
    private Controllers controllers;
    private Drive drive;
    private HatchArm hatchArm;
    private CargoManip cargoManip;
    private Lift lift;
    private Logging logging;

    public int refreshCount = 0;

    public Dashboard(Robot robot) {
        controllers = robot.getControllers();
        drive = robot.getDrive();
        hatchArm = robot.getHatchArm();
        cargoManip = robot.getCargoManip();
        lift = robot.getLift();
        logging = robot.getLogging();
    }

    
    public void showEnabledValues() {
        drive.showDashboard();
        hatchArm.displayValues();
        cargoManip.sensorLight();
        lift.isReadyToBackUpFromStairs();
        lift.isTopLimit();
        logging.showDashboard();
    }

    public void showDisabledValues() {
        refreshDashboard();

    }

    public void refreshDashboard() {
        if(SmartDashboard.getBoolean("Refresh", false)) {
            SmartDashboard.putBoolean("StartCargoSide", false);
            SmartDashboard.putBoolean("Save Logger", false);
        }

        refreshCount++;
        if(refreshCount >= 50) {
            SmartDashboard.putBoolean("Refresh", false);
            refreshCount = 0;
        }
    }
}