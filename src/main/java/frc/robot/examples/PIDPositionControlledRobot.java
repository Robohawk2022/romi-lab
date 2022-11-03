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
    private double increment;
    private double maxSpeed;
    private double tolerance;

    // This is the controller we use to calculate the adjustment required
    private PIDController leftPid;
    private PIDController rightPid;

    @Override
    public void robotInit() {

        parts = new RobotParts();
        parts.drive.setSafetyEnabled(false);

        controller = new XboxController(0);

        increment = 12;
        maxSpeed = 0.7;
        tolerance = 0.5;

        leftPid = new PIDController(0.15, 0.03, 0.05);        
        leftPid.setTolerance(0.5);

        rightPid = new PIDController(0.15, 0.03, 0.05);
        rightPid.setTolerance(0.5);

        SmartDashboard.putData("PID Controller/Left", leftPid);
        SmartDashboard.putData("PID Controller/Right", rightPid);
        SmartDashboard.putData("PID Robot", (builder) -> {
            builder.addDoubleProperty("Distance Increment", () -> increment, (v) -> increment = v);
            builder.addDoubleProperty("Max Speed", () -> maxSpeed, (v) -> maxSpeed = v);
            builder.addDoubleProperty("Tolerance", () -> tolerance, (v) -> {
                tolerance = v;
                leftPid.setTolerance(tolerance);
                rightPid.setTolerance(tolerance);
            });
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
        resetPids();
    }

    private void incrementPids(double amount) {
        leftPid.setSetpoint(leftPid.getSetpoint() + amount);
        rightPid.setSetpoint(rightPid.getSetpoint() + amount);
    }

    private void resetPids() {
        leftPid.setSetpoint(parts.leftEncoder.getDistance());
        rightPid.setSetpoint(parts.rightEncoder.getDistance());
    }

    @Override
    public void teleopPeriodic() {

        // Hitting the B button should reset the PIDs and stop the robot.
        if (controller.getBButtonPressed()) {
            parts.stop();
            resetPids();
            return;
        }

        // Adjust the target distance based on button presses.
        if (controller.getYButtonPressed()) {
            incrementPids(increment);
        } else if (controller.getAButtonPressed()) {
            incrementPids(-increment);
        }

        // Determine the correct wheel speed based on the current and target distance
        double leftSpeed = leftPid.calculate(parts.leftEncoder.getDistance());
        double rightSpeed = rightPid.calculate(parts.rightEncoder.getDistance());

        // Clamp the speed so we don't go too fast
        leftSpeed = MathUtil.clamp(leftSpeed, -maxSpeed, maxSpeed);
        rightSpeed = MathUtil.clamp(rightSpeed, -maxSpeed, maxSpeed);

        // Let's do this thing.
        parts.leftMotor.set(leftSpeed);
        parts.rightMotor.set(rightSpeed);
    }
}
