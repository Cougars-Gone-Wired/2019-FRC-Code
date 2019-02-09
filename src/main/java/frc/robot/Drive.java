package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
    public enum DriveStates {
        DRIVE_CARGO_SIDE, DRIVE_HATCH_SIDE
    }
    public enum DriveModes {
        DRIVE_STANDARD, DRIVE_DETECT
    }

    private DriveStates driveState;
    private DriveModes driveMode;
    
    private WPI_TalonSRX frontLeftMotor;
    private WPI_TalonSRX midLeftMotor;
    private WPI_TalonSRX backLeftMotor;

    private WPI_TalonSRX frontRightMotor;
    private WPI_TalonSRX midRightMotor;
    private WPI_TalonSRX backRightMotor;

    private DifferentialDrive robotDrive;

    public Drive() {
        frontLeftMotor = new WPI_TalonSRX(Constants.FRONT_LEFT_MOTOR_ID);
        midLeftMotor = new WPI_TalonSRX(Constants.MID_LEFT_MOTOR_ID);
        backLeftMotor = new WPI_TalonSRX(Constants.BACK_LEFT_MOTOR_ID);
        midLeftMotor.follow(frontLeftMotor);
        backLeftMotor.follow(frontLeftMotor);


        frontRightMotor = new WPI_TalonSRX(Constants.FRONT_RIGHT_MOTOR_ID);
        midRightMotor = new WPI_TalonSRX(Constants.MID_RIGHT_MOTOR_ID);
        backRightMotor =  new WPI_TalonSRX(Constants.BACK_RIGHT_MOTOR_ID);
        midRightMotor.follow(frontRightMotor);
        backRightMotor.follow(frontRightMotor);

        robotDrive = new DifferentialDrive(frontLeftMotor, frontRightMotor);
        robotDrive.setDeadband(Constants.DRIVE_DEADZONE);
        robotDrive.setSafetyEnabled(false);

        driveState = DriveStates.DRIVE_CARGO_SIDE;

        initalize();
    }

    public void initalize() {
        SmartDashboard.putBoolean("StartCargoSide", true);

        frontLeftMotor.set(0);
        midLeftMotor.set(0);
        backLeftMotor.set(0);

        frontRightMotor.set(0);
        midLeftMotor.set(0);
        backRightMotor.set(0);
    }

    public void robotDrive(double driveSpeedAxis, double driveTurnAxis, double hatchUltrasonicImperialValue) {
        driveSpeedAxis = driveSpeedAxis * Constants.DRIVE_SPEED;
        driveTurnAxis = driveTurnAxis * Constants.DRIVE_TURN_SPEED;
        switch (driveMode) {
            case DRIVE_STANDARD:
                switch (driveState) {
                    case DRIVE_HATCH_SIDE:
                        robotDrive.arcadeDrive(driveSpeedAxis, -driveTurnAxis);
                        break;
                    case DRIVE_CARGO_SIDE:
                        robotDrive.arcadeDrive(-driveSpeedAxis, -driveTurnAxis);
                        break;
                }
                break;
            case DRIVE_DETECT:
                if(hatchUltrasonicImperialValue <= 12.0) {
                    //robotDrive.arcadeDrive(0, 0);
                    driveMode = DriveModes.DRIVE_STANDARD; 
                } else {
                    //robotDrive.arcadeDrive(.5, 0);
                }
                break;
        }
    }

    public void setSide(boolean switchSide) {
        if(switchSide) {
            driveState = DriveStates.DRIVE_CARGO_SIDE;
        } else {
            driveState = DriveStates.DRIVE_HATCH_SIDE;
        }
    }

    public void setMode(boolean switchMode) {
        if(switchMode) {
            driveMode = DriveModes.DRIVE_STANDARD;
        } else {
            driveMode = DriveModes.DRIVE_DETECT;
        }
    }

    public void initialSide() {
        if(SmartDashboard.getBoolean("StartCargoSide", true)) {
            driveState = DriveStates.DRIVE_CARGO_SIDE;
        } else {
            driveState = DriveStates.DRIVE_HATCH_SIDE;
        }
    }

    public void showDashboard() {
        SmartDashboard.putString("Side Facing", driveState.toString());
        SmartDashboard.putNumber("RoboRIO Voltage", getBatteryVoltage());

        ///Front Left Motor
        SmartDashboard.putNumber("FLVoltage", getFrontLeftMotorVoltage());
        SmartDashboard.putNumber("FLCurrent", getFrontLeftMotorCurrent());
        //Front Right Motor
        SmartDashboard.putNumber("FRVoltage", getFrontRightMotorVoltage());
        SmartDashboard.putNumber("FRCurrent", getFrontRightMotorCurrent());

        // //Back Left Motor
        // SmartDashboard.putNumber("BLVoltage", getBackLeftMotorVoltage());
        // SmartDashboard.putNumber("BLCurrent", getBackLeftMotorCurrent());

        // //Back Right Motor
        // SmartDashboard.putNumber("BRVoltage", getBackRightMotorVoltage());
        // SmartDashboard.putNumber("BRCurrent", getBackRightMotorCurrent());
    }

    
    //RoboRIO Battery Voltage
    public double getBatteryVoltage() {
        return RobotController.getBatteryVoltage();
    }

    //Front Left Motor
    public double getFrontLeftMotorVoltage() {
        return frontLeftMotor.getMotorOutputVoltage();
    }

    public double getFrontLeftMotorCurrent() {
        return frontLeftMotor.getOutputCurrent();
    }

    //Middle Left Motor
    public double getMidLeftMotorVoltage() {
        return midLeftMotor.getMotorOutputVoltage();
    }

    public double getMidLeftMotorCurrent() {
        return midLeftMotor.getOutputCurrent();
    }

    //Back Left Motor
    public double getBackLeftMotorVoltage() {
        return backLeftMotor.getMotorOutputVoltage();
    }

    public double getBackLeftMotorCurrent() {
        return backLeftMotor.getOutputCurrent();
    }

    //Front Right Motor
    public double getFrontRightMotorVoltage() {
        return frontRightMotor.getMotorOutputVoltage();
    }

    public double getFrontRightMotorCurrent() {
        return frontRightMotor.getOutputCurrent();
    }

    //Middle Right Motor
    public double getMidRightMotorVoltage() {
        return midRightMotor.getMotorOutputVoltage();
    }

    public double getMidRightMotorCurrent() {
        return midRightMotor.getOutputCurrent();
    }

    //Back Right Motor
    public double getBackRightMotorVoltage() {
        return backRightMotor.getMotorOutputVoltage();
    }

    public double getBackRightMotorCurrent() {
        return backRightMotor.getOutputCurrent();
    }
}
