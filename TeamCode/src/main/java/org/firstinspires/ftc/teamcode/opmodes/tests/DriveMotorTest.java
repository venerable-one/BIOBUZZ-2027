package org.firstinspires.ftc.teamcode.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.Constants;

// Bring-up test, robot on blocks. One wheel per button; every wheel must spin FORWARD.
//   X / square = leftFront    Y / triangle = rightFront
//   A / cross  = leftBack     B / circle   = rightBack
// If a wheel spins backward, flip INVERT_LEFT / INVERT_RIGHT in Constants.Drive.
@TeleOp(name = "Drive Motor Test", group = "Test")
public class DriveMotorTest extends LinearOpMode {

    private static final double TEST_POWER = 0.3;

    @Override
    public void runOpMode() {
        DcMotor leftFront = motor(Constants.Drive.LEFT_FRONT, Constants.Drive.INVERT_LEFT);
        DcMotor rightFront = motor(Constants.Drive.RIGHT_FRONT, Constants.Drive.INVERT_RIGHT);
        DcMotor leftBack = motor(Constants.Drive.LEFT_BACK, Constants.Drive.INVERT_LEFT);
        DcMotor rightBack = motor(Constants.Drive.RIGHT_BACK, Constants.Drive.INVERT_RIGHT);

        telemetry.addLine("Robot on blocks! Press START.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            leftFront.setPower(gamepad1.x ? TEST_POWER : 0);
            rightFront.setPower(gamepad1.y ? TEST_POWER : 0);
            leftBack.setPower(gamepad1.a ? TEST_POWER : 0);
            rightBack.setPower(gamepad1.b ? TEST_POWER : 0);

            telemetry.addData("leftFront  (X)", leftFront.getCurrentPosition());
            telemetry.addData("rightFront (Y)", rightFront.getCurrentPosition());
            telemetry.addData("leftBack   (A)", leftBack.getCurrentPosition());
            telemetry.addData("rightBack  (B)", rightBack.getCurrentPosition());
            telemetry.update();
        }
    }

    private DcMotor motor(String name, boolean inverted) {
        DcMotor m = hardwareMap.get(DcMotor.class, name);
        m.setDirection(inverted ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
        return m;
    }
}
