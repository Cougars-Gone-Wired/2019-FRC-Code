package frc.robot;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.wpilibj.Timer;

public class Cameras implements Runnable {
    private volatile boolean exit = false;

    public void run() {
        final int FOCAL_LENGTH = 678;
        final int CAMERA_HEIGHT = 26;
        final int STEP_HEIGHT = 19;
        final int STEP_OFFSET = CAMERA_HEIGHT - STEP_HEIGHT;
        final int ROBOT_LENGTH_OFFSET = 16;
        final int DISTANCE_TO_STEP_OFFSET = 11;
        final int DISTANCE_TO_STEP = ROBOT_LENGTH_OFFSET + DISTANCE_TO_STEP_OFFSET;
        final int LINE_PIXEL_OFFSET = (FOCAL_LENGTH * STEP_OFFSET) / DISTANCE_TO_STEP;

        UsbCamera hatchCamera = new UsbCamera("USB Camera 0", 0);
        hatchCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
        UsbCamera cargoCamera = new UsbCamera("USB Camera 1", 1);
        cargoCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);

        CvSink cvSink = CameraServer.getInstance().getVideo(hatchCamera);
        CvSource cvSource = CameraServer.getInstance().putVideo("Current View", 320, 240);

        Mat image = new Mat();
        Mat output = new Mat();

        while (!exit) {
            if (cvSink.grabFrame(image) == 0) {
                cvSource.notifyError(cvSink.getError());
                continue;
            }

            if (Sides.hatchSide) {
                cvSink.setSource(hatchCamera);
                Imgproc.line(image, new Point(0, 120 + (LINE_PIXEL_OFFSET / 2)), new Point(320, 120 + (LINE_PIXEL_OFFSET / 2)), new Scalar(0, 0, 0), 5);
            } else {
                cvSink.setSource(cargoCamera);
            }

            Imgproc.cvtColor(image, output, Imgproc.COLOR_BGR2GRAY);
    
            cvSource.putFrame(output);
        }
    }

    public void stop() {
        exit = true;
    }


}