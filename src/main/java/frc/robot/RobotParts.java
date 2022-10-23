package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.romi.RomiGyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * This represents the various parts of the Romi that we're going to be interacting with.
 * 
 * Notice that we use information about the encoders and the size of the wheels to make
 * sure that the encoders are returning us the distance the wheel has travelled in inches.
 * This helps when we want to drive the robot a particular distance.
 * 
 */
public class RobotParts {

    // How many times does each wheel's encoder pulse during a single revolution?
    public static final double PULSES_PER_REV = 1440.0;

    // How big are the wheels?
    public static final double WHEEL_DIAMETER_INCHES = 2.75591;

    // How far has the wheel travelled during a single pulse of the encoder?
    public static final double DISTANCE_PER_PULSE = (Math.PI * WHEEL_DIAMETER_INCHES) / PULSES_PER_REV;

    public final Spark leftMotor;
    public final Spark rightMotor;
    public final Encoder leftEncoder;
    public final Encoder rightEncoder;
    public final DifferentialDrive drive;
    public final RomiGyro gyro;

    public RobotParts() {

        // Sets up the left motor. Notice that we use DISTANCE_PER_PULSE so that
        // the encoder will record distance travelled in inches.
        leftMotor = new Spark(0);
        leftEncoder = new Encoder(4, 5);
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        leftEncoder.reset();

        // Sets up the right motor. Notice that this one is inverted, because it's
        // mounted backwards compared to the left wheel.
        rightMotor = new Spark(1);
        rightMotor.setInverted(true);
        rightEncoder = new Encoder(6, 7);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        rightEncoder.reset();

        drive = new DifferentialDrive(leftMotor, rightMotor);

        gyro = new RomiGyro();
        gyro.reset();

        SmartDashboard.putData("Robot Parts", (builder) -> {
            builder.addDoubleProperty("Encoder Left (in)", leftEncoder::getDistance, null);
            builder.addDoubleProperty("Encoder Right (in)", rightEncoder::getDistance, null);
            builder.addDoubleProperty("Gyro Angle (deg)", gyro::getAngle, null);
            builder.addDoubleProperty("Gyro Rate (deg/sec)", gyro::getRate, null);
        });
    }

    /**
     * Convenience method that resets both encoders at the same time.
     */
    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    /**
     * Convenience method that stops the robot.
     */
    public void stop() {
        drive.stopMotor();
    }

    /**
     * Convenience method that drives the robot forward with the same speed on both wheels.
     */
    public void drive(double speed) {
        drive.tankDrive(speed, speed);
    }
}
