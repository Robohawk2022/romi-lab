package frc.robot.examples;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotParts;

/**
 * 
 * This is another example of trying to drive wheels to a specific position. In
 * this case, we can move the robot forward or backwards.
 * 
 * This example shows you the following things:
 * 
 * - A PIDController implements "closed loop" control. It uses the current
 * and desired state of the robot to tell you exactly how to drive the motors
 * to where they should be.
 * 
 */
public class PIDPositionControlledRobot extends TimedRobot {

    private RobotParts parts;
    private XboxController controller;

    // Adjustable properties: how far to move with each push of a button, and top speed
    private double distanceIncrement;
    private double maxSpeed;

    // These capture the state of the robot - where we want to be, where we are, how
    // current speed of the wheels.
    private double targetDistance;
    private double currentDistance;
    private double desiredSpeed;

    // This is the controller we use to calculate the adjustment required
    private PIDController pidController;

    @Override
    public void robotInit() {

        parts = new RobotParts();
        controller = new XboxController(0);

        distanceIncrement = 12;
        maxSpeed = 0.7;
        targetDistance = 0;
        currentDistance = 0;
        desiredSpeed = 0;
        pidController = new PIDController(1.0, 0, 0);

        SmartDashboard.putData("PID Controller", pidController);
        SmartDashboard.putData("PID Robot", (builder) -> {

            // Read only properties
            builder.addDoubleProperty("Target Distance", () -> targetDistance, null);
            builder.addDoubleProperty("Current Distance", () -> currentDistance, null);
            builder.addDoubleProperty("Desired Speed", () -> desiredSpeed, null);

            // Read-write properties
            builder.addDoubleProperty("Max Speed", () -> maxSpeed, (v) -> maxSpeed = v);
            builder.addDoubleProperty("Distance Increment", () -> distanceIncrement, (v) -> distanceIncrement = v);
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
        } else if (controller.getAButtonPressed()) {
            targetDistance -= 12;
        } else if (controller.getBButtonPressed()) {
            targetDistance = currentDistance;
        }

        // Determine the right wheel speed based on the current and target distance
        desiredSpeed = pidController.calculate(currentDistance, targetDistance);

        // Clamp the speed so we don't go too fast
        desiredSpeed = MathUtil.clamp(desiredSpeed, -maxSpeed, maxSpeed);

        // Let's do this thing.
        parts.drive(desiredSpeed);
    }
}
