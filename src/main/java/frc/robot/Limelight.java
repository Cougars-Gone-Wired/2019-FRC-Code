package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
    
    static final double TURN_CONSTANT = .068;
    static final double DRIVE_CONSTANT = .1;
    static final double DESIRED_TARGET_AREA = 8; //camera 32 inches away
    static final double MAX_SPEED = .7;

    boolean validTarget = false;
    double driveSpeed = 0.0;
    double turnSpeed = 0.0;

    NetworkTable table;
    double tv;
    double tx;
    double ty;
    double ta;

    public Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        setLight(false);
    }

    public void limelight() {
        tv = table.getEntry("tv").getDouble(0);
        tx = table.getEntry("tx").getDouble(0);
        ty = table.getEntry("ty").getDouble(0);
        ta = table.getEntry("ta").getDouble(0);

        if (tv < 1) {
            validTarget = false;
            driveSpeed = 0.0;
            turnSpeed = 0.0;
            return;
        }
        validTarget = true;

        if (tx > 0) {
            tx += 5;
        }  

        turnSpeed = -tx * TURN_CONSTANT;
        driveSpeed = (DESIRED_TARGET_AREA - ta) * DRIVE_CONSTANT;
        if (driveSpeed > MAX_SPEED) driveSpeed = MAX_SPEED;
    }

    public void setLight(boolean turnOnLight){
        if (turnOnLight) {
            table.getEntry("ledMode").setNumber(3);
            table.getEntry("camMode").setNumber(0);
        } else {
            table.getEntry("ledMode").setNumber(1);
            table.getEntry("camMode").setNumber(1);
        }
    }

    public double getDriveSpeed() {
        return driveSpeed;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    //Stealin yo tv
    //whatcha gonna do
    public double getTv() {
        return tv;
    }

    public double getTx() {
        return tx;
    }

    public double getTa() {
        return ta;
    }
}