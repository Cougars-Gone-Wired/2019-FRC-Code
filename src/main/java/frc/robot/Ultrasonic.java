package frc.robot;

import java.util.stream.DoubleStream;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Ultrasonic {
    private AnalogInput ultrasonicSensor;

    private double[] ultrasonicLog = new double[Constants.ULTRASONIC_LOG_ELEMENT_COUNT];

    private double rawUltrasonicValue;
    private double averagedUltrasonicValue;

    private int i = 0;

    public Ultrasonic(int ultrasonicPort) {
        ultrasonicSensor = new AnalogInput(ultrasonicPort);
        initialize();
    }

    public void initialize() {
        for (int j = Constants.ULTRASONIC_LOG_ELEMENT_COUNT - 1; j >= 0; j--) {
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
        if (i < Constants.ULTRASONIC_LOG_ELEMENT_COUNT - 1) {
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

    public void displayValues(String key) {
        SmartDashboard.putNumber(key + " Raw Value", rawUltrasonicValue);
        SmartDashboard.putNumber(key + " Imperial Value", averagedUltrasonicValue);
    }
}