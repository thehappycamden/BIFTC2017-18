package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team9374.CamdenStandClasses.Robot;
import org.firstinspires.ftc.team9374.CamdenStandClasses.VectorD;

/**
 * Created by lego7_000 on 10/21/2017.
 */

@Autonomous(name="9kDriveStraight",group="9kNoSkill")
public class Auto9kDrive extends LinearOpMode {
    private VectorD movement = new VectorD();
    private VectorD rotation = new VectorD();
    private ElapsedTime runTime = new ElapsedTime();

    private Robot robot = new Robot();

    private double motorSpeed = 1.0;

    public void runOpMode() {
        robot.init(hardwareMap);
        robot.setSpeed(1);
        waitForStart();

        while (opModeIsActive() && (runTime.seconds() < 3.0)) {
            movement.set(motorSpeed);
            robot.runMotors(robot.getMotors(0, movement, rotation));
        }

        movement.reset();
    }
}