// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
// MOTORS
  private TalonFX intakeR = new TalonFX(IntakeConstants.INTAKE_ROTATE_MOTOR_ID);
  private TalonFX intakeS = new TalonFX(IntakeConstants.INTAKE_SPIN_MOTOR_ID);

  public Intake() {
    //CONFIGURE
    intakeR.getConfigurator().apply(IntakeConstants.configs);
    intakeS.getConfigurator().apply(IntakeConstants.configs);
    intakeR.getConfigurator().refresh(IntakeConstants.currentLimits);
    intakeS.getConfigurator().refresh(IntakeConstants.currentLimits);
    intakeR.getConfigurator().apply(IntakeConstants.currentLimits);
    intakeS.getConfigurator().apply(IntakeConstants.currentLimits);
  }
  // takes in balls
  public void TakeIn(double speed) {
  intakeS.set(speed);
  }
  
  // sets the intake up and down
  public void RotateIntake(double speed) {
  intakeR.set(speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake Output% ", intakeS.get());
    SmartDashboard.putNumber("Intake Rotate% ", intakeR.get());
  }
}
