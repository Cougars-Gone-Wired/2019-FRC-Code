package frc.robot;

public class Constants {

//Manipulator
	static final int MANIPULATOR_CONTROLLER_PORT = 1;
    static final int HATCH_ARM_AXIS = 1;
    static final int HATCH_ARM_SCHEME_BUTTON = 6;
    static final int HATCH_ARM_GRAB_BUTTON = 3;
    static final int MID_SWITCH_ID = 0;

    static final int HATCH_ARM_MOVE_MOTOR_ID = 0;
    static final int HATCH_ARM_GRAB_MOTOR_ID = 1;

    static final double HATCH_ARM_MOVE_SPEED = 0.05;
    static final double HATCH_ARM_GRAB_SPEED = 0.2;

	static final double HATCH_ARM_MOVE_AXIS_THRESHHOLD = 0.8;

	//Lift Moto Ports
	static final int LIFT_MOTO_FRONT_ID = 0;
	static final int LIFT_MOTO_BACK_ID = 1;
	

//Mobility
	static final int MOBILITY_CONTROLLER_PORT = 0;

	static final int DRIVE_SPEED_AXIS = 1;
	static final int DRIVE_TURN_AXIS = 4;

	static final int DRIVE_TOGGLE_BUTTON = 1;
	static final int ULTRASONIC_TOGGLE_BUTTON = 4;

	static final int FRONT_LEFT_MOTOR_ID = 4;
	static final int MID_LEFT_MOTOR_ID = 2;
	static final int BACK_LEFT_MOTOR_ID = 5;

	static final int FRONT_RIGHT_MOTOR_ID = 6;
	static final int MID_RIGHT_MOTOR_ID = 3;
	static final int BACK_RIGHT_MOTOR_ID = 7;

	static final double DRIVE_SPEED = 0.95;
	static final double DRIVE_TURN_SPEED = 0.75;
	static final double DRIVE_DEADZONE = 0.15;

	static final int HATCH_ULTRASONIC_SENSOR_PORT = 1;
	static final double ULTRASONIC_IMPERIAL_CONVERSION_RATIO = 21.3;

//Lift
    //Other
    static final double LIFT_SPEED_AND_REVERSE_FACTOR = 1; //Make -1 to reverse
    //Buttons
    static final int LIFT_TOGGLER_BUTTON = 3;
	
}