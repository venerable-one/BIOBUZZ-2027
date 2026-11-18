package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.commands.ShootAllCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;

/**
 * Starter AUTO, time-based (odometry/Pedro comes later): launch the 4 preloaded POLLEN, then drive
 * off the wall for LEAVE (3 pts). Start position, aim and drive time are placeholders.
 */
@Autonomous(name = "Auto: Shoot Preload + Leave", group = "Competition")
public class AutoLeave extends CommandOpMode {

    private static final double DRIVE_POWER = 0.4;
    private static final long DRIVE_TIME_MS = 800;

    private DriveSubsystem drive;
    private IndexerSubsystem indexer;
    private LauncherSubsystem launcher;

    @Override
    public void initialize() {
        drive = new DriveSubsystem(hardwareMap);
        new IntakeSubsystem(hardwareMap); // registers itself so the intake stays off
        indexer = new IndexerSubsystem(hardwareMap);
        launcher = new LauncherSubsystem(hardwareMap);
    }

    @Override
    public void preRun() { // runs once, right after START
        schedule(
                new SequentialCommandGroup(
                        new ShootAllCommand(launcher, indexer),
                        new InstantCommand(() -> drive.driveRobotCentric(0, DRIVE_POWER, 0), drive),
                        new WaitCommand(DRIVE_TIME_MS),
                        new InstantCommand(drive::stop, drive)));
    }

    @Override
    public void run() {
        super.run();
        telemetry.addData("Launcher tps", "%.0f", launcher.getVelocity());
        telemetry.update();
    }
}
