package frc.robot;

public class Constants {

	//Manipulator
	static final int MANIPULATOR_CONTROLLER_PORT = 1;
    static final int HATCH_ARM_AXIS = 1;
    static final int HATCH_ARM_SCHEME_BUTTON = 6;
    static final int HATCH_ARM_GRAB_BUTTON = 3;
    static final int MID_SWITCH_ID = 0;

    static final int HATCH_ARM_MOVE_MOTOR_ID = 8;
    static final int HATCH_ARM_GRAB_MOTOR_ID = 9;

    static final double HATCH_ARM_MOVE_SPEED = 0.05;
    static final double HATCH_ARM_GRAB_SPEED = 0.2;

	static final double HATCH_ARM_MOVE_AXIS_THRESHHOLD = 0.8;


	//Mobility
	static final int MOBILITY_CONTROLLER_PORT = 0;

	static final int DRIVE_SPEED_AXIS = 1;
	static final int DRIVE_TURN_AXIS = 4;

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

//Lift
    //Other
    
    //Buttons
    static final int LIFT_TOGGLER_BUTTON = 3;
	

	//Gavin's
	static final int ARM_AXIS = 1;
    static final int INTAKE_BUTTON = 5;
    static final int OUTTAKE_BUTTON = 6;
    static final int TOP_BUTTON = 4;
    static final int BOTTOM_BUTTON = 1;
    static final int CARGO_SHIP_BUTTON = 3;
    static final int ROCKET_BUTTON = 2;
    static final int MANIPULATOR_STICK_PORT = 0;
    static final int ARM_MOTOR_ID = 4;
    static final int INTAKE_MOTOR_ID = 1;
    //static final int CARGO_SHIP_LIMIT_SWITCH_ID = 0;
    //static final int ROCKET_LIMIT_SWITCH_ID = 1;
    static final int ARM_ENCODER_PORT_ONE = 0;
    static final int ARM_ENCODER_PORT_TWO = 1;
    static final boolean INVERT_ENCODER_DIRECTION = false;
    static final int ENCODER_CARGO_SHIP_GOAL = 40;
    static final int ENCODER_ROCKET_GOAL = 50;
    static final int ARM_MOTOR_SPEED = 1;
}