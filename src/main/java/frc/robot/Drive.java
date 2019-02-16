package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;

public class Drive {
    public enum DriveModes {
        DRIVE_STANDARD, DRIVE_TO_STAIRS, BACKING_UP
    }
    public enum DriveStates {
        DRIVE_CARGO_SIDE, DRIVE_HATCH_SIDE
    }
    
    public DriveModes driveMode;
    private DriveStates driveState;

    private WPI_TalonSRX frontLeftMotor; //Based off Hatch Side
    private WPI_TalonSRX midLeftMotor;
    private WPI_TalonSRX backLeftMotor;

    private WPI_TalonSRX frontRightMotor;
    private WPI_TalonSRX midRightMotor;
    private WPI_TalonSRX backRightMotor;

    private DifferentialDrive robotDrive;

    private SensorCollection leftSensors;
    private SensorCollection rightSensors;

    private Encoders encoders;

    /**
     *  hello
     * I have destroyed the universe
     * @param accepts the_universe.app
     * @return the_nothing.txt (empty text file)
     * @throws InvalidUniverseException
     * @see
     * {@code}> Executing task: destroy_the_universe.exe <
     * 
     */
    public Drive() {
        midLeftMotor = new WPI_TalonSRX(Constants.MID_LEFT_MOTOR_ID);
        midLeftMotor.setNeutralMode(NeutralMode.Brake);
        midLeftMotor.configOpenloopRamp(Constants.RAMP_TIME);

        frontLeftMotor = new WPI_TalonSRX(Constants.FRONT_LEFT_MOTOR_ID);
        frontLeftMotor.setNeutralMode(NeutralMode.Brake);
        frontLeftMotor.configOpenloopRamp(Constants.RAMP_TIME);

        backLeftMotor = new WPI_TalonSRX(Constants.BACK_LEFT_MOTOR_ID);
        backLeftMotor.setNeutralMode(NeutralMode.Brake);
        backLeftMotor.configOpenloopRamp(Constants.RAMP_TIME);
        
        midLeftMotor.setInverted(true);
        frontLeftMotor.follow(midLeftMotor);
        backLeftMotor.follow(frontLeftMotor);


        midRightMotor = new WPI_TalonSRX(Constants.MID_RIGHT_MOTOR_ID);
        midRightMotor.setNeutralMode(NeutralMode.Brake);
        midRightMotor.configOpenloopRamp(Constants.RAMP_TIME);

        frontRightMotor = new WPI_TalonSRX(Constants.FRONT_RIGHT_MOTOR_ID);
        frontRightMotor.setNeutralMode(NeutralMode.Brake);
        frontRightMotor.configOpenloopRamp(Constants.RAMP_TIME);

        backRightMotor =  new WPI_TalonSRX(Constants.BACK_RIGHT_MOTOR_ID);
        backRightMotor.setNeutralMode(NeutralMode.Brake);
        backRightMotor.configOpenloopRamp(Constants.RAMP_TIME);

        frontRightMotor.follow(midRightMotor);
        backRightMotor.follow(frontRightMotor);


        robotDrive = new DifferentialDrive(midLeftMotor, midRightMotor);
        robotDrive.setDeadband(Constants.DRIVE_DEADZONE);
        robotDrive.setSafetyEnabled(false);

        driveState = DriveStates.DRIVE_HATCH_SIDE;
        driveMode = DriveModes.DRIVE_STANDARD;

        leftSensors = midLeftMotor.getSensorCollection();
        rightSensors = midRightMotor.getSensorCollection();

        encoders = new Encoders(this);

        initalize();
    }

    public void initalize() {

        // frontLeftMotor.set(0);
        midLeftMotor.set(0);
        // backLeftMotor.set(0);

        //frontRightMotor.set(0);
        midRightMotor.set(0);
        // backRightMotor.set(0);

        leftSensors.setQuadraturePosition(0, 0);
        rightSensors.setQuadraturePosition(0, 0);
    }

    public void robotDrive(double driveSpeedAxis, double driveTurnAxis, double leftHatchUltrasonic, double rightHatchUltrasonic) {
        if(SmartDashboard.getBoolean("PowerFactor", false)) {
            driveSpeedAxis = driveSpeedAxis * Constants.DRIVE_SPEED * 0.75;
            driveTurnAxis = driveTurnAxis * Constants.DRIVE_TURN_SPEED * 0.75;
        } else {
            driveSpeedAxis = driveSpeedAxis * Constants.DRIVE_SPEED;
            driveTurnAxis = driveTurnAxis * Constants.DRIVE_TURN_SPEED;
        }
  
        switch (driveMode) {
            case DRIVE_STANDARD:
           // robotDrive.arcadeDrive(-driveSpeedAxis, driveTurnAxis);
                switch (driveState) {
                    case DRIVE_HATCH_SIDE:
                        robotDrive.arcadeDrive(driveSpeedAxis, -driveTurnAxis);
                        break;

                    case DRIVE_CARGO_SIDE:
                        robotDrive.arcadeDrive(-driveSpeedAxis, -driveTurnAxis);
                        break;
                }
                break;

            case DRIVE_TO_STAIRS:
            if(driveSpeedAxis > Constants.DRIVE_DEADZONE || driveTurnAxis > Constants.DRIVE_DEADZONE) {
                driveMode = DriveModes.DRIVE_STANDARD;
            }
            if(leftHatchUltrasonic <= Constants.STOP_DISTANCE && rightHatchUltrasonic <= Constants.STOP_DISTANCE) {
                robotDrive.arcadeDrive(0,0);

            } else if (leftHatchUltrasonic <= Constants.SLOW_DISTANCE || rightHatchUltrasonic <= Constants.SLOW_DISTANCE) {
                if(leftHatchUltrasonic - rightHatchUltrasonic > Constants.DETECTING_DEAD_ZONE) {
                    frontLeftMotor.set(Constants.DETECTING_SLOW_SPEED);
                    frontRightMotor.set((leftHatchUltrasonic / rightHatchUltrasonic) * 1.5 * -Constants.DETECTING_SLOW_SPEED);
                
                } else if (rightHatchUltrasonic - leftHatchUltrasonic > Constants.DETECTING_DEAD_ZONE) {
                    frontRightMotor.set(-Constants.DETECTING_SLOW_SPEED);
                    frontLeftMotor.set((rightHatchUltrasonic / leftHatchUltrasonic) * 1.5 * Constants.DETECTING_SLOW_SPEED);
                
                } else {
                    frontRightMotor.set(-Constants.DETECTING_SLOW_SPEED);
                    frontLeftMotor.set(Constants.DETECTING_SLOW_SPEED);
                }

            } else {
                if(leftHatchUltrasonic - rightHatchUltrasonic > Constants.DETECTING_DEAD_ZONE) {
                    frontLeftMotor.set(Constants.DETECTING_DRIVE_SPEED);
                    frontRightMotor.set((leftHatchUltrasonic / rightHatchUltrasonic) * 1.5 * -Constants.DETECTING_DRIVE_SPEED);
                
                } else if (rightHatchUltrasonic - leftHatchUltrasonic > Constants.DETECTING_DEAD_ZONE) {
                    frontRightMotor.set(-Constants.DETECTING_DRIVE_SPEED);
                    frontLeftMotor.set((rightHatchUltrasonic / leftHatchUltrasonic) * 1.5 * Constants.DETECTING_DRIVE_SPEED);
                
                } else {
                    frontRightMotor.set(-Constants.DETECTING_DRIVE_SPEED);
                    frontLeftMotor.set(Constants.DETECTING_DRIVE_SPEED);
                }
            }
            break;
            
            case BACKING_UP:
                if(encoders.getAverageDistanceInches() < Constants.DISTANCE_AT_LIFT) {
                    robotDrive.curvatureDrive(1, 0, false);
                } else if (encoders.getAverageDistanceInches() > Constants.DISTANCE_AT_LIFT + 1) {
                    robotDrive.curvatureDrive(-1, 0, false);
                } else {
                    robotDrive.curvatureDrive(0, 0, false);
                    driveMode = DriveModes.DRIVE_STANDARD;
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
    // public void setMode(boolean switchMode) {
    //     if(switchMode) {
    //         driveMode = DriveModes.DRIVE_STANDARD;
    //     } else {
    //         driveMode = DriveModes.DRIVE_DETECT;
    //     }
    // }

    public void initialSide() {
        if(SmartDashboard.getBoolean("StartCargoSide", true)) {
            driveState = DriveStates.DRIVE_CARGO_SIDE;
        } else {
            driveState = DriveStates.DRIVE_HATCH_SIDE;
        }
    }

    public void driveToStairs() {
        driveMode = DriveModes.DRIVE_TO_STAIRS;
    }

    public void backUpFromStairs() {
        driveMode = DriveModes.BACKING_UP;
    }

    public void showDashboard() {
        SmartDashboard.putString("Side Facing", driveState.toString());
        SmartDashboard.putString("Drive Mode", driveMode.toString());
        SmartDashboard.putNumber("RoboRIO Voltage", getBatteryVoltage());
        //SmartDashboard.putNumber("Left Encoder", -leftSensors.getQuadraturePosition());
        //SmartDashboard.putNumber("Right Encoder", rightSensors.getQuadraturePosition());

        ///Front Left Motor
        //SmartDashboard.putNumber("FLVoltage", getFrontLeftMotorVoltage());
        //SmartDashboard.putNumber("FLCurrent", getFrontLeftMotorCurrent());
        //Front Right Motor
        //SmartDashboard.putNumber("FRVoltage", getFrontRightMotorVoltage());
        //SmartDashboard.putNumber("FRCurrent", getFrontRightMotorCurrent());

        //SmartDashboard.putNumber("MLCurrent", getMidLeftMotorCurrent());
        
        //SmartDashboard.putNumber("MRCurrent", getMidRightMotorCurrent());
        // //Back Left Motor
        // SmartDashboard.putNumber("BLVoltage", getBackLeftMotorVoltage());
        //SmartDashboard.putNumber("BLCurrent", getBackLeftMotorCurrent());

        // //Back Right Motor
        // SmartDashboard.putNumber("BRVoltage", getBackRightMotorVoltage());
        //SmartDashboard.putNumber("BRCurrent", getBackRightMotorCurrent());
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

    public SensorCollection getLeftSensors() {
        return leftSensors;
    }

    public SensorCollection getRightSensors() {
        return rightSensors;
    }
}
