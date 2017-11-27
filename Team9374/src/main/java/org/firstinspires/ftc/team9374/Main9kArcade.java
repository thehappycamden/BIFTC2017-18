package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team9374.CSC.Robot;
import org.firstinspires.ftc.team9374.CSC.VectorD;

/**
 * Created by lego7_000 on 10/14/2017.
 */

@TeleOp(name="9k Main Opmode - Arcade", group="9kMechanum")
public class Main9kArcade extends OpMode {
    private VectorD lStick = new VectorD();
    private VectorD rStick = new VectorD();

    public Robot robot = new Robot();

    public void init() {
        robot.init(hardwareMap, true, true, true, false);
    }

    public void loop() {
        lStick.getController(gamepad1, 0);
        rStick.getController(gamepad1, 1);

        int speed = 1;
        if (gamepad1.a) {
            speed = 1;
        } else if (gamepad1.x) {
            speed = 2;
        } else if (gamepad1.y) {
            speed = 3;
        }

        robot.speed = speed;
        robot.runMotors(robot.getMotors(0, lStick, rStick));
        robot.grasp(gamepad2.right_trigger, gamepad2.left_trigger);
        robot.lift(gamepad2);
    }
}