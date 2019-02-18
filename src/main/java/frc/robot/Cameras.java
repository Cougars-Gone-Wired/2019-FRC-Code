package frc.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;

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
        hatchCamera = new UsbCamera("USB Camera 0", 0);
        hatchCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);
        cargoCamera = new UsbCamera("USB Camera 1", 1);
        cargoCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 15);

        cvSink = CameraServer.getInstance().getVideo(hatchCamera);
        cvSource = CameraServer.getInstance().putVideo("Current View", 320, 240);

        image = new Mat();
        output = new Mat();
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

    public void setSide(boolean sideToggle) {
        if(sideToggle) {
            currentCameraSide = CameraStates.CARGO_CAMERA;
        } else {
            currentCameraSide = CameraStates.HATCH_CAMERA;
        }
    }
}