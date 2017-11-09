package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.team9374.CSC.Robot;
import org.firstinspires.ftc.team9374.CSC.VectorD;

/**
 * Created by lego7_000 on 10/21/2017.
 */

@Autonomous(name="9kDriveAndCarry",group="9kNoSkill")
public class Auto9kDriveAndCarry extends LinearOpMode {
    private VectorD movement = new VectorD(0, -1.0);
    private VectorD rotation = new VectorD(0);

    private Robot robot = new Robot();

    private double motorSpeed = 1.0;

    public void runOpMode() {
        robot.init(hardwareMap, true, true, true);
        robot.speed = 1;
        robot.resetTimer();
        robot.grasp(1.0);
        robot.lift_manual(0, telemetry);

        waitForStart();

        robot.grasp(0.0);
        robot.lift_manual(1, telemetry);
        while (opModeIsActive() && (robot.runTime.seconds() <= 2.0)) {
            movement.set(0, motorSpeed);
            robot.runMotors(robot.getMotors(movement, rotation));
        }
        robot.grasp(1.0);
        robot.lift_manual(0, telemetry);
        movement.y = 1.0;
        while (opModeIsActive() && (robot.runTime.seconds() <= 0.2)) {
            movement.set(0, motorSpeed);
            robot.runMotors(robot.getMotors(movement, rotation));
        }
        robot.grasp(0.0);
        movement.set(0, 0);
    }
}
