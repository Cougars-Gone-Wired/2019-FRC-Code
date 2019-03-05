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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Cameras {

    public enum CameraStates {
        HATCH_CAMERA, CARGO_CAMERA
    }
    CameraStates currentCameraSide = CameraStates.HATCH_CAMERA;

    UsbCamera hatchCamera;
    UsbCamera cargoCamera;

    CvSink cvSink;
    CvSource cvSource;

    Mat image;
    Mat output;

    public Cameras() {
        // hatchCamera = new UsbCamera("USB Camera 0", 0);
        // hatchCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
        // cargoCamera = new UsbCamera("USB Camera 1", 1);
        // cargoCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);

        // cvSink = CameraServer.getInstance().getVideo(hatchCamera);
        // cvSource = CameraServer.getInstance().putVideo("Current View", 320, 240);

        // image = new Mat();
        // output = new Mat();

        Thread t = new Thread(() -> {
            final int FOCAL_LENGTH = 678;
            final int CAMERA_HEIGHT = 26;
            final int STEP_HEIGHT = 19;
            final int STEP_OFFSET = CAMERA_HEIGHT - STEP_HEIGHT;
            final int ROBOT_LENGTH_OFFSET = 16;
            final int DISTANCE_TO_STEP_OFFSET = 6;
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

            //Joystick mobilityStick = new Joystick(Constants.MOBILITY_CONTROLLER_PORT);
            //Toggle switchButton = new Toggle(mobilityStick, Constants.DRIVE_TOGGLE_BUTTON);

            while(!Thread.interrupted()) {
                if (cvSink.grabFrame(image) == 0) {
                    cvSource.notifyError(cvSink.getError());
                    continue;
                }

                 // if(switchButton.toggle()) {
                //     cvSink.setSource(cargoCamera);
                // } else {
                //     cvSink.setSource(hatchCamera);
                //     if (Timer.getMatchTime() <= 20) {
                //         Imgproc.line(image, new Point(0, 240 + LINE_PIXEL_OFFSET), new Point(640, 240 + LINE_PIXEL_OFFSET), new Scalar(0, 255, 0), 5);
                //     }
                // }

                if (Sides.hatchSide) {
                    cvSink.setSource(hatchCamera);
                    if (Timer.getMatchTime() <= 20) {
                        Imgproc.line(image, new Point(0, 240 + LINE_PIXEL_OFFSET), new Point(640, 240 + LINE_PIXEL_OFFSET), new Scalar(0, 255, 0), 5);
                    }
                } else {
                    cvSink.setSource(cargoCamera);
                }

                
                Imgproc.cvtColor(image, output, Imgproc.COLOR_BGR2GRAY);
                cvSource.putFrame(output);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void cameraVideo() {
        if (cvSink.grabFrame(image) == 0) {
            cvSource.notifyError(cvSink.getError());
        } else {
            switch(currentCameraSide) {
                case HATCH_CAMERA:
                    cvSink.setSource(cargoCamera);
                    currentCameraSide = CameraStates.CARGO_CAMERA;
                    break;
                case CARGO_CAMERA:
                    cvSink.setSource(hatchCamera);
                    currentCameraSide = CameraStates.HATCH_CAMERA;
                    break;
            }
            Imgproc.cvtColor(image, output, Imgproc.COLOR_BGR2GRAY);
            cvSource.putFrame(output);
        }
    }

    public void init() {
        
    }

    public void setSide(boolean sideToggle) {
        if(sideToggle) {
            currentCameraSide = CameraStates.CARGO_CAMERA;
        } else {
            currentCameraSide = CameraStates.HATCH_CAMERA;
        }
    }
}