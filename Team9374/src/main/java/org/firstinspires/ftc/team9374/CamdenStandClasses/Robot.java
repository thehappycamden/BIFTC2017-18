package org.firstinspires.ftc.team9374.CamdenStandClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import static org.firstinspires.ftc.team9374.CamdenStandClasses.Spec.boolNeg;

/**
 * Created by lego7_000 on 10/14/2017.
 */

public class Robot {

    public Robot() {}

    /******************************************\
    |*****************Definitions**************|
    \******************************************/
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public int speed = 3;

    public int mode = 0;

    public void init(HardwareMap hardwareMap) {
        /******************************************\
        |*************Initializations**************|
        \******************************************/
        //Motors
        frontLeft = hardwareMap.dcMotor.get("DriveFrontLeft");
        frontRight = hardwareMap.dcMotor.get("DriveFrontRight");
        backLeft = hardwareMap.dcMotor.get("DriveBackLeft");
        backRight = hardwareMap.dcMotor.get("DriveBackRight");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        speed = 2;
    }

    public void setSpeed(int speedSetting) {
        speed = speedSetting;
    }

    public double[] getMotors(int mode, VectorD lStick, VectorD rStick) {
        double fl = 0; // Front-Left
        double fr = 0; // Front-Right
        double bl = 0; // Back-Left
        double br = 0; // Back-Right
        if (mode == 0) {
            double xVelocity = lStick.x;
            double yVelocity = lStick.y;
            double angular = rStick.x;
            fl = yVelocity - xVelocity + angular;
            fr = yVelocity + xVelocity - angular;
            bl = yVelocity + xVelocity + angular;
            br = yVelocity - xVelocity - angular;
        } else if (mode == 1) {
            fl = lStick.y + lStick.x;
            fr = rStick.y - rStick.x;
            bl = lStick.y - lStick.x;
            br = rStick.y + rStick.x;
        } else if (mode == 2) {
            fl = lStick.y * lStick.x;
            bl = lStick.y * lStick.x;
            fr = lStick.y * (-lStick.x);
            br = lStick.y * (-lStick.x);
        } else if (mode == 3) {
            fl = lStick.x;
            fr = lStick.y;
            bl = rStick.x;
            br = rStick.x;
        }
        double[] res;
        res = new double[4];
        res[0] = fl;
        res[1] = fr;
        res[2] = bl;
        res[3] = br;
        return res;
    }

    public VectorD DemonConfig(int num, Gamepad gamepad) {
        VectorD[] movement = new VectorD[2];
        movement[0] = new VectorD(boolNeg(gamepad.left_trigger, gamepad.left_bumper), boolNeg(gamepad.right_trigger, gamepad.right_bumper));
        movement[1] = new VectorD(gamepad.left_stick_y, gamepad.right_stick_y);
        return movement[num];
    }

    public void getMotors(VectorD lStick, VectorD rStick) {
        getMotors(mode, lStick, rStick);
    }

    public void runMotors(double[] motors) {
        for (int i = 0; i < motors.length; i ++) {
            motors[i] = Range.clip(motors[i], -1, 1);
        }
        frontLeft.setPower(motors[0]);
        frontRight.setPower(motors[1]);
        backLeft.setPower(motors[2]);
        backRight.setPower(motors[3]);
    }
}
