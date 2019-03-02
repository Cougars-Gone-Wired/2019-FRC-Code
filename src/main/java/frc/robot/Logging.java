package frc.robot;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Logging extends Object{
    
    private boolean loggingStart = false;
    private boolean loggingSave = false;

    private int place;
    private int runs = 0;
    private int logLength;
    private String d = ", " ;

    private File file;
    private FileHandler fh;
    private Logger logger;
    private Level level;
    private String timeStamp;
    private String fileTimeStamp;
    private String[] logArray;

    private StringBuilder logValues = new StringBuilder();

    //private Robot xrobot;
    private Drive drive;
    private Controllers controllers;
    private HatchArm hatchArm;
    private CargoManip cargoManip;
    private Lift lift;
    //private Ultrasonic leftHatchUltrasonic;
    //private Ultrasonic rightHatchUltrasonic;
    //private Ultrasonic hatchUltrasonic;    

    Logging(Robot robot) {
        //xrobot = robot;
        drive = robot.getDrive();
        controllers = robot.getControllers();
        hatchArm = robot.getHatchArm();
        cargoManip = robot.getCargoManip();
        lift = robot.getLift();
        //leftHatchUltrasonic = robot.getLeftHatchUltrasonic();
        //rightHatchUltrasonic = robot.getRightHatchUltrasonic();
        //hatchUltrasonic = robot.getHatchUltrasonic();        
    }

    public void activeInitialize() {
        logger = Logger.getLogger(Logging.class.getName());
        logLength = 1000;
        logArray = new String[logLength];
        fileTimeStamp = new SimpleDateFormat("MMM dd YYYY_HH.mm.ss").format(Calendar.getInstance().getTime());

        logger = Logger.getLogger(Logging.class.getName());
        logger.setUseParentHandlers(false);
    }

    public void disabledInitialize() {
        loggingSave = SmartDashboard.getBoolean("Save Logger", false);
        if(loggingStart) {
            loggingStart = false;
        }
        if(loggingSave) {
            try {
                makeFile();
                timeStamp = new SimpleDateFormat("MMM dd YYYY_HH.mm.ss").format(Calendar.getInstance().getTime());
                logger.fine(timeStamp + ", Logging Stopped");
                //logger.fine(timeStamp + "Logging Stopped");
                logger.fine("+++Reload universe and reboot+++");
                fh.close();
            } catch (Exception e) {
                System.out.println("Failed to Save my Blood Sweat and Tears");
            }
        }
    }

    public void makeFile() throws IOException {
        file = new File("/home/lvuser/" + "log-" + fileTimeStamp + ".csv");
        file.createNewFile();

        fh = new FileHandler(file.getAbsolutePath());
        fh.setFormatter(new LoggingFormatter());

        logger.addHandler(fh);
        level = Level.FINE;
        logger.setLevel(level);

        System.out.println("File Created at" + file.getAbsolutePath());

        int i = 0;
        while(i < logLength && logArray[i] != null) {
            logger.fine(logArray[i]);
            i++;
        }
    }

    public void collectData() {
        loggingSave = SmartDashboard.getBoolean("Save Logger", false);
        if(!loggingStart) {
            loggingStart = true;
            logValues = new StringBuilder();
            timeStamp = new SimpleDateFormat("MMM dd YYYY_HH.mm.ss.SSS").format(Calendar.getInstance().getTime());
            
            logValues.append(timeStamp);
            
            //Battery Voltage
            logValues.append(d).append("BatteryVoltage");

            //Joystick Values
            logValues.append(d).append("SpeedAxis");
            logValues.append(d).append("TurnAxis");
            logValues.append(d).append("DriveToggleValue");
            logValues.append(d).append("UltrasonicToggleValue");

            //Left Motors
            logValues.append(d).append("FrontLeftMotorVoltage");
            logValues.append(d).append("FrontLeftMotorCurrent");

            logValues.append(d).append("MidLeftMotorVoltage");
            logValues.append(d).append("MidLeftMotorCurrent");

            logValues.append(d).append("BackLeftMotorVoltage");
            logValues.append(d).append("BackLeftMotorCurrent");
            
            //Right Motors
            logValues.append(d).append("FrontRightMotorVoltage");
            logValues.append(d).append("FrontRightMotorCurrent");

            logValues.append(d).append("MidRightMotorVoltage");
            logValues.append(d).append("MidRightMotorCurrent");

            logValues.append(d).append("BackRightMotorVoltage");
            logValues.append(d).append("BackRightMotorCurrent");

            logValues.append(d).append("BackRightMotorVoltage");
            logValues.append(d).append("BackRightMotorCurrent");

            //Ultrasonic 
            // logValues.append(d).append("LeftHatchUltrasonic");
            // logValues.append(d).append("RightHatchUltrasonic");
            //logValues.append(d).append("HatchUltrasonicDistance");

            //HatchArm
            logValues.append(d).append("HatchArmMoveMotorVoltage");
            logValues.append(d).append("HatchArmMoveMotorCurrent");

            logValues.append(d).append("HatchArmGrabMotorVoltage");
            logValues.append(d).append("HatchArmGrabMotorCurrent");

            //CargoManip
            logValues.append(d).append("CargoManipIntakeMotorVoltage");
            logValues.append(d).append("CargoManipIntakeMotorCurrent");

            logValues.append(d).append("CargoManipArmMotorVoltage");
            logValues.append(d).append("CargoManipArmMotorVoltage");

            //Lift
            logValues.append(d).append("FrontLiftMotorVoltage");
            logValues.append(d).append("FrontLiftMotorCurrent");

            logValues.append(d).append("BackLiftMotorVoltage");
            logValues.append(d).append("BackLiftMotorCurrent");

            logArray[0] = logValues.toString();
            place = 1;
        }

        runs++;
        if(runs >= 5) {
            runs = 0;
            logValues = new StringBuilder();
            timeStamp = new SimpleDateFormat("MMM dd YYYY_HH.mm.ss.SSS").format(Calendar.getInstance().getTime());
            
            logValues.append(timeStamp);
            
            //Battery Voltage
            logValues.append(d).append(drive.getBatteryVoltage());

            //Joystick Values
            logValues.append(d).append(controllers.getDriveSpeedAxis());
            logValues.append(d).append(controllers.getDriveTurnAxis());
            logValues.append(d).append(controllers.getDriveToggleValue());
            //logValues.append(d).append(controllers.getUltrasonicToggleValue());

            //Left
            logValues.append(d).append(drive.getFrontLeftMotorVoltage());
            logValues.append(d).append(drive.getFrontLeftMotorCurrent());

            logValues.append(d).append(drive.getMidLeftMotorVoltage());
            logValues.append(d).append(drive.getMidLeftMotorCurrent());

            logValues.append(d).append(drive.getBackLeftMotorVoltage());
            logValues.append(d).append(drive.getBackLeftMotorCurrent());

            //Right
            logValues.append(d).append(drive.getFrontRightMotorVoltage());
            logValues.append(d).append(drive.getFrontRightMotorCurrent());

            logValues.append(d).append(drive.getMidRightMotorVoltage());
            logValues.append(d).append(drive.getMidRightMotorCurrent());

            logValues.append(d).append(drive.getBackRightMotorVoltage());
            logValues.append(d).append(drive.getBackLeftMotorCurrent());

            //Ultrasonic

            // logValues.append(d).append(xrobot.getLeftHatchUltrasonicImperialValue());
            // logValues.append(d).append(xrobot.getRightHatchUltrasonicImperialValue());

            // logValues.append(d).append(SmartDashboard.getNumber("Left Ultrasonic Imperial Value", 0));
            // logValues.append(d).append(SmartDashboard.getNumber("Right Ultrasonic Imperial Value", 0));

            // logValues.append(d).append(leftHatchUltrasonic.getImperialUltrasonicValue());
            // logValues.append(d).append(rightHatchUltrasonic.getImperialUltrasonicValue());
            //logValues.append(d).append(hatchUltrasonic.getImperialUltrasonicValue());
            
            //HatchArm
            logValues.append(d).append(hatchArm.getHatchArmMoveMotorVoltage());
            logValues.append(d).append(hatchArm.getHatchArmMoveMotorCurrent());

            //CargoManip
            logValues.append(d).append(cargoManip.getIntakeMotorVoltage());
            logValues.append(d).append(cargoManip.getIntakeMotorCurrent());

            logValues.append(d).append(cargoManip.getArmMotorVoltage());
            logValues.append(d).append(cargoManip.getArmMotorCurrent());
            
            //Lift
            logValues.append(d).append(lift.getFrontLiftMotorVoltage());
            logValues.append(d).append(lift.getFrontLiftMotorCurrent());

            logValues.append(d).append(lift.getBackLiftMotorVoltage());
            logValues.append(d).append(lift.getBackLiftMotorCurrent());
            
            if(place < logLength) {
                logArray[place] = logValues.toString();
                place++;
            }
        } 
    }
}
