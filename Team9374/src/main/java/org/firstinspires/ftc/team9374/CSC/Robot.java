package org.firstinspires.ftc.team9374.CSC;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.team9374.CSC.Spec.boolNeg;

/**
 * Created by lego7_000 on 10/14/2017.
 */

public class Robot {

    public Robot() {}

    /******************************************\
    |*****************Definitions**************|
    \******************************************/
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor glyphLift;

    private Servo glyphGrabberLeft;
    private Servo glyphGrabberRight;
    private Servo jewelManipulator;

    public boolean drive_enabled;
    public boolean glyph_enabled;
    public boolean jewel_enabled;

    public ColorSensor jewelColorSensor;

    //Special Values
    public int speed = 2;
    public int mode = 0;
    public int height_setting = 0; // 0= completely low, 1 = barely, 2 = 1 cube high (1/4 total?) 3 = 2 cubes high (1/1 total)

    public ElapsedTime runTime = new ElapsedTime();

    public void init(HardwareMap hardwareMap, boolean drive, boolean glyph, boolean jewel) {
        drive_enabled = drive;
        glyph_enabled = glyph;
        jewel_enabled = jewel;
        /******************************************\
        |*************Initializations**************|
        \******************************************/
        //Motors
        if (drive) {
            frontLeftMotor = hardwareMap.dcMotor.get("DriveFrontLeft");
            frontRightMotor = hardwareMap.dcMotor.get("DriveFrontRight");
            backLeftMotor = hardwareMap.dcMotor.get("DriveBackLeft");
            backRightMotor = hardwareMap.dcMotor.get("DriveBackRight");
        }
        if (glyph) {
            glyphLift = hardwareMap.dcMotor.get("GlyphCenter");
        }

        //Servos
        if (glyph) {
            glyphGrabberLeft = hardwareMap.servo.get("GlyphLeft");
            glyphGrabberRight = hardwareMap.servo.get("GlyphRight");
        }
        if (jewel) {
            jewelManipulator = hardwareMap.servo.get("JewelServo");
        }

        //Color
        if (jewel) {
            jewelColorSensor = hardwareMap.colorSensor.get("JewelColor");
        }
        if (drive) {
            frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
            backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        }
        if (glyph) {
            glyphLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            glyphLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            glyphGrabberLeft.setPosition(0.65);
            glyphGrabberRight.setPosition(0.45);
        }
        if (jewel) {
            jewelManipulator.setPosition(0);
            jewelColorSensor.enableLed(true);
        }
        speed = 2;
        mode = 0;
    }

    public void grasp(double strength) {
        if (glyph_enabled) {
            glyphGrabberLeft.setPosition(0.5 + 0.5 * strength);
            glyphGrabberRight.setPosition(0.5 - 0.5 * strength);
        }
    }

    public void lift(Gamepad gamepad) {
        if (glyph_enabled) {
            if (!glyphLift.isBusy()) {
                glyphLift.setPower(0);
                glyphLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            double amount = gamepad.left_trigger;
            int total = 3900;
            int distance = 0;
            if (gamepad.b) {
                height_setting++;
            }
            if (height_setting >= 4 || gamepad.left_trigger > 0.5) {
                height_setting = 0;
            }
            switch (height_setting) {
                case 0:
                    distance = 0;
                    break;
                case 1:
                    distance = total / 4;
                    break;
                case 2:
                    distance = total / 2 + 250;
                    break;
                case 3:
                    distance = total;
                    break;
            }
        /*if (gamepad.b) {
            /**int distance = (int) (amount * GLYPH_LIFT_RATIO * COUNTS_PER_MOTOR_REV);
             telemetry.addData("Position", glyphLift.getCurrentPosition());
             telemetry.addData("Target", distance);
             telemetry.update();
             glyphLift.setTargetPosition(distance);
        }*/
            glyphLift.setTargetPosition(distance);
            glyphLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            glyphLift.setPower(1.0);
        }
    }

    public void lift_manual(int height) {
        if (glyph_enabled) {
            if (!glyphLift.isBusy()) {
                glyphLift.setPower(0);
                glyphLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            int total = 3900;
            int distance = 0;
            height_setting = height;
            switch (height_setting) {
                case 0:
                    distance = 0;
                    break;
                case 1:
                    distance = total / 4;
                    break;
                case 2:
                    distance = total / 2 + 250;
                    break;
                case 3:
                    distance = total;
                    break;
            }
            glyphLift.setTargetPosition(distance);
            glyphLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            glyphLift.setPower(1.0);
        }
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
            double xVelocity = rStick.x;
            double yVelocity = -lStick.y;
            double angular = rStick.x;
            fl = yVelocity - xVelocity + angular;
            fr = yVelocity + xVelocity - angular;
            bl = yVelocity + xVelocity + angular;
            br = yVelocity - xVelocity - angular;
        } else if (mode == 3) {
            fl = lStick.x;
            fr = lStick.y;
            bl = rStick.x;
            br = rStick.y;
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

    public double[] getMotors(VectorD lStick, VectorD rStick) {
        return getMotors(mode, lStick, rStick);
    }

    public void runMotors(double[] motors) {
        if (drive_enabled) {
            for (int i = 0; i < motors.length; i++) {
                motors[i] = Range.clip(motors[i]/speed, -1, 1);
            }
            frontLeftMotor.setPower(motors[0]);
            frontRightMotor.setPower(motors[1]);
            backLeftMotor.setPower(motors[2]);
            backRightMotor.setPower(motors[3]);
        }
    }

    public void resetTimer() {
        runTime.reset();
        runTime.startTime();
    }

    /**public void runToPosition(VectorD distance, double speed) {
        int target = frontLeftMotor.getCurrentPosition() + (int)(distance.y * COUNTS_PER_INCH);
        resetTimer();
        frontLeftMotor.setTargetPosition(target);
        frontRightMotor.setTargetPosition(target);
        backLeftMotor.setTargetPosition(target);
        backRightMotor.setTargetPosition(target);

        frontLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        backRightMotor.setPower(speed);

        while (frontLeftMotor.isBusy()) {

        }

        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
    }*/

    public boolean jewelArm(Telemetry telemetry, double extension) {
        if (jewel_enabled) {
            jewelManipulator.setPosition(extension);
            ElapsedTime timeToExtend = new ElapsedTime();
            while (timeToExtend.seconds() < 1) {}
            int red = jewelColorSensor.red();
            int green = jewelColorSensor.green();
            int blue = jewelColorSensor.blue();
            int alpha = jewelColorSensor.alpha();
            String color;
            boolean isRed;
            if (red > blue) {
                color = "Red";
                isRed = true;
            } else if (blue > red) {
                color = "Blue";
                isRed = false;
            } else {
                color = "None";
                isRed = false;
            }
            telemetry.addData("Color", color);
            telemetry.addData("R", red);
            telemetry.addData("G", green);
            telemetry.addData("B", blue);
            telemetry.addData("A", alpha);

            telemetry.update();

            return isRed;
        } else {return false;}
    }
}
