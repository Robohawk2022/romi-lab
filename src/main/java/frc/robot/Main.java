package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.romi.RomiMotor;
import frc.robot.examples.TripleModeDrivingRobot;
import frc.robot.examples.ManualPositionControlledRobot;
import frc.robot.examples.OnOffRobot;
import frc.robot.examples.PIDPositionControlledRobot;
import frc.robot.examples.PIDTurningControlledRobot;
import frc.robot.examples.PadDrivingRobot;
import frc.robot.examples.TankDrivingRobot;

public final class Main {

    public static void main(String... args) {
        // RobotBase.startRobot(TripleModeDrivingRobot::new);
        // RobotBase.startRobot(PadDrivingRobot::new);
        RobotBase.startRobot(PIDPositionControlledRobot::new);
        // RobotBase.startRobot(PIDTurningControlledRobot::new);
        // RobotBase.startRobot(OnOffRobot::new);
        // RobotBase.startRobot(ManualPositionControlledRobot::new);
        // RobotBase.startRobot(EmptyRobot::new);
    }
}
