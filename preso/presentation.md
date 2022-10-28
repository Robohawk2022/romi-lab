# Robots

![](robots.jpg)

---

> We spin motors
```
motor.set(0.75)
```

---

# 2022 competition bot

[.column]

* Shooting
    * Intake wheel
    * Indexer wheel
    * Shooter wheel
* Climbing
    * Extension
    * Rotation

[.column]

* Driving
    * Front left (x2)
    * Front right (x2)
    * Back right (x2)
    * Back left (x2)

---

# Romi

![right](romi.jpg)

* Left wheel
* Right wheel

---

# It's harder than it sounds

* What does each of the controller buttons do?
* How fast should the wheels spin? For how long?
* What if it's not working right?
    * Are things wired up wrong?
    * Is the motor burned out?
    * Is it our code?

---

# Safety is critical

![left 90%](safety.png)

* Motors spin at 5000 rpm
* Robots can weigh >100 lbs
* Gears can be pinchy

---

# Programming basics

* A program is a list of instructions for the computer
    * The computer executes them in a specific order
    * They are spread out in many files
* Visual Studio Code will help us:
    * Translate it into something the computer understands
    * Run it and send instructions via wifi to the robot

---

> Enough talk! Let's play!

---

# Java

![](java.png)

---

# Variables

```java
double desiredSpeed;
boolean isMoving;

if (!isMoving) {
    desiredSpeed = 10.3;
}
	
if (desiredSpeed < 15.4) {
}
```

---

# Methods (aka functions aka procedures)

```java
public double wrapAngle(double angle) {
    if (angle > 180) {
        angle = angle - 360;
    }
    if (angle < -180) {
        angle = angle + 360;
    }
    return angle;
}

frontLeftWheelAngle = wrapAngle(frontLeftWheelAngle);
frontRightWheelAngle = wrapAngle(frontRightWheelAngle);
backRightWheelAngle = wrapAngle(backRightWheelAngle);
backLeftWheelAngle = wrapAngle(backLeftWheelAngle);
```

---

# Classes

```java
public OnOffRobot {

    boolean spinning;
    double currentSpeed;
    double maxSpeed;
    double speedIncrement;
    
    public void teleopInit() {
    }

    public void teleopPeriodic() {
    }
}
```

---

# Objects

```java
PIDController turnPid = new PIDController(1.0, 0, 0);
turnPid.enableContinuousInput(-180, 180);
turnPid.setTolerance(5.0);

CANSparkMax motor = new CANSparkMax(1, kBrushless);
motor.restoreFactoryDefaults();
motor.setIdleMode(IdleMode.kBrake);
motor.setInverted(false);
motor.setOpenLoopRampRate(0.5);
motor.setClosedLoopRampRate(0.5);
```

---

# Modifiers & Annotations

```java
public static final double WHEEL_DIAMETER_INCHES = 2.75591;

public static void main(String [] args) {
}

@Override
public void teleopPeriodic() {
}
```

---

# Code files

```
README.md                <-- this has useful stuff in it
src/main/java/frc/robot
    Main.java            <-- this is where it all begins
    RobotParts.java      <-- we will read this together
    EmptyRobot.java      <-- this is what you will work with
    examples/            <-- these are for help if we get stuck
        DualModeDrivingRobot.java
        ManualPositionControlledRobot.java
        OnOffRobot.java
        PIDPositionControlledRobot.java
        ... and so on 
```

---

# The "main loop"

* `Main.java` selects which robot class to run
* Robot is initialized (`robotInit()`)
* We will mainly be in "teleop" mode:
    * `teleopInit()      <-- this is called once`
    * `teleopPeriodic()  <-- 50x per second`
* You must supply a motor speed every time

---

# Coding problems

![](code.jpg)

---

# Spin a wheel

* Press a button and the motor spins; release it and it stops
* Variations
    * One button goes forward, one button goes backwards
    * Use a trigger instead of a button for variable speed

---

# Implement a tank drive

* Left joystick spins left wheel forward/backward
* Right joystick spins right wheel forward/backward
* Robot is stopped if you aren't pressing anything

---

# On/off buttons

* One button starts the wheel spinning, another one stops it
* Variations
    * Use the same button for on and off
    * Other buttons increase/decrease speed

---

# Hints
```
// spin a motor
parts.leftMotor.set(...)

// read the button state right now
boolean foo = controller.getAButton()

// read the trigger value right now
double foo = controller.getLeftTriggerAxis()

// print something out in the VS Code window
System.err.println("the value of foo is "+foo);

// show something in the simulator window
SmartDashboard.putNumber("Foo", 13.6);
SmartDashboard.putBoolean("Foo", true);
```

