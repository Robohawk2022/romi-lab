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
public class PIDTurningControlledRobot extends TimedRobot {

    private RobotParts parts;
    private XboxController controller;

    // Adjustable properties: how far to turn with each push of a button, and top speed
    private double angleIncrement;
    private double maxSpeed;

    // These capture the state of the robot - where we want to be, where we are, how
    // current speed of the wheels.
    private double targetAngle;
    private double currentAngle;
    private double desiredSpeed;

    // This is the controller we use to calculate the adjustment required
    private PIDController pidController;

    @Override
    public void robotInit() {

        parts = new RobotParts();
        controller = new XboxController(0);

        angleIncrement = 90;
        maxSpeed = 0.6;
        targetAngle = 0;
        currentAngle = 0;
        desiredSpeed = 0;

        pidController = new PIDController(1.0, 0, 0);
        pidController.enableContinuousInput(-180, 180);
        pidController.setTolerance(5.0);

        SmartDashboard.putData("PID Controller", pidController);
        SmartDashboard.putData("PID Robot", (builder) -> {

            // Read only properties
            builder.addDoubleProperty("Target Angle", () -> targetAngle, null);
            builder.addDoubleProperty("Current Angle", () -> currentAngle, null);
            builder.addDoubleProperty("Desired Speed", () -> desiredSpeed, null);

            // Read-write properties
            builder.addDoubleProperty("Max Speed", () -> maxSpeed, (v) -> maxSpeed = v);
            builder.addDoubleProperty("Angle Increment", () -> angleIncrement, (v) -> angleIncrement = v);
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
        
        targetAngle = parts.getAngle();
    }

    @Override
    public void teleopPeriodic() {

        // Capture the current position of the wheels.
        currentAngle = parts.getAngle();

        // Adjust the target angle based on button presses. Note that we are 
        // "wrapping" the angle to make sure it's between -180 and 180.
        if (controller.getYButtonPressed()) {
            targetAngle += angleIncrement;
            if (targetAngle > 180) {
                targetAngle = targetAngle - 360;
            }
        } else if (controller.getAButtonPressed()) {
            targetAngle -= angleIncrement;
            if (targetAngle < -180) {
                targetAngle = targetAngle + 360;
            }
        } else if (controller.getBButtonPressed()) {
            targetAngle = currentAngle;
        }

        // Determine the correct wheel speed based on the current and target distance
        desiredSpeed = pidController.calculate(currentAngle, targetAngle);

        // Clamp the speed so we don't go too fast
        desiredSpeed = MathUtil.clamp(desiredSpeed / angleIncrement, -maxSpeed, maxSpeed);

        // Let's do this thing.
        parts.leftMotor.set(desiredSpeed / 2);
        parts.rightMotor.set(-desiredSpeed / 2);
    }
}
