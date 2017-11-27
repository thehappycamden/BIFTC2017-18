package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team9374.CSC.Robot;
import org.firstinspires.ftc.team9374.CSC.VectorD;

/**
 * Created by lego7_000 on 11/10/2017.
 */

@Autonomous(name="9kJewelManipulatedRed", group="9kBase")
public class Auto9kManipulateJewelsRed extends LinearOpMode {
    private VectorD movement = new VectorD(0, 1.0);
    private VectorD rotation = new VectorD(0);

    private Robot robot = new Robot();

    private double motorSpeed = 1.0;

    public void runOpMode() {
        robot.init(hardwareMap, true, true, true, false);
        robot.speed = 1;
        robot.resetTimer();
        waitForStart();

        movement.mult(motorSpeed);

        boolean isRed = robot.jewelArm(telemetry, 1);

        if (!isRed) {
            movement.mult(-1);
        }

        robot.resetTimer();
        while (robot.runTime.seconds() < 0.2) {
            robot.runMotors(robot.getMotors(movement, rotation));
        }
        movement.set(0, 0);
        robot.runMotors(robot.getMotors(movement, rotation));

        robot.jewelArm(telemetry, 0);
    }
}
