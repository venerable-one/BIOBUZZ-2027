package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import org.firstinspires.ftc.teamcode.Constants;

/**
 * Flywheel that launches POLLEN into the HIVE. Holds a target speed with a PIDF loop that runs in
 * periodic(), so every shot leaves at the same speed even as the battery drains.
 */
public class LauncherSubsystem extends SubsystemBase {

    private final MotorEx flywheel;
    private final VoltageSensor battery;
    private final PIDFController controller =
            new PIDFController(
                    Constants.Launcher.KP,
                    Constants.Launcher.KI,
                    Constants.Launcher.KD,
                    Constants.Launcher.KF);

    private double targetTps = 0;

    public LauncherSubsystem(HardwareMap hardwareMap) {
        flywheel = new MotorEx(hardwareMap, Constants.Launcher.MOTOR, Motor.GoBILDA.BARE);
        flywheel.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT); // coast down, don't brake
        battery = hardwareMap.voltageSensor.iterator().next();
    }

    public void spinUp() {
        targetTps = Constants.Launcher.TARGET_TPS;
    }

    public void stop() {
        targetTps = 0;
    }

    public double getVelocity() {
        return flywheel.getVelocity();
    }

    public double getTarget() {
        return targetTps;
    }

    public boolean atSpeed() {
        return targetTps > 0
                && Math.abs(getVelocity() - targetTps) < Constants.Launcher.TOLERANCE_TPS;
    }

    @Override
    public void periodic() {
        if (targetTps <= 0) {
            controller.reset();
            flywheel.set(0);
            return;
        }
        double power = controller.calculate(getVelocity(), targetTps);
        power *= Constants.Launcher.NOMINAL_VOLTAGE / battery.getVoltage();
        flywheel.set(Math.max(0, Math.min(1, power)));
    }
}
