/*
  __          __        _      _____         _____                                   
 \ \        / /       | |    |_   _|       |  __ \                                  
  \ \  /\  / /__  _ __| | __   | |  _ __   | |__) | __ ___   __ _ _ __ ___  ___ ___ 
   \ \/  \/ / _ \| '__| |/ /   | | | '_ \  |  ___/ '__/ _ \ / _` | '__/ _ \/ __/ __|
    \  /\  / (_) | |  |   <   _| |_| | | | | |   | | | (_) | (_| | | |  __/\__ \__ \
     \/  \/ \___/|_|  |_|\_\ |_____|_| |_| |_|   |_|  \___/ \__, |_|  \___||___/___/
                                                             __/ |                  
                                                            |___/                   

This robot is work in progress; it's got bugs in it. You have been warned.

 */

package frc.robot.examples;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotParts;

public class PadDrivingRobot extends TimedRobot {

    public static enum Direction {

        FORWARD, LEFT, BACKWARD, RIGHT;

        public boolean isDrive() {
            return this == FORWARD || this == BACKWARD;
        }
    }

    private RobotParts parts;
    private XboxController controller;
    private BooleanSupplier command;

    private PIDController leftDrivePid;
    private PIDController rightDrivePid;
    private double driveIncrement;
    private double driveSpeed;
    private double leftSpeed;
    private double rightSpeed;

    private PIDController turnPid;
    private double turnSpeed;
    private double targetHeading;

    @Override
    public void robotInit() {

        parts = new RobotParts();
        controller = new XboxController(0);

        leftDrivePid = new PIDController(1.0, 0, 0);
        rightDrivePid = new PIDController(1.0, 0, 0);
        driveIncrement = 12.0;
        driveSpeed = 0.8;

        turnPid = new PIDController(1.0, 0, 0);
        turnPid.enableContinuousInput(-180, 180);
        turnPid.setTolerance(5.0);
        turnSpeed = 0.8;
        targetHeading = Double.NaN;

        SmartDashboard.putData("Pad Driver", (builder) -> {
            builder.addDoubleProperty("Drive Speed", () -> driveSpeed, (v) -> driveSpeed = v);
            builder.addDoubleProperty("Drive Distance", () -> driveIncrement, (v) -> driveIncrement = v);
            builder.addDoubleProperty("Speed (Left)", () -> leftSpeed, null);
            builder.addDoubleProperty("Speed (Right)", () -> rightSpeed, null);
            builder.addDoubleProperty("Heading - Target", () -> targetHeading, null);
            builder.addDoubleProperty("Heading - Current", parts::getAngle, null);
            builder.addDoubleProperty("Turn Speed", () -> turnSpeed, (v) -> turnSpeed = v);
        });
        SmartDashboard.putData("PID - Left Drive", leftDrivePid);
        SmartDashboard.putData("PID - Right Drive", rightDrivePid);
        SmartDashboard.putData("PID - Turn", turnPid);
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

        // reset the wheel encoders and drive PIDs to 0
        parts.resetEncoders();
        leftDrivePid.reset();
        rightDrivePid.reset();

        // set the turn PID to the current angle heading
        turnPid.setSetpoint(parts.getAngle());

        // no command to fullow right now
        command = null;

        // tell the wheels to stop
        parts.stop();
    }

    @Override
    public void teleopPeriodic() {

        // If someone presses the start button, we'll stop right where we are and reset
        // everything, just like it's the beginning of the round.
        if (controller.getRawButtonPressed(5)) {
            teleopInit();
            return;
        }

        // If we're in the middle of running a command, let's do it. If it finishes,
        // reset everything and we're done. Otherwise, the command has set the motors,
        // so we don't have anything to do here.
        if (command != null) {
            if (command.getAsBoolean()) {
                teleopInit();
                return;
            }
            return;
        }

        // If we have no command, check for a button press. If there isn't one,
        // we'll just sit here in place and be done.
        Direction next = readDirection();
        if (next != null) switch (next) {

            case FORWARD:
            case BACKWARD:
                command = new DriveCommand(next == Direction.FORWARD);
                break;
        
            case LEFT:
            case RIGHT:
                command = new TurnCommand(next == Direction.RIGHT);
                break;
        }

        parts.stop();
    }

    private Direction readDirection() {
        if (controller.getYButtonPressed()) {
            return Direction.FORWARD;
        } else if (controller.getBButtonPressed()) {
            return Direction.RIGHT;
        } else if (controller.getAButtonPressed()) {
            return Direction.BACKWARD;
        } else if (controller.getXButtonPressed()) {
            return Direction.LEFT;
        }
        return null;
    }

    public class DriveCommand implements BooleanSupplier {

        public DriveCommand(boolean forward) {
            double delta = forward ? driveIncrement : -driveIncrement;
            leftDrivePid.setSetpoint(parts.leftEncoder.getDistance() + delta);
            rightDrivePid.setSetpoint(parts.leftEncoder.getDistance() + delta);
        }
            
        public boolean getAsBoolean() {

            leftSpeed = leftDrivePid.calculate(parts.leftEncoder.getDistance());
            leftSpeed = MathUtil.clamp(leftSpeed, -driveSpeed, driveSpeed);

            rightSpeed = leftDrivePid.calculate(parts.leftEncoder.getDistance());
            rightSpeed = MathUtil.clamp(rightSpeed, -driveSpeed, driveSpeed);
    
            if (leftSpeed < 0.25 || rightSpeed < 0.25) {
                return true;
            }
            
            parts.drive.tankDrive(leftSpeed, rightSpeed);
            return false;
        }
    }

    public class TurnCommand implements BooleanSupplier {

        public TurnCommand(boolean right) {
            double angleDelta = right ? -90 : 90;
            targetHeading = MathUtil.angleModulus(parts.getAngle() + angleDelta);
            turnPid.setSetpoint(targetHeading);
        }
            
        // 1 2 3 4
        // a b x y
        public boolean getAsBoolean() {

            // Determine the correct wheel speed based on the current and target distance.
            // If we're there, we're done.
            double desiredSpeed = turnPid.calculate(parts.getAngle());
            if (turnPid.atSetpoint()) {
                return true;
            }

            // Clamp the speed so we don't go too fast
            desiredSpeed = MathUtil.clamp(desiredSpeed, -turnSpeed, turnSpeed);
            leftSpeed = turnSpeed / 2;
            rightSpeed = -turnSpeed / 2;
            parts.drive.tankDrive(leftSpeed, rightSpeed);
            return false;
        }
    }
}