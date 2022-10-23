package frc.robot.examples;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotParts;

/**
 * 
 * This is a free-driving robot which implements two modes of driving:
 * - Tank mode: steer each wheel independently forward/back with the
 * corresponding joystick
 * - Arcade mode: left joystick controls forward/back, right joystick controls
 * turning
 * 
 * This example shows you the following things:
 * 
 * - We use a "SendableChooser" to put a control on the dashboard that lets us
 * select our
 * drive mode without having to redeploy new code to the robot.
 * 
 * - We read input from the joystick and apply a "deadband" (which ignores very
 * small values),
 * to deal with joystick drift if there is any.
 * 
 * - We apply a "max power" factor to prevent running the motors full out.
 * 
 * - We deal with the weird mapping of the Y coordinate for XBox joysticks.
 * 
 */
public class DualModeDrivingRobot extends TimedRobot {

    public static final double MAX_POWER = 0.7;

    public static enum DriveMode {
        TANK_DRIVE,
        ARCADE_DRIVE
    }

    private RobotParts parts;
    private XboxController controller;
    private SendableChooser<DriveMode> chooser;

    @Override
    public void robotInit() {

        parts = new RobotParts();
        controller = new XboxController(0);

        chooser = new SendableChooser<>();
        chooser.setDefaultOption("Tank Drive", DriveMode.TANK_DRIVE);
        chooser.addOption("Arcade Drive", DriveMode.ARCADE_DRIVE);

        SmartDashboard.putData("Drive Mode", chooser);
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

        // XBox controllers sometimes "drift", meaning the joystick will never reach
        // zero and will instead always return a small value. We use a "deadband"
        // around zero to compensate for this.
        double ly = MathUtil.applyDeadband(controller.getLeftY(), 0.05);
        double rx = MathUtil.applyDeadband(controller.getRightX(), 0.05);
        double ry = MathUtil.applyDeadband(controller.getRightY(), 0.05);

        // Full forward on the joystick is Y = -1, but negative rotation spins
        // motors backwards. So we'll just flip the Y inputs.
        ly = -ly;
        ry = -ry;

        // We very frequently don't want to spin motors at full power; this applies
        // a little bit of a limit.
        ly = ly * MAX_POWER;
        rx = rx * MAX_POWER;
        ry = ry * MAX_POWER;

        // The DifferentialDrive class will handle updating the motors for us. We
        // always want to supply a value, even if it's 0 - if the motors go too
        // long without getting an instruction then warnings will start firing.
        if (chooser.getSelected() == DriveMode.TANK_DRIVE) {
            parts.drive.tankDrive(ly, ry);
        } else {
            parts.drive.arcadeDrive(ly, rx);
        }
    }
}
