package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import org.firstinspires.ftc.teamcode.Constants;

/** Gecko-wheel intake that pulls POLLEN from FLOWERS and the floor. */
public class IntakeSubsystem extends SubsystemBase {

    private final MotorEx motor;
    private boolean running = false;
    private boolean ejecting = false;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        motor = new MotorEx(hardwareMap, Constants.Intake.MOTOR);
        motor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
    }

    public void toggle() {
        running = !running;
    }

    /** Run backwards to clear a jam; stopEject() returns to the previous state. */
    public void startEject() {
        ejecting = true;
    }

    public void stopEject() {
        ejecting = false;
    }

    public void stop() {
        running = false;
        ejecting = false;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void periodic() {
        if (ejecting) {
            motor.set(Constants.Intake.EJECT_POWER);
        } else if (running) {
            motor.set(Constants.Intake.INTAKE_POWER);
        } else {
            motor.set(0);
        }
    }
}
