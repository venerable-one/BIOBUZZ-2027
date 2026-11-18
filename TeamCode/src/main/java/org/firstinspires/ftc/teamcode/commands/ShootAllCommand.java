package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IndexerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;

/** Spin up, wait for speed, then feed every stored POLLEN into the HIVE one at a time. */
public class ShootAllCommand extends SequentialCommandGroup {

    private final LauncherSubsystem launcher;
    private final IndexerSubsystem indexer;

    public ShootAllCommand(LauncherSubsystem launcher, IndexerSubsystem indexer) {
        this.launcher = launcher;
        this.indexer = indexer;

        addCommands(
                new InstantCommand(launcher::spinUp, launcher),
                new WaitUntilCommand(launcher::atSpeed)
                        .withTimeout(Constants.Shooter.SPIN_UP_TIMEOUT_MS));

        for (int shot = 0; shot < Constants.Shooter.POLLEN_CAPACITY; shot++) {
            addCommands(
                    new InstantCommand(indexer::open, indexer),
                    new WaitCommand(Constants.Shooter.GATE_OPEN_MS),
                    new InstantCommand(indexer::close, indexer),
                    new WaitCommand(Constants.Shooter.RECOVER_MS));
        }

        addCommands(new InstantCommand(launcher::stop, launcher));
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        if (interrupted) { // never leave the gate open or the flywheel spinning
            indexer.close();
            launcher.stop();
        }
    }
}
