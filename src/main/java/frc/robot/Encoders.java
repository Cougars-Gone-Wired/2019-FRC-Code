package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;

public class Encoders {

	public SensorCollection leftSensors;
	public SensorCollection rightSensors;
	
	static double WHEEL_RADIUS = 3;
	static double CIRCUMFERENCE = 2 * Math.PI * WHEEL_RADIUS;
	static double PULSES_PER_REVOLUTION = 400; // should be 1000, but 400 works
	static double distancePerPulse = CIRCUMFERENCE / PULSES_PER_REVOLUTION;
	
	public Encoders(Drive drive) { 
		leftSensors = drive.getLeftSensors();
		rightSensors = drive.getRightSensors();
	}
	
	
	public int getLeftCount() {
		return leftSensors.getQuadraturePosition();
	}
	public int getRightCount() {
		return -rightSensors.getQuadraturePosition();
	}
	public int getAverageCount() {
		return (getLeftCount() + getRightCount()) / 2;
	}
	
	
	public double getLeftDistanceInches() {
		return getLeftCount() * distancePerPulse;
	}
	public double getRightDistanceInches() {
		return getRightCount() * distancePerPulse;
	}
	public double getAverageDistanceInches() {
		return (getLeftDistanceInches() + getRightDistanceInches()) / 2;
	}
	
	
	public double getLeftDistanceFeet() {
		return (getLeftCount() * distancePerPulse) / 12;
	}
	public double getRightDistanceFeet() {
		return (getRightCount() * distancePerPulse) / 12;
	}
	public double getAverageDistanceFeet() {
		return (getLeftDistanceFeet() + getRightDistanceFeet()) / 2;
	}
	
	
	public void resetLeftEncoder() {
		leftSensors.setQuadraturePosition(0, 10);
	}

	public void resetRightEncoder() {
		rightSensors.setQuadraturePosition(0, 10);
	}

	public void reset() {
		leftSensors.setQuadraturePosition(0, 10);
		rightSensors.setQuadraturePosition(0, 10);
	}
}
