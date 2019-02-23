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
  private Cameras cameras;

	private Controllers controllers;
  private CargoManip cargoManip;
  private Drive drive;
  private HatchArm hatchArm;

  //Lift code
  private Lift lift;

  // private Ultrasonic leftHatchUltrasonic;
  // private Ultrasonic rightHatchUltrasonic;

  private Logging logging;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */

  @Override
  public void robotInit() {
    cameras = new Cameras();

    controllers = new Controllers();
    cargoManip = new CargoManip();
    drive = new Drive();
    hatchArm = new HatchArm();

    lift = new Lift();

    // leftHatchUltrasonic = new Ultrasonic(Constants.LEFT_HATCH_ULTRASONIC_SENSOR_PORT);
    // rightHatchUltrasonic = new Ultrasonic(Constants.RIGHT_HATCH_ULTRASONIC_SENSOR_PORT);

    logging = new Logging(this);  
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
    drive.refreshDashboard();
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
    controllers.initialize();
    
    controllers.setDriveToggle();
    cargoManip.initialize();
    drive.initalize();
    hatchArm.initialize();

    // leftHatchUltrasonic.initialize();
    // rightHatchUltrasonic.initialize();
    
    logging.activeInitialize();
  }

  /**
   * This function is called periodically during autonomous.
   */

  @Override
  public void autonomousPeriodic() {
    //cameras.cameraVideo();

    controllers.setControllerValues();
    setSide(controllers.getDriveToggleValue());

    //cargoManip.armMove(controllers.getCargoArmTopButton(), controllers.getCargoArmBottomButton(), controllers.getCargoArmCargoShipButton(), controllers.getCargoArmRocketButton());
    cargoManip.armMoveManual(controllers.getCargoArmAxis());
    cargoManip.intakeMove(controllers.getCargoArmIntakeAxis(), controllers.getCargoArmOuttakeAxis());
    //cargoManip.sensorLight();

    //drive.setSide(controllers.getDriveToggleValue());
    //drive.setMode(controllers.getUltrasonicToggleValue());
    drive.setFine(controllers.getDriveFineToggleValue());
    drive.robotDrive(controllers.getDriveSpeedAxis(), controllers.getDriveTurnAxis());
    // drive.robotDrive(controllers.getDriveSpeedAxis(), controllers.getDriveTurnAxis(), leftHatchUltrasonic.getImperialUltrasonicValue(), rightHatchUltrasonic.getImperialUltrasonicValue());
    drive.showDashboard();

    hatchArm.hatchArmGrab(controllers.getHatchArmGrabButton());
    hatchArm.hatchArmManualMove(controllers.getLowerHatchArmButton(), controllers. getRaiseHatchArmButton());
    // hatchArm.hatchArmMove(controllers.getHatchArmSchemeButton(), controllers.getHatchArmInsideButton(), controllers.getHatchArmVertButton(), controllers.getHatchArmFloorButton(), controllers.getLowerHatchArmButton(), controllers. getRaiseHatchArmButton());
    
    // leftHatchUltrasonic.setUltrasonicValues();
    // rightHatchUltrasonic.setUltrasonicValues();
    //leftHatchUltrasonic.displayValues("Left Ultrasonic");
    //rightHatchUltrasonic.displayValues("Right Ultrasonic");

    //The Loggers chopping down trees
    logging.collectData();
  }

  /**
   * This function is called when teleop is initialzied.
   */

  @Override
  public void teleopInit() {
    controllers.initialize();

    cargoManip.initialize();
    drive.initalize();
    hatchArm.initialize();

    lift.initialize();

    // leftHatchUltrasonic.initialize();
    // rightHatchUltrasonic.initialize();

    logging.activeInitialize();
  }

  /**
   * This function is called periodically during operator control.
   */

  @Override
  public void teleopPeriodic() {
    //cameras.cameraVideo();

    controllers.setControllerValues();
    setSide(controllers.getDriveToggleValue());

    //cargoManip.armMove(controllers.getCargoArmTopButton(), controllers.getCargoArmBottomButton(), controllers.getCargoArmCargoShipButton(), controllers.getCargoArmRocketButton());
    cargoManip.armMoveManual(controllers.getCargoArmAxis());
    cargoManip.intakeMove(controllers.getCargoArmIntakeAxis(), controllers.getCargoArmOuttakeAxis());
    //cargoManip.sensorLight();

    //drive.setMode(controllers.getUltrasonicToggleValue());
    drive.setFine(controllers.getDriveFineToggleValue());
    drive.robotDrive(controllers.getDriveSpeedAxis(), controllers.getDriveTurnAxis());
    // drive.robotDrive(controllers.getDriveSpeedAxis(), controllers.getDriveTurnAxis(), leftHatchUltrasonic.getImperialUltrasonicValue(), rightHatchUltrasonic.getImperialUltrasonicValue());
    drive.showDashboard();

    //hatchArm.hatchArmGrab(controllers.getHatchArmGrabButton());
    hatchArm.grab2(controllers.isHatchGrabInButton(), controllers.isHatchGrabOutButton());
    hatchArm.hatchArmManualMove(controllers.getLowerHatchArmButton(), controllers. getRaiseHatchArmButton());
    // hatchArm.hatchArmMove(controllers.getHatchArmSchemeButton(), controllers.getHatchArmInsideButton(), controllers.getHatchArmVertButton(), controllers.getHatchArmFloorButton(), controllers.getLowerHatchArmButton(), controllers. getRaiseHatchArmButton());
    
    //Lift
    // lift.lift(controllers.isLiftDeployButton(), controllers.isLiftRetractButton(), controllers.isLiftStopButton(), controllers.isLiftWithdrawFromStairButton(), drive, controllers.getMobilityStick());
    lift.lift2(controllers.isLiftDeployButton(), controllers.isLiftRetractButton());
    
    // leftHatchUltrasonic.setUltrasonicValues();
    // rightHatchUltrasonic.setUltrasonicValues();
    //leftHatchUltrasonic.displayValues("Left Ultrasonic");
    //rightHatchUltrasonic.displayValues("Right Ultrasonic");

    //The Loggers chopping down trees
    logging.collectData();
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
    // SmartDashboard.putBoolean("StartCargoSide", false);
    // SmartDashboard.putBoolean("PowerFactor", false);
    // SmartDashboard.putBoolean("Save Logger", false);
  }

  @Override
  public void disabledPeriodic() {
    //drive.refreshDashboard();
    setSide(SmartDashboard.getBoolean("StartCargoSide", true));
    controllers.setDriveToggle();
    drive.showDashboard();
  }


  public void setSide(boolean sideToggle) {
    drive.setSide(sideToggle);
    //cameras.setSide(sideToggle);
    Sides.setSide(sideToggle);
  }

  public Drive getDrive() {
    return drive;
  }

  public Controllers getControllers() {
    return controllers;
  }

  // public Ultrasonic getLeftHatchUltrasonic() {
  //   return leftHatchUltrasonic;
  // }

  // public Ultrasonic getRightHatchUltrasonic() {
  //   return rightHatchUltrasonic;
  // }

  public HatchArm getHatchArm() {
    return hatchArm;
  }

  public CargoManip getCargoManip() {
    return cargoManip;
  }

  // public double getLeftHatchUltrasonicImperialValue() {
  //   return leftHatchUltrasonic.getImperialUltrasonicValue();
  // }

  // public double getRightHatchUltrasonicImperialValue() {
  //   return rightHatchUltrasonic.getImperialUltrasonicValue();
  // }

  public Lift getLift() {
    return lift;
  }
}



// private string rygar = "dumb"
// print('Is Rygar dumb? (Y for Yes and N for No)')
// if rygar == 'Y' or rygar = 'Y'
//     print(Totally, he is very dumb, to an extent the world has never seen the likes of)
// else:
//     print(You are wrong, rygar is )

//Josh L: What in the life of Pete is this?!? Why are we insulting each other? Or is there an inside joke I'm not in on?