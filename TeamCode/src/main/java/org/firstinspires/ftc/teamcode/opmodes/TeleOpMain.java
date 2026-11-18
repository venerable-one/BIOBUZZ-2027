package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.commands.ShootAllCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;

// Gamepad 1 controls:
//   left stick       drive / strafe (field-centric)   right stick X   turn
//   left bumper      hold = slow mode                 options         reset heading
//   triangle         intake on/off                    cross           hold = eject
//   dpad up / down   launcher spin up / stop          right bumper    shoot all 4 POLLEN
@TeleOp(name = "TeleOpMain", group = "Competition")
public class TeleOpMain extends CommandOpMode {

    private DriveSubsystem drive;
    private IntakeSubsystem intake;
    private IndexerSubsystem indexer;
    private LauncherSubsystem launcher;

    @Override
    public void initialize() {
        drive = new DriveSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        indexer = new IndexerSubsystem(hardwareMap);
        launcher = new LauncherSubsystem(hardwareMap);

        GamepadEx driver = new GamepadEx(gamepad1);

        // Drive runs every loop unless another command needs the drivetrain.
        drive.setDefaultCommand(
                new RunCommand(
                        () ->
                                drive.driveFieldCentric(
                                        driver.getLeftX(), driver.getLeftY(), driver.getRightX()),
                        drive));

        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new InstantCommand(() -> drive.setSpeedScale(Constants.Drive.SLOW_SCALE)))
                .whenReleased(new InstantCommand(() -> drive.setSpeedScale(1.0)));
        driver.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenPressed(new InstantCommand(drive::resetHeading));

        driver.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(new InstantCommand(intake::toggle, intake));
        driver.getGamepadButton(GamepadKeys.Button.CROSS)
                .whenPressed(new InstantCommand(intake::startEject, intake))
                .whenReleased(new InstantCommand(intake::stopEject, intake));

        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new InstantCommand(launcher::spinUp, launcher));
        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new InstantCommand(launcher::stop, launcher));
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new ShootAllCommand(launcher, indexer));

        telemetry.addLine("Ready. Face the robot away from the driver, then press START.");
        telemetry.update();
    }

    @Override
    public void run() {
        super.run(); // runs the command scheduler: buttons, commands, every subsystem's periodic()
        telemetry.addData("Heading (deg)", "%.1f", drive.getHeadingDegrees());
        telemetry.addData("Intake", intake.isRunning() ? "ON" : "off");
        telemetry.addData("Launcher tps", "%.0f / %.0f", launcher.getVelocity(), launcher.getTarget());
        telemetry.addData("At speed", launcher.atSpeed());
        telemetry.update();
    }
}
