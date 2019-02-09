package frc.robot;

public class Constants {

	//Manipulator
	static final int MANIPULATOR_CONTROLLER_PORT = 1;
	static final int MID_SWITCH_PORT = 0;
	//static final int CARGO_SHIP_SWITCH_PORT = 1; //
	//static final int ROCKET_SWITCH_PORT = 2; //
	static final int CARGO_ARM_ENCODER_PORT_ONE = 0; //
    static final int CARGO_ARM_ENCODER_PORT_TWO = 1; //

	static final int CARGO_ARM_AXIS = 1; //
    static final int HATCH_ARM_AXIS = 1;
    static final int HATCH_ARM_SCHEME_BUTTON = 6;
	static final int HATCH_ARM_GRAB_BUTTON = 3;
	static final int CARGO_ARM_INTAKE_BUTTON = 5; //
    static final int CARGO_ARM_OUTTAKE_BUTTON = 6; //
    static final int CARGO_ARM_TOP_BUTTON = 4; //
    static final int CARGO_ARM_BOTTOM_BUTTON = 1; //
    static final int CARGO_ARM_CARGO_SHIP_BUTTON = 3; //
    static final int CARGO_ARM_ROCKET_BUTTON = 2; //

    static final int HATCH_ARM_MOVE_MOTOR_ID = 8;
	static final int HATCH_ARM_GRAB_MOTOR_ID = 9;
	static final int CARGO_ARM_MOTOR_ID = 10; //
    static final int CARGO_ARM_INTAKE_MOTOR_ID = 11; //

    static final double HATCH_ARM_MOVE_SPEED = 0.05;
	static final double HATCH_ARM_GRAB_SPEED = 0.2;
	static final double CARGO_ARM_MOTOR_SPEED = 0.05; //
	static final double CARGO_ARM_INTAKE_SPEED = 0.2; //

	static final double HATCH_ARM_MOVE_AXIS_THRESHHOLD = 0.8;
	static final int CARGO_SHIP_GOAL_ENCODER_VALUE = 40; //
    static final int ROCKET_GOAL_ENCODER_VALUE = 50; //


	//Mobility
	static final int MOBILITY_CONTROLLER_PORT = 0;

	static final int DRIVE_SPEED_AXIS = 1;
	static final int DRIVE_TURN_AXIS = 4;

	static final int LIFT_TOGGLE_BUTTON = 3;
	static final int DRIVE_TOGGLE_BUTTON = 1;
	static final int ULTRASONIC_TOGGLE_BUTTON = 4;

	static final int FRONT_LIFT_MOTOR_ID = 0;
	static final int BACK_LIFT_MOTOR_ID = 1;

	static final int FRONT_LEFT_MOTOR_ID = 4;
	static final int MID_LEFT_MOTOR_ID = 2;
	static final int BACK_LEFT_MOTOR_ID = 5;

	static final int FRONT_RIGHT_MOTOR_ID = 6;
	static final int MID_RIGHT_MOTOR_ID = 3;
	static final int BACK_RIGHT_MOTOR_ID = 7;

	static final double LIFT_SPEED = 1; //Make -1 to reverse direction
	static final double DRIVE_SPEED = 0.95;
	static final double DRIVE_TURN_SPEED = 0.75;
	static final double DRIVE_DEADZONE = 0.15;

	static final int HATCH_ULTRASONIC_SENSOR_PORT = 1;
	static final double ULTRASONIC_IMPERIAL_CONVERSION_RATIO = 21.3;

}