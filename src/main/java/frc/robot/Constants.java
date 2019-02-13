package frc.robot;

public class Constants {

	//Manipulator
	static final int MANIPULATOR_CONTROLLER_PORT = 1;
	static final int MID_SWITCH_PORT = 0;
	//static final int CARGO_SHIP_SWITCH_PORT = 1; //
	//static final int ROCKET_SWITCH_PORT = 2; //

	//static final int CARGO_ARM_AXIS = 1; //
	static final int HATCH_ARM_AXIS = 1;
	static final int HATCH_ARM_GRAB_BUTTON = 6;
    static final int HATCH_ARM_SCHEME_BUTTON = 8;
	static final int CARGO_INTAKE_AXIS = 2; //
    static final int CARGO_OUTTAKE_AXIS = 3; //
    static final int CARGO_ARM_TOP_BUTTON = 4; //
    static final int CARGO_ARM_BOTTOM_BUTTON = 1; //
    static final int CARGO_ARM_CARGO_SHIP_BUTTON = 3; //
    static final int CARGO_ARM_ROCKET_BUTTON = 2; //

    static final int HATCH_ARM_MOVE_MOTOR_ID = 8;
	static final int HATCH_ARM_GRAB_MOTOR_ID = 9;
	static final int CARGO_ARM_MOTOR_ID = 10; //
    static final int CARGO_INTAKE_MOTOR_ID = 11; //

    static final double HATCH_ARM_MOVE_SPEED = 0.05;
	static final double HATCH_ARM_GRAB_SPEED = 0.2;
	static final double CARGO_ARM_MOTOR_SPEED = 0.05; //
	static final double CARGO_ARM_INTAKE_SPEED = 0.2; //

	static final double HATCH_ARM_MOVE_AXIS_THRESHHOLD = 0.8;
	static final double HATCH_ARM_GRAB_AXIS_THRESHHOLD = 0.8;
	static final int CARGO_SHIP_ENCODER_VALUE_GOAL = 40; //
    static final int ROCKET_ENCODER_VALUE_GOAL = 50; //


	//Mobility
	static final int MOBILITY_CONTROLLER_PORT = 0;

	static final int DRIVE_SPEED_AXIS = 1;
	static final int DRIVE_TURN_AXIS = 4;

	static final int LIFT_TOGGLE_BUTTON = 3;
	static final int DRIVE_TOGGLE_BUTTON = 1;
	static final int ULTRASONIC_TOGGLE_BUTTON = 4;

	static final int FRONT_LIFT_MOTOR_ID = 0;
	static final int BACK_LIFT_MOTOR_ID = 1;

	static final int FRONT_LEFT_MOTOR_ID = 7; //Based off Hatch Side
	static final int MID_LEFT_MOTOR_ID = 3;
	static final int BACK_LEFT_MOTOR_ID = 6;

	static final int FRONT_RIGHT_MOTOR_ID = 5;
	static final int MID_RIGHT_MOTOR_ID = 2;
	static final int BACK_RIGHT_MOTOR_ID = 4;

	static final double LIFT_SPEED = 1; //Make -1 to reverse direction, use values 1, -1, and in between. (Probably not 0.)
	static final int ULTRASONIC_HATCH_LEFT_PORT = 1; //SET PORTS!!!
	static final int ULTRASONIC_HATCH_RIGHT_PORT = 0; //SET PORTS!!!
	static final double DISTANCE_AT_LIFT = 12; //Inches
	static final double ULTRASONIC_THRESHOLD = 11; //Inches

	static final double DRIVE_SPEED = 0.95;
	static final double DRIVE_TURN_SPEED = 0.75;
	static final double DRIVE_DEADZONE = 0.15;

	static final double DETECTING_DRIVE_SPEED = .4;
	static final double DETECTING_SLOW_SPEED = .2;
	static final double DETECTING_DEAD_ZONE = 1.5;
	static final double SLOW_DISTANCE = 30;
	static final double STOP_DISTANCE = 12;

	static final int LEFT_HATCH_ULTRASONIC_SENSOR_PORT = 1;
	static final int RIGHT_HATCH_ULTRASONIC_SENSOR_PORT = 0;
	static final double ULTRASONIC_IMPERIAL_CONVERSION_RATIO = 21.3;

	static final int ULTRASONIC_LOG_ELEMENT_COUNT = 6;

}