package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team9374.CSC.Robot;
import org.firstinspires.ftc.team9374.CSC.VectorD;

/**
 * Created by lego7_000 on 10/21/2017.
 */

@Autonomous(name="9kDriveStraight",group="9kBase")
public class Auto9kDriveStraight extends LinearOpMode {
    private VectorD movement = new VectorD(0, 1.0);
    private VectorD rotation = new VectorD(0);

    private Robot robot = new Robot();

    private double motorSpeed = 0.5;

    public void runOpMode() {
        robot.init(hardwareMap, true, false, false, false);
        robot.speed = 1;
        robot.resetTimer();
        waitForStart();
        robot.runToPosition(36, motorSpeed);
    }
}