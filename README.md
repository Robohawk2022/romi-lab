# What's this?

Code for a two-day programming lab using Romi robots.

# Exercises

## Get the Romi robot working

Using `Main.java`, make sure the robot being initialized is the `DualModeDrivingRobot`.
Run the simulator from VSCode, switch to Teleop mode, and enjoy driving around the
robot. 

Things to mess around with:
* Experiment with switching the drive modes to see what happens.
* Try to drive your robot straight for a ways and see if it drifts to one side.
    - Why do you think that's happening? 
    - How do you think you could fix it?

## Build your own drive code

Write your own code to read controller inputs and apply logic to spin the
motors. Make a copy of `EmptyRobot.java` and write some code that will move
the robot.

Suggestion - start small. See if you can drive the robot forward
when a button is being held down. Get that working and build up from there.

Things to mess around with:
* See if you can drive the robot forward at 50% while someone is holding a button
* See if you can drive it backwards at 50% if a different button is held down
* Read a joystick input and use it to drive at variable speed forward/backward
* Implement a tank drive:
    - Left joystick forward/backward spins the left wheel forward/backward
    - Right joystick forward/backward spins the left wheel forward/backward

*Code snippets*

You can determine if a button is pressed like so:

    // This will execute if someone is holding down the A button
    if (controller.getAButton()) {
        // do something
    }

You can drive the motor to a certain power level using code like so:

    // Drives the left motor forward at 50% power. Supplying 0 will
    // stop the motor, supplying 1 will drive it at 100% power.
    parts.leftMotor.drive(0.5);

You can read the position of a joystick like so:

    // Gets the forward/backward position. -1 is fully forward, 
    // 1 is fully backward, 0 is in the middle.
    double leftY = controller.getLeftY();

    // Gets the left/right position. -1 is fully left, 1 is fully
    // right, 0 is in the middle.
    double leftX = controller.getLeftX();

# Build an on/off switch

Many of the systems on a real robot turn on and off with button presses.
Make a copy of `EmptyRobot.java` and write some code that will start and
stop the wheels with a button press.

Things to mess around with:
* Use two buttons for an on/off:
    - A button starts the wheels spinning forward at 50% power
    - B button stops the wheels spinning
* Use a single button:
    - A button starts the wheels spinning forward at 50% power if they're stopped
    - A button stops the wheels if they are spinning
* Variable speed:
    - B button starts/stops the wheels at 50% power
    - Y button increases speed by 10% power
    - A button decreases speed by 10% power

*Code snippets*

A `boolean` is a true/false value.

    // Declares a boolean, sets it to false
    boolean foo = false;

    // Sets the boolean to a specific value
    foo = true;
    foo = false;

    // Sets the boolean to the opposite of its current value
    foo = !foo;

    // Does something if the boolean is true
    if (foo) {

    }

    // Does something if the boolean is false
    if (!foo) {

    }

A `double` is a number value.

    // Declares a double, sets it to 0.3
    double foo = 0.3;

    // Sets a double
    foo = 0.5;

    // Increases/decreases a double by a fixed amount
    foo = foo + 0.1;
    foo = foo - 0.1;

    // Increases/decreases a double by 10%
    foo = foo * 1.1;
    foo = foo * 0.9;

    // Do something if a double is above a certain amount
    if (foo > 0.5) {
        // do something
    }

# Drive to a fixed position

For some systems (like 2022's indexer or climbing arms), we want to turn a motor a
specific number of times and then stop. Make a copy of `EmptyRobot.java` and write
some code that will move the robot forward a specific amount.

To do this, you will need the Encoders. These are components attached to each motor
that measure its spin. They are initialized to 0 when the robot starts up. The
code in `RobotParts` sets it up so they will measure the distance the robot has
travelled in inches.

Things to mess around with:
* Spin the wheels forward, and check out the distance travelled on the dashboard
(it's under "Robot Parts / Encoder Left").
* Spin the wheels forward, and stop when the robot has travelled 4 feet. How close
can you get to your target?
* Every time someone pushes the A button, move the robot forward 12 inches. How close
can you get to your target?

### Code snippets

This will read how far each wheel has travelled in inches since the robot was
turned on.

    double leftPosition = parts.leftEncoder.getDistance();
    double rightPosition = parts.rightEncoder.getDistance();

This will reset the distance counter to 0.

    parts.resetEncoders();

This will do something once the left wheel has travelled more than 12 inches from when
it was turned on.

    if (parts.leftEncoder.getDistance() > 12) {
        // Do something
    }

This will do something once the left wheel is within one inch of a target position.

    double delta = parts.leftEncoder.getDistance() - targetDistance;
    if (Math.abs(delta) < 1) {
        // Do something
    }

# Controller Mapping

| Axis          | Port |
| ============= | ==== |
| Left X        |    0 |
| Left Y        |    1 |
| Left Trigger  |    2 |
| Right Trigger |    3 |
| Right X       |    4 |
| Right Y       |    5 |

| Button        | Port |
| ============= | ==== |
| A             |    1 |
| B             |    2 |
| X             |    3 |
| Y             |    4 |
| Left Bumper   |    5 |
| Right Bumper  |    6 |
| Back          |    7 |
| Start         |    8 |
| Left Stick    |    9 |
| Right Stick   |   10 |
