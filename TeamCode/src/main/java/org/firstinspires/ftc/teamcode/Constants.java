package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

/**
 * Every config name and tuning number in one place. Change values here, not inside subsystems.
 * Config names must match the robot configuration on the Driver Station exactly.
 */
public final class Constants {
    private Constants() {}

    public static final class Drive {
        public static final String LEFT_FRONT = "leftFront";
        public static final String RIGHT_FRONT = "rightFront";
        public static final String LEFT_BACK = "leftBack";
        public static final String RIGHT_BACK = "rightBack";
        public static final String IMU = "imu";

        // Run "Drive Motor Test": every wheel must spin forward. Flip these until it does.
        public static final boolean INVERT_LEFT = true;
        public static final boolean INVERT_RIGHT = false;

        // How the Control Hub is mounted on the robot.
        public static final RevHubOrientationOnRobot.LogoFacingDirection LOGO_DIRECTION =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        public static final RevHubOrientationOnRobot.UsbFacingDirection USB_DIRECTION =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        public static final double SLOW_SCALE = 0.4;
    }

    public static final class Intake {
        public static final String MOTOR = "intake_motor";
        public static final double INTAKE_POWER = 1.0;
        public static final double EJECT_POWER = -0.6;
    }

    public static final class Indexer {
        public static final String GATE_SERVO = "gateServo";
        public static final double GATE_CLOSED = 0.20; // tune on the robot
        public static final double GATE_OPEN = 0.55;
    }

    public static final class Launcher {
        public static final String MOTOR = "launcherMotor";

        // Encoder ticks per second. goBILDA 6000 rpm (bare) motor = 28 ticks/rev, max ~2800 tps.
        public static final double MAX_TPS = 2800;
        public static final double TARGET_TPS = 1800; // tune by test shots
        public static final double TOLERANCE_TPS = 60;

        // Velocity PIDF: output = KP * error + KF * target. Start with KF, then add KP.
        public static final double KP = 0.0005;
        public static final double KI = 0.0;
        public static final double KD = 0.0;
        public static final double KF = 1.0 / MAX_TPS;

        // Scale power by 12 V / battery voltage so shots don't drop as the battery drains.
        public static final double NOMINAL_VOLTAGE = 12.0;
    }

    public static final class Shooter {
        public static final int POLLEN_CAPACITY = 4;
        public static final long SPIN_UP_TIMEOUT_MS = 1500; // shoot anyway if speed never reached
        public static final long GATE_OPEN_MS = 150; // time for one POLLEN to pass
        public static final long RECOVER_MS = 300; // flywheel recovers between shots
    }
}
