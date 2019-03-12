package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sides {

    public enum SideStates {
        HATCH_SIDE, CARGO_SIDE
    }
    public static SideStates currentSide;

    static boolean hatchSide;

    public static void setSide(boolean sideToggle) {
        if(sideToggle) {
            currentSide = SideStates.CARGO_SIDE;
            hatchSide = false;
        } else {
            currentSide = SideStates.HATCH_SIDE;
            hatchSide = true;
        }
    }

    public static void showSide() {
        SmartDashboard.putString("Side Facing", currentSide.toString());
    }
}