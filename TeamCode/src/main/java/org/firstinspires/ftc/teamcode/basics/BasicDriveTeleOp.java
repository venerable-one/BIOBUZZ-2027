package org.firstinspires.ftc.teamcode.basics;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Gamepad 1:
 *   left stick  = drive / strafe     right stick X = turn
 *   left bumper = slow mode (40%)    back          = toggle field/robot-centric
 *   options     = reset heading (robot must face away from driver)
 */
@TeleOp(name = "Drive Only (plain SDK)", group = "Test")
public class BasicDriveTeleOp extends LinearOpMode {

    private static final double STRAFE_CORRECTION = 1.1; // mecanum strafes slower than it drives
    private static final double SLOW_SCALE = 0.4;

    @Override
    public void runOpMode() {
        BasicMecanumDrive drive = new BasicMecanumDrive(hardwareMap);
        boolean fieldCentric = true;
        boolean backWasDown = false;

        telemetry.addLine("Ready. Face robot away from driver, then press START.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            double forward = -gamepad1.left_stick_y;              // stick up reads negative
            double strafe  =  gamepad1.left_stick_x * STRAFE_CORRECTION;
            double turn    =  gamepad1.right_stick_x;

            double scale = gamepad1.left_bumper ? SLOW_SCALE : 1.0;
            forward *= scale;
            strafe  *= scale;
            turn    *= scale;

            // Edge detection: toggle once per press, not every loop while held.
            if (gamepad1.back && !backWasDown) {
                fieldCentric = !fieldCentric;
            }
            backWasDown = gamepad1.back;

            if (gamepad1.options) {
                drive.resetHeading();
            }

            if (fieldCentric) {
                drive.driveFieldCentric(forward, strafe, turn);
            } else {
                drive.driveRobotCentric(forward, strafe, turn);
            }

            telemetry.addData("Mode", fieldCentric ? "FIELD-centric" : "ROBOT-centric");
            telemetry.addData("Heading (deg)", "%.1f", drive.getHeadingDegrees());
            telemetry.addData("Sticks f/s/t", "%.2f / %.2f / %.2f", forward, strafe, turn);
            telemetry.update();
        }
        drive.stop();
    }
}
