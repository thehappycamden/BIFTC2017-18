package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team9374.CSC.Robot;
import org.firstinspires.ftc.team9374.CSC.VectorD;

/**
 * Created by lego7_000 on 10/14/2017.
 */

@TeleOp(name="9k Main Opmode - Tank", group="9kMechanum")
public class Main9kTank extends OpMode {
    private VectorD lStick = new VectorD();
    private VectorD rStick = new VectorD();

    public Robot robot = new Robot();

    public void init() {
        robot.init(hardwareMap, true, true, true);
    }

    public void loop() {
        lStick.getController(gamepad1, 0);
        rStick.getController(gamepad1, 1);

        robot.grasp(gamepad2.right_trigger);
        robot.lift(gamepad2);

        int speed = 2;
        if (gamepad1.a) {
            speed = 1;
        } else if (gamepad1.x) {
            speed = 2;
        } else if (gamepad1.y) {
            speed = 3;
        }

        robot.speed = speed;
        robot.runMotors(robot.getMotors(1, lStick, rStick));
    }
}
