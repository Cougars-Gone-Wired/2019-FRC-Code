package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Ultrasonic {
    private AnalogInput ultrasonicSensor;

    private double[] ultrasonicLog = new double[5];

    private double rawUltrasonicValue;
    private double averagedUltrasonicValue;

    public Ultrasonic(int ultrasonicPort) {
        ultrasonicSensor = new AnalogInput(ultrasonicPort);
        
    }

    public void setUltrasonicValue() {
        rawUltrasonicValue = ultrasonicSensor.getValue();
        ultrasonicRecord();
        setAveragedUltrasonicValue();
    }

    private void ultrasonicRecord() {
        for (int i = 4; i > 0; i--) {
            ultrasonicLog[i] = ultrasonicLog[i - 1];
        }
        ultrasonicLog[0] = rawUltrasonicValue;
    }

    private void setAveragedUltrasonicValue() {
        double sum = 0;
        for (double i : ultrasonicLog) {
            sum += i;
        }
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