package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;
import org.firstinspires.ftc.teamcode.Constants;

/** Gate between storage (up to 4 POLLEN) and the launcher. */
public class IndexerSubsystem extends SubsystemBase {

    private final ServoEx gate;

    public IndexerSubsystem(HardwareMap hardwareMap) {
        gate = new ServoEx(hardwareMap, Constants.Indexer.GATE_SERVO);
        close();
    }

    public void open() {
        gate.set(Constants.Indexer.GATE_OPEN);
    }

    public void close() {
        gate.set(Constants.Indexer.GATE_CLOSED);
    }
}
