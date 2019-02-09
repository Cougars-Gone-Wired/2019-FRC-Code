/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  private Controllers controller;
  private CargoManip cargoManip;
  
  private Cameras cameras;
	private Controllers controllers;
  private Drive drive;
  private HatchArm hatchArm;
  private Logging logging;
  private Ultrasonic hatchUltrasonic;
  
  //Lift code
  private Lift lift;


  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */

  @Override
  public void robotInit() {
    controller = new Controllers();
   cargoManip = new CargoManip();

	  controllers = new Controllers();
    drive = new Drive();
    hatchArm = new HatchArm();
    logging = new Logging(this);  
    cameras = new Cameras();
    hatchUltrasonic = new Ultrasonic(Constants.HATCH_ULTRASONIC_SENSOR_PORT);
    lift = new Lift();
    logging.activeInitialize();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */

  @Override
  public void robotPeriodic() {

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */

  @Override
  public void autonomousInit() {
    // new Thread(logging.toString()).start();
    cargoManip.initialize();
    logging.activeInitialize();

    drive.initalize();

    controllers.initialize();
    controllers.setDriveToggle();

    hatchArm.initialize();
  }

  /**
   * This function is called periodically during autonomous.
   */

  @Override
  public void autonomousPeriodic() {
  //drive.setSide(controllers.getDriveToggleValue());
  
  // Thread thread = new Thread(logging);
  // thread.run();
  // thread.start();
  }

  /**
   * This function is called when teleop is initialzied.
   */

  @Override
  public void teleopInit() {
    cargoManip.initialize();

    drive.initalize();
    controllers.initialize();
    lift.initialize();
    logging.activeInitialize();
    // Thread thread = new Thread(logging);
    // thread.start();
    // thread.run();
  }

  /**
   * This function is called periodically during operator control.
   */

  @Override
  public void teleopPeriodic() {
    controller.setControllerValues();
    cargoManip.armMove(controller.getTop(), controller.getBottom(), controller.getCargoShip(), controller.getRocket());
    cargoManip.intakeMove(controller.getIntake(), controller.getOuttake());
    cargoManip.sensorLight();
    
    controllers.setControllerValues();
    hatchUltrasonic.setUltrasonicValue();
    setSide(controllers.getDriveToggleValue());
    drive.setMode(controllers.getUltrasonicToggleValue());
    drive.robotDrive(controllers.getDriveSpeedAxis(), controllers.getDriveTurnAxis(), hatchUltrasonic.getImperialUltrasonicValue());
    drive.showDashboard();
    cameras.cameraVideo();
    logging.collectData();
    SmartDashboard.putNumber("Raw Ultrasonic", hatchUltrasonic.getRawUltrasonicValue());
    SmartDashboard.putNumber("Imperial Ultrasonic", hatchUltrasonic.getImperialUltrasonicValue());
    hatchArm.hatchArmGrab(controllers.getHatchArmGrabButton());
    hatchArm.hatchArmMove(controllers.getHatchArmSchemeButton(), controllers.getHatchArmInsideButton(), controllers.getHatchArmVertButton(), controllers.getHatchArmFloorButton(), controllers.getLowerHatchArmButton(), controllers. getRaiseHatchArmButton());
    lift.lift(controllers.getLiftToggleDeployer());
  }

  /**
   * This function is called periodically during test mode.
   */

  @Override
  public void testPeriodic() {
    drive.initialSide();
  }

  @Override
  public void disabledInit() {
    drive.initalize();
    logging.disabledInitialize();
  }

  @Override
  public void disabledPeriodic() {
    setSide(SmartDashboard.getBoolean("StartCargoSide", true));
    controllers.setDriveToggle();
    drive.showDashboard();
  }

  public void setSide(boolean sideToggle) {
    drive.setSide(sideToggle);
    cameras.setSide(sideToggle);
    Sides.setSide(sideToggle);
  }

  public Drive getDrive() {
    return drive;
  }

  public Ultrasonic getHatchUltrasonic() {
    return hatchUltrasonic;
  }
}



// private string rygar = "dumb"
// print('Is Rygar dumb? (Y for Yes and N for No)')
// if rygar == 'Y' or rygar = 'Y'
//     print(Totally, he is very dumb, to an extent the world has never seen the likes of)
// else:
//     print(You are wrong, rygar is )

//Josh L: What in the life of Pete is this?!? Why are we insulting each other? Or is there an inside joke I'm not in on?