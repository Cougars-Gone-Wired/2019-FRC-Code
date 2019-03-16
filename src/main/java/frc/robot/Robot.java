package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private Cameras cameras;

	private Controllers controllers;
  private CargoManip cargoManip;
  private Drive drive;
  private HatchArm hatchArm;

  //Lift code
  private Lift lift;

  private Dashboard dashboard;
  private Logging logging;

  @Override
  public void robotInit() {
    //cameras = new Cameras();

    controllers = new Controllers();
    cargoManip = new CargoManip();
    drive = new Drive();
    hatchArm = new HatchArm();

    lift = new Lift();

    dashboard = new Dashboard(this);
    logging = new Logging(this);  
    logging.activeInitialize();

    cameras = new Cameras();
    new Thread(cameras).start();;
  }

  @Override
  public void robotPeriodic() {
    //drive.refreshDashboard();
    dashboard.showDisabledValues();

    if (controllers.getCameraStopButton().toggle()) {
      cameras.stop();
      controllers.getCameraStopButton().setOutput(false);
    }
  }

  @Override
  public void autonomousInit() {
    controllers.initialize();
    
    controllers.setDriveToggle();
    cargoManip.initialize();
    drive.initalize();
    hatchArm.initialize();
 
    logging.activeInitialize();
  }

  @Override
  public void autonomousPeriodic() {
    controllers.setControllerValues();
    setSide(controllers.getDriveToggleValue());

    //cargoManip.armMove(controllers.getCargoArmAxis(), controllers.getCargoArmTopButton(), controllers.getCargoArmRocketButton(), controllers.getCargoArmCargoShipButton(), controllers.getCargoArmFloorButton());
    cargoManip.armMoveManual(controllers.getCargoArmAxis());
    cargoManip.intakeMove(controllers.getCargoArmIntakeAxis(), controllers.getCargoArmOuttakeAxis());
    //cargoManip.sensorLight();

    //drive.setSide(controllers.getDriveToggleValue());
    drive.setFine(controllers.getDriveFineToggleValue());
    drive.setTrack(controllers.isTrackButton());
    drive.robotDrive(controllers.getDriveSpeedAxis(), controllers.getDriveTurnAxis());

    hatchArm.hatchArmManualMove(controllers.getHatchArmAxis());
    // hatchArm.hatchArmMove(controllers.getLowerHatchArmButton(), controllers.getRaiseHatchArmButton());
    
    dashboard.showEnabledValues();

    //The Loggers chopping down trees
    logging.collectData();
  }

  @Override
  public void teleopInit() {
    controllers.initialize();

    cargoManip.initialize();
    drive.initalize();
    hatchArm.initialize();

    lift.initialize();

    logging.activeInitialize();
  }

  @Override
  public void teleopPeriodic() {

    controllers.setControllerValues();
    setSide(controllers.getDriveToggleValue());

    //cargoManip.armMove(controllers.getCargoArmAxis(), controllers.getCargoArmTopButton(), controllers.getCargoArmRocketButton(), controllers.getCargoArmCargoShipButton(), controllers.getCargoArmFloorButton());
    cargoManip.armMoveManual(controllers.getCargoArmAxis());
    cargoManip.intakeMove(controllers.getCargoArmIntakeAxis(), controllers.getCargoArmOuttakeAxis());
    //cargoManip.sensorLight();

    drive.setFine(controllers.getDriveFineToggleValue());
    drive.setTrack(controllers.isTrackButton());
    drive.robotDrive(controllers.getDriveSpeedAxis(), controllers.getDriveTurnAxis());

    hatchArm.hatchArmManualMove(controllers.getHatchArmAxis());
    // hatchArm.hatchArmMove(controllers.getLowerHatchArmButton(), controllers.getRaiseHatchArmButton());
    
    
    //Lift
    // lift.lift(controllers.isLiftDeployButton(), controllers.isLiftRetractButton(), controllers.isLiftStopButton(), controllers.isLiftWithdrawFromStairButton(), drive, controllers.getMobilityStick());
    lift.lift2(controllers.isLiftDeployButton(), controllers.isLiftRetractButton());
    
    dashboard.showEnabledValues();

    //The Loggers chopping down trees
    logging.collectData();
  }

  @Override
  public void testPeriodic() {
    controllers.setControllerValues();
    hatchArm.hatchArmManualMove(controllers.getHatchArmAxis());
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
    dashboard.showDisabledValues();
    setSide(SmartDashboard.getBoolean("StartCargoSide", false));
    controllers.setDriveToggle();
    drive.initialSide();
  }


  public void setSide(boolean sideToggle) {
    drive.setSide(sideToggle);
    Sides.setSide(sideToggle);
  }

  public Drive getDrive() {
    return drive;
  }

  public Controllers getControllers() {
    return controllers;
  }

  public HatchArm getHatchArm() {
    return hatchArm;
  }

  public CargoManip getCargoManip() {
    return cargoManip;
  }

  public Lift getLift() {
    return lift;
  }

  public Logging getLogging() {
    return logging;
  }
}