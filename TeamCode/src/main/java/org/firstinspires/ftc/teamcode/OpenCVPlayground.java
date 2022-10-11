
package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Var.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp
public class OpenCVPlayground extends LinearOpMode {
    private OpenCvWebcam webcam;;
    private PachetelNouOpenCV pipeline = new PachetelNouOpenCV();
    private double width, height;
    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(),telemetry);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        webcam.setPipeline(pipeline);

        webcam.setMillisecondsPermissionTimeout(2500);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(Webcam_w, Webcam_h, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
        telemetry.addLine("Waiting for start");
        telemetry.update();
        FtcDashboard.getInstance().startCameraStream(webcam, 0);
        waitForStart();

        while (opModeIsActive()) {
            width = pipeline.getRect().width;
            height = pipeline.getRect().height;
            telemetry.addData("Frame Count", webcam.getFrameCount());
            telemetry.addData("FPS", String.format("%.2f", webcam.getFps()));
            telemetry.addData("Total frame time ms", webcam.getTotalFrameTimeMs());
            telemetry.addData("Pipeline time ms", webcam.getPipelineTimeMs());
            telemetry.addData("Overhead time ms", webcam.getOverheadTimeMs());
            telemetry.addData("Theoretical max FPS", webcam.getCurrentPipelineMaxFps());
            telemetry.addData("Rectangle Width:", width);
            telemetry.addData("Rectangle Height:", height);
            telemetry.addData("Rectangle H/W:", height/width);
            if(height/width < 0.9){
                telemetry.addData("Rect","3");
            }
            else if(height/width < 3){
                telemetry.addData("Rect","1");
            }
            else{
                telemetry.addData("Rect","2");
            }
            telemetry.update();

        }
    }
    /*
    H/W(big=1):1.588
    H/W(small=3):0.23
    H/W(medium=2):4.23
     */
}