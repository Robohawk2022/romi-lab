Exercises

- make it drive
- make one wheel spin when you push a button
- make it start up slower
- make both wheels spin in the same direction
- click to turn wheels on/off
- increase/decrease speed
- click to start/stop rotating wheels at 30 rpm
- click to rotate wheels through 5 revolutions

Notes

  private static final double kCountsPerRevolution = 1440.0;
  private static final double kWheelDiameterInch = 2.75591; // 70 mm

  leftEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);
  rightEncoder.setDistancePerPulse((Math.PI * kWheelDiameterInch) / kCountsPerRevolution);

  rightMotor.setInverted(true);
