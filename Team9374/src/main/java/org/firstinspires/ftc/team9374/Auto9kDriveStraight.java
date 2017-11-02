package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team9374.CSC.Robot;
import org.firstinspires.ftc.team9374.CSC.VectorD;

/**
 * Created by lego7_000 on 10/21/2017.
 */

@Autonomous(name="9kDriveStraight",group="9kNoSkill")
public class Auto9kDriveStraight extends LinearOpMode {
    private VectorD movement = new VectorD(0, 1);
    private VectorD rotation = new VectorD(0);

    private Robot robot = new Robot();

    private double motorSpeed = 1.0;

    public void runOpMode() {
        robot.init(hardwareMap, 0);
        robot.setSpeed(1);
        robot.resetTimer();
        waitForStart();

        while (opModeIsActive() && (robot.runTime.seconds() <= 2.0)) {
            movement.set(0, motorSpeed);
            robot.runMotors(robot.getMotors(movement, rotation));
        }

        movement.set(0, 0);
    }
}
