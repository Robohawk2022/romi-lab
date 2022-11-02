package frc.robot.examples;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotParts;

/**
 * 
 * This is a free-driving robot which implements three modes of driving
 * (all of which come from the DifferentialDrive software):
 * 
 * - Tank mode: steers each wheel independently forward/back with the
 * corresponding joystick.
 * 
 * - Arcade mode: left joystick controls forward/back, right joystick controls
 * the rate of turning.
 * 
 * - Arcade mode: left joystick controls forward/back, right joystick controls
 * the curvature of the turn.
 * 
 * This example shows you the following things:
 * 
 * - We use a "SendableChooser" to put a control on the dashboard that lets us
 * select our drive mode without having to redeploy new code to the robot.
 * 
 * - We deal with the weird mapping of the Y coordinate for XBox joysticks.
 * 
 * - We put values on the smart dashboard that show what the robot is doing in
 * realtime. A couple of the values can actually be changed from the dashboard
 * to tweak behavior without having to reload the code.
 * 
 */
public class TripleModeDrivingRobot extends TimedRobot {

    public static enum DriveMode {
        TANK_DRIVE,
        ARCADE_DRIVE,
        CURVATURE_DRIVE
    }

    private RobotParts parts;
    private XboxController controller;
    private SendableChooser<DriveMode> modeChooser;
    private double maxOutput;
    private double deadband;
    private boolean squareInputs;

    @Override
    public void robotInit() {

        // Sometimes we don't want to run the motors at 100% power - it might be
        // too fast. This sets a maximum amount of power to use (1 means allow full
        // power).
        maxOutput = 0.8;

        // Joysticks sometimes have "drift", meaning they'll report a small value even
        // if they aren't being pressed. This will ignore inputs in a small radius 
        // around the center of the joystick.
        deadband = 0.02;

        // We frequently want to manipulate inputs to provide very fine-grained control
        // at low speeds. Squaring the input value from the joystick does this.
        squareInputs = true;

        parts = new RobotParts();
        controller = new XboxController(0);

        modeChooser = new SendableChooser<>();
        modeChooser.setDefaultOption("Tank Drive", DriveMode.TANK_DRIVE);
        modeChooser.addOption("Arcade Drive", DriveMode.ARCADE_DRIVE);
        modeChooser.addOption("Curvature Drive", DriveMode.CURVATURE_DRIVE);

        SmartDashboard.putData("Drive Mode", modeChooser);
        SmartDashboard.putData("Drive Properties", (builder) -> {
            builder.addDoubleProperty("Deadband", () -> deadband, (v) -> deadband = v);
            builder.addDoubleProperty("Max Output", () -> maxOutput, (v) -> maxOutput = v);
            builder.addBooleanProperty("Square Input?", () -> squareInputs, (v) -> squareInputs = v);
        });
    }

    @Override
    public void robotPeriodic() {
        parts.drive.setDeadband(deadband);
        parts.drive.setMaxOutput(maxOutput);
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

        // Full forward on the joystick is Y = -1, but negative rotation spins
        // motors backwards. So we'll just flip the Y inputs.
        double ly = -controller.getLeftY();
        double rx = controller.getRightX();
        double ry = -controller.getRightY();

        // Normally in curvature mode, you can't turn unless you're moving.
        // If you hold down the A button, we'll let you turn in place.
        boolean turnInPlace = controller.getAButton();

        // The DifferentialDrive class will handle updating the motors for us. We
        // always want to supply a value, even if it's 0 - if the motors go too
        // long without getting an instruction then warnings will start firing.
        switch (modeChooser.getSelected()) {

            case TANK_DRIVE:
                parts.drive.tankDrive(ly, ry, squareInputs);
                break;

            case ARCADE_DRIVE:
                parts.drive.arcadeDrive(ly, rx, squareInputs);
                break;
            
            case CURVATURE_DRIVE:
                parts.drive.curvatureDrive(ly, rx, turnInPlace);
                break;
        }
    }
}
