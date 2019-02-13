package frc.robot;

import java.util.stream.DoubleStream;

import edu.wpi.first.wpilibj.AnalogInput;

public class Ultrasonic {
    private AnalogInput ultrasonicSensor;

    private double[] ultrasonicLog = new double[5];

    private double rawUltrasonicValue;
    private double averagedUltrasonicValue;

    private int i = 0;

    public Ultrasonic(int ultrasonicPort) {
        ultrasonicSensor = new AnalogInput(ultrasonicPort);
        initialize();
    }

    public void initialize() {
        for (int j = 4; j >= 0; j--) {
            ultrasonicLog[j] = 0;
        }
    }

    public void setUltrasonicValue() {
        rawUltrasonicValue = ultrasonicSensor.getValue();
        setAveragedUltrasonicValue();
        ultrasonicRecord();
    }

    private void ultrasonicRecord() {
        ultrasonicLog[i] = rawUltrasonicValue;
        if (i < 4) {
            i++;
        } else {
            i = 0;
        }
    }

    private void setAveragedUltrasonicValue() {
        double sum = DoubleStream.of(ultrasonicLog).sum();
        averagedUltrasonicValue = (sum + rawUltrasonicValue) / 6;
    }

    public double getRawUltrasonicValue() {
        return rawUltrasonicValue;
    }

    public double getAveragedUltrasonicValue() {
        return averagedUltrasonicValue;
    }

    public double getImperialUltrasonicValue() { // Is averaged
        return averagedUltrasonicValue / Constants.ULTRASONIC_IMPERIAL_CONVERSION_RATIO;
    }
}