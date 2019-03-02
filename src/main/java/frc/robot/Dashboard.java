package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
    private Drive drive;
    private Controllers controllers;
    private HatchArm hatchArm;
    private CargoManip cargoManip;
    private Lift lift;

    public int refreshCount = 0;

    public Dashboard(Robot robot) {
        drive = robot.getDrive();
        controllers = robot.getControllers();
        hatchArm = robot.getHatchArm();
        cargoManip = robot.getCargoManip();
        lift = robot.getLift();
    }

    public void showDisabledValues() {
        refreshDashboard();
        
    }

    public void showEnabledValues() {
        drive.showDashboard();
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