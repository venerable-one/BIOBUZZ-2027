# BIOBUZZ 2026-27 robot code

FTC SDK 12.0 + [SolversLib 0.3.6](https://docs.seattlesolvers.com/) (command-based).
The original FTC SDK readme is in `doc/FTC-SDK-README.md`.

## Setup

1. Install Android Studio (Narwhal 3 Feature Drop or newer).
2. Clone this repo, then **File > Open** the folder.
3. Let Gradle sync. It downloads the FTC SDK and SolversLib.
4. Before every commit, run `gradlew spotlessApply` (formats the code the same way for everyone).

## Code layout

All our code is in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`.

| Path | What it is |
| --- | --- |
| `Constants.java` | Every config name and tuning number. Change values here. |
| `subsystems/DriveSubsystem.java` | Mecanum drive + IMU (field-centric) |
| `subsystems/IntakeSubsystem.java` | Gecko-wheel intake |
| `subsystems/IndexerSubsystem.java` | Gate servo between storage and launcher |
| `subsystems/LauncherSubsystem.java` | Flywheel with PIDF speed control + battery compensation |
| `commands/ShootAllCommand.java` | Spin up, wait for speed, fire all 4 POLLEN |
| `opmodes/TeleOpMain.java` | Competition TeleOp |
| `opmodes/AutoLeave.java` | Starter AUTO: shoot preload, drive off the wall |
| `opmodes/tests/DriveMotorTest.java` | One wheel per button, for wiring checks |
| `basics/` | Plain-SDK drive code with no library, for learning |

How SolversLib code fits together: a **subsystem** owns hardware (motors, servos) and has simple methods
(`spinUp()`, `open()`). A **command** is an action that uses subsystems. In the OpMode, **buttons are bound
to commands**. To add a mechanism: new subsystem, new commands, bind buttons in `TeleOpMain`.

## Robot configuration names

| Device | Config name |
| --- | --- |
| Drive motors | `leftFront`, `rightFront`, `leftBack`, `rightBack` |
| IMU | `imu` |
| Intake motor | `intake_motor` |
| Gate servo | `gateServo` |
| Launcher motor | `launcherMotor` |

## TeleOp controls (gamepad 1)

| Input | Action |
| --- | --- |
| Left stick | Drive / strafe (field-centric) |
| Right stick X | Turn |
| Left bumper (hold) | Slow mode |
| Options | Reset heading (robot facing away from driver) |
| Triangle | Intake on/off |
| Cross (hold) | Eject |
| D-pad up / down | Launcher spin up / stop |
| Right bumper | Shoot all 4 POLLEN |

## Working as a team

- Never code directly on `main`. Make a branch per task (`shooter-hood`, `limelight-aim`), then open a pull request.
- Pull before you start, commit small, write what you changed.
- Anything that needs tuning goes in `Constants.java`, not hard-coded in a subsystem.

## First robot test

1. Robot on blocks, run **Drive Motor Test**: every wheel must spin forward. Fix with `INVERT_LEFT` / `INVERT_RIGHT`.
2. Run **TeleOpMain**, rotate the robot 90 degrees, push the stick forward: it should still drive away from you.
3. Tune `Constants.Launcher`: set `KF` so speed is close, then raise `KP` until it holds `TARGET_TPS` under load.
