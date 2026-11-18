package org.firstinspires.ftc.teamcode.basics;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Plain-SDK mecanum drive (Module 1 lesson). No library: the wheel math is written out by hand.
 * Config names match the DECODE robot: leftFront, rightFront, leftBack, rightBack, imu.
 */
public class BasicMecanumDrive {

    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftBack;
    private final DcMotor rightBack;
    private final IMU imu;

    public BasicMecanumDrive(HardwareMap hardwareMap) {
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack   = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack  = hardwareMap.get(DcMotor.class, "rightBack");

        // One side must be reversed so "positive power" drives forward on both sides.
        // If the robot drives backward on forward stick, reverse the RIGHT side instead.
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        DcMotor[] motors = {leftFront, rightFront, leftBack, rightBack};
        for (DcMotor m : motors) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        imu = hardwareMap.get(IMU.class, "imu");
        // Match these two directions to how the Control Hub is mounted.
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
        imu.resetYaw();
    }

    /**
     * Robot-centric drive. Inputs in [-1, 1].
     * forward: + = forward, strafe: + = right, turn: + = clockwise.
     */
    public void driveRobotCentric(double forward, double strafe, double turn) {
        double fl = forward + strafe + turn;
        double fr = forward - strafe - turn;
        double bl = forward - strafe + turn;
        double br = forward + strafe - turn;

        // Scale all four down together if any exceeds 1, so the direction is preserved.
        double max = Math.max(1.0, Math.max(
                Math.max(Math.abs(fl), Math.abs(fr)),
                Math.max(Math.abs(bl), Math.abs(br))));

        leftFront.setPower(fl / max);
        rightFront.setPower(fr / max);
        leftBack.setPower(bl / max);
        rightBack.setPower(br / max);
    }

    /** Field-centric drive: stick "up" always means away from the driver. */
    public void driveFieldCentric(double forward, double strafe, double turn) {
        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotStrafe  = strafe * Math.cos(-heading) - forward * Math.sin(-heading);
        double rotForward = strafe * Math.sin(-heading) + forward * Math.cos(-heading);
        driveRobotCentric(rotForward, rotStrafe, turn);
    }

    public void resetHeading() {
        imu.resetYaw();
    }

    public double getHeadingDegrees() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void stop() {
        driveRobotCentric(0, 0, 0);
    }
}
