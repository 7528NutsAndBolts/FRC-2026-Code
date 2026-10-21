// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.commands.AutoCommands;

// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.Intake;

// /* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
// public class AutoIntakeDisengage extends Command {
//   private Intake intake;
//   private int target = 0; // # of cycles
//   private int count = 0;  // current # of cycles


//   public AutoIntakeDisengage(Intake intake, double seconds) {
//     target = (int)(seconds * 50);   //50 cycles per second apparently
//     this.intake  = intake;
//     addRequirements(this.intake);
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {}

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
//     if(count < target) {
//       intake.RotateIntake(-0.5);
//     }
//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {}

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }
