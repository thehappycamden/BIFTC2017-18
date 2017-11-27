package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team9374.CSC.Robot;
import org.firstinspires.ftc.team9374.CSC.Spec;
import org.firstinspires.ftc.team9374.CSC.VectorD;

/**
 * Created by lego7_000 on 11/10/2017.
 */

@Autonomous(name="9kDriveJewel BN", group="9kComposite")
public class Auto9kJewelDriveBN extends LinearOpMode {
    private VectorD movement = new VectorD(0, 1.0);
    private VectorD rotation = new VectorD(0);

    private Robot robot = new Robot();

    private double motorSpeed = 1.0;

    public void runOpMode() {
        robot.init(hardwareMap, true, true, true, false);
        robot.speed = 1;
        robot.resetTimer();
        waitForStart();
        double distance_to_travel = 36;
        movement.mult(motorSpeed);

        boolean isRed = robot.jewelArm(telemetry, 1);

        if (isRed) {
            distance_to_travel += 2;
            robot.runToPosition(-2, 1);
        } else {
            distance_to_travel -= 2;
            robot.runToPosition(2, 1);
        }

        robot.jewelArm(telemetry, 0);

        robot.runToPosition(distance_to_travel, 1);
    }
}