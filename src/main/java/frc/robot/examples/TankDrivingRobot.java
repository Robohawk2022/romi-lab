package frc.robot.examples;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotParts;

/**
 * 
 * This is an example of a simple "tank drive" robot. Each wheel is controlled
 * by a different joystick - push forward on the joystick to make the wheel go
 * forward, and vice versa.
 * 
 * This example shows you the following things:
 * 
 * - We put values on the smart dashboard that show what the robot is doing in
 * realtime. A couple of the values can actually be changed from the dashboard
 * to tweak behavior without having to reload the code.
 * 
 * - We use Encoders to figure out how far the wheels have travelled, and apply
 * a slight correction to accomodate for wheel slippage / build error / etc.
 * 
 */
public class TankDrivingRobot extends TimedRobot {

    private RobotParts parts;
    private XboxController controller;
    private double driftFactor;
    private double driftAmount;
    private double driftAdjustment;
    private double maxPower;
    private double leftPower;
    private double rightPower;

    @Override
    public void robotInit() {

        parts = new RobotParts();
        controller = new XboxController(0);
        driftFactor = 0.1;
        driftAdjustment = 0.0;
        driftAmount = 0.0;
        maxPower = 0.7;
        leftPower = 0.0;
        rightPower = 0.0;

        SmartDashboard.putData("Tank Drive", (builder) -> {

            // These are "read only" properties. The part with the () is code telling the
            // dashboard how to get the values we want to send to the console.
            builder.addDoubleProperty("Drift Amount", () -> driftAmount, null);
            builder.addDoubleProperty("Drift Adjustment", () -> driftAdjustment, null);
            builder.addDoubleProperty("Power - Left", () -> leftPower, null);
            builder.addDoubleProperty("Power - Right", () -> rightPower, null);

            // These are the "read write" properties. The part with the (v) is code telling
            // the dashboard what do to with a new value coming from the console
            builder.addDoubleProperty("Drift Factor", () -> driftFactor, (v) -> driftFactor = v);
            builder.addDoubleProperty("Power Max", () -> maxPower, (v) -> maxPower = v);
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
    }

    @Override
    public void teleopPeriodic() {

        // XBox controllers sometimes "drift", meaning the joystick will never reach
        // zero and will instead always return a small value. We use a "deadband"
        // around zero to compensate for this.
        double ly = MathUtil.applyDeadband(controller.getLeftY(), 0.05);
        double ry = MathUtil.applyDeadband(controller.getRightY(), 0.05);

        // Find out the difference between the two wheels, and calculate an adjustment.
        // If the left wheel is farther ahead (robot drifts right), adjustment is
        // positive.
        driftAmount = parts.leftEncoder.getDistance() - parts.rightEncoder.getDistance();
        driftAdjustment = driftFactor * driftAmount;

        // Set the desired speed based on the joysticks. We "deadband" a little bit
        // around the zero point to protect against controller drift.
        leftPower = MathUtil.applyDeadband(-ly, 0.01) * maxPower;
        rightPower = MathUtil.applyDeadband(-ry, 0.01) * maxPower;

        // We only want to apply the correction if someone is trying to drive straight.
        // We'll detect this be checking if both sticks are pushed and they are roughly
        // equal to one another.
        if (ly != 0 && ry != 0 && Math.abs(ly - ry) < 0.1) {
            leftPower -= driftAdjustment;
            rightPower += driftAdjustment;
        }

        // If we're not going to apply our adjustment, we want to reset the encoders
        // back to 0, because any mismatch that builds up between the wheels is meant
        // to be there (it's not accidental drift).
        else {
            parts.resetEncoders();
        }

        // Let's do this thing.
        parts.drive.tankDrive(leftPower, rightPower);
    }
}
