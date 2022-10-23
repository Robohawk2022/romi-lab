package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.examples.DualModeDrivingRobot;
import frc.robot.examples.ManualPositionControlledRobot;
import frc.robot.examples.OnOffRobot;
import frc.robot.examples.PIDPositionControlledRobot;
import frc.robot.examples.TankDrivingRobot;

public final class Main {

    private Main() {
    }

    public static void main(String... args) {
        RobotBase.startRobot(DualModeDrivingRobot::new);
        // RobotBase.startRobot(PIDPositionControlledRobot::new);
        // RobotBase.startRobot(OnOffRobot::new);
        // RobotBase.startRobot(ManualPositionControlledRobot::new);
        // RobotBase.startRobot(EmptyRobot::new);
    }
}
