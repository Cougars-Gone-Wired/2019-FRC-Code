package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Ultrasonic {
    private AnalogInput ultrasonicSensor;

    private double rawUltrasonicValue;

    public Ultrasonic(int ultrasonicPort) {
        ultrasonicSensor = new AnalogInput(ultrasonicPort);
    }

    public void setUltrasonicValue() {
        rawUltrasonicValue = ultrasonicSensor.getValue();
    }

    public double getRawUltrasonicValue() {
        return rawUltrasonicValue;
    }

    public double getImperialUltrasonicValue() {
        return rawUltrasonicValue / Constants.ULTRASONIC_IMPERIAL_CONVERSION_RATIO;
    }
}