package frc.robot;

public class Constants {


	//Controllers
	static final int MOBILITY_CONTROLLER_PORT = 0;
	static final int MANIPULATOR_CONTROLLER_PORT = 1;

	//Switches
	static final int HATCH_MID_SWITCH_PORT = 4;
	static final int ROCKET_LIMIT_SWITCH_ID = 5;
	static final int CARGO_SHIP_LIMIT_SWITCH_ID = 6;
	static final int LIFT_SWITCH_PORT = 0;

	//Axes(plural of axis, not axe)
	//Manipulator
	static final int HATCH_ARM_AXIS = 1;
	static final int CARGO_INTAKE_AXIS = 2;
	static final int CARGO_OUTTAKE_AXIS = 3;
	static final int CARGO_ARM_AXIS = 5;
	//Mobility
	static final int DRIVE_SPEED_AXIS = 1;
	static final int TRACK_BUTTON = 2;
	static final int DRIVE_TURN_AXIS = 4;
	static final int CAMERA_STOP_BUTTON = 8;
	
	//Buttons
	//Manipulator
	static final int CARGO_ARM_FLOOR_BUTTON = 1;
	static final int CARGO_ARM_ROCKET_BUTTON = 2;
	static final int CARGO_ARM_CARGO_SHIP_BUTTON = 3;
	static final int CARGO_ARM_TOP_BUTTON = 4;
	//Mobility
	static final int DRIVE_TOGGLE_BUTTON = 1;
	static final int LIFT_STOP_BUTTON = 2;
	static final int DRIVE_FINE_BUTTON = 3;
	static final int LIFT_WITHDRAW_FROM_STAIR_BUTTON = 4;
	static final int LIFT_DEPLOY_BUTTON = 5; // Based off of the mobility joystick
	static final int LIFT_RETRACT_BUTTON = 6;

	//Motor Ports
	static final int FRONT_LIFT_MOTOR_ID = 0; //Right Lift Motor
	static final int BACK_LIFT_MOTOR_ID = 1; //Left Lift Motor
	static final int MID_RIGHT_MOTOR_ID = 2; //Based off Hatch Side
	static final int MID_LEFT_MOTOR_ID = 3;
	static final int BACK_RIGHT_MOTOR_ID = 4;
	static final int FRONT_RIGHT_MOTOR_ID = 5;
	static final int BACK_LEFT_MOTOR_ID = 6;
	static final int FRONT_LEFT_MOTOR_ID = 7;
    static final int HATCH_ARM_MOVE_MOTOR_ID = 8;
	static final int CARGO_ARM_MOTOR_ID = 10;
	static final int CARGO_INTAKE_MOTOR_ID = 11;
	
	//Constants
	//Speeds
	static final double DRIVE_SPEED = 0.95;
	static final double DRIVE_TURN_SPEED = 0.9;
	static final double DRIVE_FINE_SPEED = 0.75;
	static final double LIFT_SPEED = 0.75; //Make negative to reverse direction, use any value from 1.0 to -1.0. (Probably not 0.0.)
    static final double HATCH_ARM_MOVE_SPEED = 0.5;
	static final double CARGO_ARM_MOVE_SPEED = 0.95;
	static final double CARGO_ARM_MANUAL_MOVE_SPEED = 0.975;
	static final double CARGO_ARM_INTAKE_SPEED = .7;
	static final double CARGO_ARM_OUTTAKE_SPEED = 1.0;
	//Deadzones
	static final double DRIVE_DEADZONE = 0.15;
	static final double HATCH_ARM_MOVE_AXIS_DEADZONE = 0.15;
	static final double CARGO_ARM_MOVE_AXIS_DEADZONE = 0.15;
	static final double CARGO_INTAKE_AXIS_DEADZONE = 0.15;
	static final double CARGO_OUTTAKE_AXIS_DEADZONE = 0.15;
	//Miscellanious
	static final double RAMP_TIME = 0.0;
	static final double DISTANCE_AT_LIFT = 11.0; //Inches

	//Commented Out
	// static final int CARGO_SHIP_ENCODER_VALUE_GOAL = 60; //In degrees
	// static final int ROCKET_ENCODER_VALUE_GOAL = 40; //In degrees
	// static final double DETECTING_DRIVE_SPEED = 0.4;
	// static final double DETECTING_SLOW_SPEED = 0.2;
	// static final double DETECTING_DEAD_ZONE = 1.5;
	// static final double SLOW_DISTANCE = 30.0;
	// static final double STOP_DISTANCE = 12.0;


}