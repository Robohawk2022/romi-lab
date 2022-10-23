package frc.robot.examples;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotParts;

/**
 * 
 * This is an example of a simple "on/off" robot. One button push starts it
 * moving, one button push stops it.
 * 
 * This example shows you the following things:
 * 
 * - We use variables in the robot to keep track of state in between calls to
 * teleopPeriodic, and calculate the right speed for the wheels. We always
 * want to supply a power setting to the motors, even if it's 0.
 * 
 * - We put those values on the smart dashboard that show what the robot is
 * doing in realtime. A couple of the values can actually be changed from the
 * dashboard to tweak behavior without having to reload the code.
 * 
 */
public class OnOffRobot extends TimedRobot {

    private RobotParts parts;
    private XboxController controller;
    private boolean spinning;
    private double currentSpeed;
    private double maxSpeed;
    private double speedIncrement;

    @Override
    public void robotInit() {

        parts = new RobotParts();
        controller = new XboxController(0);

        spinning = false;
        currentSpeed = 0;
        maxSpeed = 0.7;
        speedIncrement = 0.1;

        SmartDashboard.putData("On-Off Robot", (builder) -> {

            // Read only properties
            builder.addDoubleProperty("Current Speed", () -> currentSpeed, null);
            builder.addBooleanProperty("Spinning?", () -> spinning, null);

            // Read/write properties
            builder.addDoubleProperty("Adjustments/Speed Increment", () -> speedIncrement, (v) -> speedIncrement = v);
            builder.addDoubleProperty("Adjustments/Max Speed", () -> maxSpeed, (v) -> maxSpeed = v);
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
        spinning = false;
        currentSpeed = 0;
    }

    @Override
    public void teleopPeriodic() {

        // Read the control and invert the "spinning" flag if necessary.
        if (controller.getBButtonPressed()) {

            spinning = !spinning;

            // When we first start or stop spinning, we'll set the current speed
            // to a default value.
            if (spinning) {
                currentSpeed = 0.3;
            } else {
                currentSpeed = 0.0;
            }
        }

        // If we're spinning, we can adjust the speed with button presses. We want
        // to make sure we honor the max speed, though.
        if (spinning) {
            if (controller.getYButtonPressed()) {
                currentSpeed = MathUtil.clamp(currentSpeed + speedIncrement, -maxSpeed, maxSpeed);
            } else if (controller.getAButtonPressed()) {
                currentSpeed = MathUtil.clamp(currentSpeed - speedIncrement, -maxSpeed, maxSpeed);
            }
        }

        // Let's do this thing.
        parts.drive(currentSpeed);
    }
}
