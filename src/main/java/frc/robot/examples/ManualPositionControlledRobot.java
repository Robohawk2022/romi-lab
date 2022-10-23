package frc.robot.examples;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotParts;

/**
 * 
 * This is an example of trying to drive wheels to a specific position. In
 * this case, we are moving the robot forward by a fixed amount.
 * 
 * This example shows you the following things:
 * 
 * - When you're trying to hit a target (position, velocity), you are aiming for
 * "good enough". For instance, if you're 0.00001 inches away from your target,
 * that's probably enough.
 * 
 * - When you're trying to hit a target, you have to consider the possibility
 * that you're going to overshoot. If you account for that, then you have to
 * consider the possibility that you're going to keep oscillating around the
 * set point.
 * 
 */
public class ManualPositionControlledRobot extends TimedRobot {

    private RobotParts parts;
    private XboxController controller;

    // These capture the state of the robot - where we want to be, where we are, how
    // far off we are, and the current speed of the wheels.
    private double targetDistance;
    private double currentDistance;
    private double deltaDistance;
    private double drivePower;

    // Adjustable settings to determine how close we want to get to our target
    // distance, how fast to adjust position forward/backward
    private double tolerance;
    private double forwardPower;
    private double reversePower;

    @Override
    public void robotInit() {

        parts = new RobotParts();
        controller = new XboxController(0);

        targetDistance = 0;
        currentDistance = 0;
        deltaDistance = 0;
        drivePower = 0;
        forwardPower = 0.7;
        reversePower = 0.3;
        tolerance = 0.1;

        SmartDashboard.putData("Lousy Controller", (builder) -> {

            // Read only properties
            builder.addDoubleProperty("Distance - Current", () -> currentDistance, null);
            builder.addDoubleProperty("Distance - Target", () -> targetDistance, null);
            builder.addDoubleProperty("Distance Delta", () -> deltaDistance, null);
            builder.addDoubleProperty("Power - Current", () -> drivePower, null);

            // Read-write properties
            builder.addDoubleProperty("Tolerance", () -> tolerance, (v) -> tolerance = v);
            builder.addDoubleProperty("Power Forward", () -> forwardPower, (v) -> forwardPower = v);
            builder.addDoubleProperty("Power Reverse", () -> reversePower, (v) -> reversePower = v);
        });
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
        parts.resetEncoders();

        // We use the left wheel's encoder to represent position. The left and right
        // wheels should report roughly the same position.
        targetDistance = parts.leftEncoder.getDistance();
    }

    @Override
    public void teleopPeriodic() {

        // Capture the current position of the wheels.
        currentDistance = parts.leftEncoder.getDistance();

        // Adjust the target distance based on button presses.
        if (controller.getYButtonPressed()) {
            targetDistance += 12;
        } else if (controller.getBButtonPressed()) {
            targetDistance = currentDistance;
        }

        // Determine how far away we are from the target distance.
        deltaDistance = targetDistance - currentDistance;

        // If we not within the tolerance, we need to move either forward or backward.
        drivePower = 0;
        if (Math.abs(deltaDistance) > tolerance) {
            if (deltaDistance > 0) {
                drivePower = forwardPower;
            } else {
                drivePower = -reversePower;
            }
        }

        // Let's do this thing.
        parts.drive(drivePower);
    }
}
