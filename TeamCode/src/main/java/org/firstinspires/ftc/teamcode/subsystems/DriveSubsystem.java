package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Constants;

/** Mecanum drivetrain + IMU. SolversLib's MecanumDrive does the wheel math and normalization. */
public class DriveSubsystem extends SubsystemBase {

    private final MecanumDrive drive;
    private final IMU imu;
    private double speedScale = 1.0;

    public DriveSubsystem(HardwareMap hardwareMap) {
        MotorEx leftFront = driveMotor(hardwareMap, Constants.Drive.LEFT_FRONT, Constants.Drive.INVERT_LEFT);
        MotorEx rightFront = driveMotor(hardwareMap, Constants.Drive.RIGHT_FRONT, Constants.Drive.INVERT_RIGHT);
        MotorEx leftBack = driveMotor(hardwareMap, Constants.Drive.LEFT_BACK, Constants.Drive.INVERT_LEFT);
        MotorEx rightBack = driveMotor(hardwareMap, Constants.Drive.RIGHT_BACK, Constants.Drive.INVERT_RIGHT);

        // false = don't auto-invert the right side; Constants.Drive controls inversion instead.
        drive = new MecanumDrive(false, leftFront, rightFront, leftBack, rightBack);

        imu = hardwareMap.get(IMU.class, Constants.Drive.IMU);
        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                Constants.Drive.LOGO_DIRECTION, Constants.Drive.USB_DIRECTION)));
        imu.resetYaw();
    }

    private static MotorEx driveMotor(HardwareMap hardwareMap, String name, boolean inverted) {
        MotorEx motor = new MotorEx(hardwareMap, name, Motor.GoBILDA.RPM_312);
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        motor.setInverted(inverted);
        return motor;
    }

    /** Inputs in [-1, 1]. strafe + = right, forward + = forward, turn + = clockwise. */
    public void driveFieldCentric(double strafe, double forward, double turn) {
        drive.driveFieldCentric(
                strafe * speedScale, forward * speedScale, turn * speedScale, getHeadingDegrees());
    }

    public void driveRobotCentric(double strafe, double forward, double turn) {
        drive.driveRobotCentric(strafe * speedScale, forward * speedScale, turn * speedScale);
    }

    public void setSpeedScale(double scale) {
        speedScale = scale;
    }

    /** Call with the robot facing away from the driver. */
    public void resetHeading() {
        imu.resetYaw();
    }

    public double getHeadingDegrees() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void stop() {
        drive.stop();
    }
}
