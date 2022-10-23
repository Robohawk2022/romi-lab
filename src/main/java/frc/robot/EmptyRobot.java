package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;

/**
 * 
 * This is the skeleton of a robot that you can do whatever you want with.
 * 
 */
public class EmptyRobot extends TimedRobot {

  private RobotParts parts;
  private XboxController controller;

  @Override
  public void robotInit() {
    parts = new RobotParts();
    controller = new XboxController(0);
  }

  @Override
  public void autonomousInit() {
    parts.stop();
  }

  @Override
  public void disabledInit() {
    parts.stop();
  }

  @Override
  public void teleopInit() {
    parts.stop();
  }

  @Override
  public void teleopPeriodic() {
    // what do we do now?
  }
}
