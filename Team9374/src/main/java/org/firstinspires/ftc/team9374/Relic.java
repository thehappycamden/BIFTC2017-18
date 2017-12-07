package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by lego7_000 on 11/29/2017.
 */

@TeleOp(name="Relic Manipulator", group = "Rev")
public class Relic extends OpMode {
    public DcMotor arm;

    public void init() {
        arm = hardwareMap.dcMotor.get("arm-motor");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void loop() {
        double speed = -gamepad1.left_stick_y/10;
        if ((arm.getCurrentPosition() <= 60 && speed < 0) || (arm.getCurrentPosition() >= 0 && speed > 0)) {
            speed = 0;
        }
        arm.setPower(speed);

        telemetry.addData("Pos:", arm.getCurrentPosition());
        telemetry.update();
    }
}
