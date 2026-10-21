package frc.robot.commands;

import static edu.wpi.first.units.Units.*;

import javax.naming.NameNotFoundException;

import org.w3c.dom.views.DocumentView;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.lemonlight.lemonlightuno;
import frc.robot.subsystems.lemonlight.lemonlightuno.NoSuchTargetException;
import frc.robot.subsystems.lemonlight.LimelightHelpers;
import frc.robot.subsystems.lemonlight.lemonlightdos;
import frc.robot.subsystems.lemonlight.LimelightHelpers.RawFiducial;
import edu.wpi.first.math.controller.PIDController;
class PIDControllerConfigurable extends PIDController {
  public PIDControllerConfigurable(double kP, double kI, double kD) {
      super(kP, kI, kD);
  }
  
  public PIDControllerConfigurable(double kP, double kI, double kD, double tolerance) {
      super(kP, kI, kD);
      this.setTolerance(tolerance);
  }
}
public class Align extends Command {
  private final CommandSwerveDrivetrain m_drivetrain;
  private final lemonlightuno m_Limelight;

  private static final PIDControllerConfigurable rotationalPidController = new PIDControllerConfigurable(0.05000, 0.000000, 0.001000, 0.01);
  private static final PIDControllerConfigurable xPidController = new PIDControllerConfigurable(0.400000, 0.000000, 0.000600, 0.01);
  private static final PIDControllerConfigurable yPidController = new PIDControllerConfigurable(0.3, 0, 0, 0.3);
  private static final SwerveRequest.RobotCentric alignRequest = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
  private static final SwerveRequest.Idle idleRequest = new SwerveRequest.Idle();
  
  public double rotationalRate = 0;
  public double velocityX = 0;
  // private static final SwerveRequest.SwerveDriveBrake brake = new
  // SwerveRequest.SwerveDriveBrake();
public boolean redTeam; 
public int goalTag;
  

  public Align(CommandSwerveDrivetrain drivetrain, lemonlightuno limelight, boolean redTeam) {
    this.m_drivetrain = drivetrain;
    this.m_Limelight = limelight;
    addRequirements(m_Limelight);
    this.redTeam = redTeam;
    if (this.redTeam) {
        this.goalTag = 9;
    } else {
        this.goalTag = 9;
    }
    
  }


public RawFiducial getFiducialWithId(int id, RawFiducial[] fiducials) throws NameNotFoundException {// Debug
    // https://github.com/LSRobotics/2025Robot/blob/2e593b524d59a1dbb6f38d302ac03bd51ced3021/src/main/java/frc/robot/subsystems/VisionSubsystem.java#L104
    StringBuilder availableIds = new StringBuilder();

    for (RawFiducial fiducial : fiducials) {
      if (availableIds.length() > 0) {
        availableIds.append(", ");
      } // Error reporting
      availableIds.append(fiducial.id);

      if (fiducial.id == id) {
        return fiducial;
      }
    }
    throw new NameNotFoundException("Cannot find: " + id + ". IN view:: " + availableIds.toString());
  }



  @Override
  public void initialize() {
    
  }
  
  
  @Override
  public void execute() {
    
    RawFiducial fiducial;
    RawFiducial[] fiducials = LimelightHelpers.getRawFiducials("");

    try {
      fiducial = this.getFiducialWithId(this.goalTag, fiducials);

      rotationalRate = rotationalPidController.calculate(2*fiducial.txnc, 0.0) * 0.75* 0.9;
      
      final double velocityX = xPidController.calculate(fiducial.distToRobot, 0.1) * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond) * 0.7;
        
      if (rotationalPidController.atSetpoint() && xPidController.atSetpoint()) {
        this.end(true);
      }

      SmartDashboard.putNumber("txnc", fiducial.txnc);
      SmartDashboard.putNumber("distToRobot", fiducial.distToRobot);
      SmartDashboard.putNumber("rotationalPidController", rotationalRate);
      SmartDashboard.putNumber("xPidController", velocityX);
      m_drivetrain.setControl(
          alignRequest.withRotationalRate(-rotationalRate).withVelocityX(-velocityX));//.withVelocityY(velocityY));
      // drivetrain.applyRequest(() -> alignRequest.withRotationalRate(0.5 *
      // MaxAngularRate)
      // .withVelocityX(xPidController.calculate(0.2 * MaxSpeed)));
      // drivetrain.setControl(brake);
    } catch (NameNotFoundException nste) { 
      System.out.println("No apriltag found");
      if ((rotationalRate != 0) && (velocityX != 0)){
        m_drivetrain.setControl(
          alignRequest.withRotationalRate(-rotationalRate).withVelocityX(-velocityX));//.withVelocityY(velocityY));
        }
      }
      
    }
  

  @Override
  public boolean isFinished() {
    return rotationalPidController.atSetpoint() && xPidController.atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    m_drivetrain.applyRequest(() -> idleRequest);
    
  }
}

