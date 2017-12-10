package org.firstinspires.ftc.team9374.CSC;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.*;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static com.sun.tools.javac.util.Constants.format;
import static org.firstinspires.ftc.team9374.CSC.Spec.boolNeg;

/**
 * Created by lego7_000 on 10/14/2017.
 */

public class Robot {

    public Robot() {}

    public boolean in_auto = false;

    /******************************************\
    |*****************Definitions**************|
    \******************************************/
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor glyphLift;
    private DcMotor relicArm;

    private Servo glyphGrabberLeft;
    private Servo glyphGrabberRight;
    private Servo jewelManipulator;
    private Servo relicGrip;
    private Servo relicLift;

    public boolean drive_enabled;
    public boolean glyph_enabled;
    public boolean jewel_enabled;

    public ColorSensor jewelColorSensor;

    //Special Values
    public int speed = 2;
    public int mode = 0;
    public int height_setting = 0; // 0= completely low, 1 = barely, 2 = 1 cube high (1/4 total?) 3 = 2 cubes high (1/1 total)
    private boolean prevStateBButton = false;

    public int TICKS_PER_REVOLUTION = 280;
    public int WHEEL_DIAMETER_IN_INCHES = 4;
    public double WHEEL_CIRCUMFERENCE_IN_INCHES = WHEEL_DIAMETER_IN_INCHES*Math.PI;
    public double TICKS_PER_INCH = TICKS_PER_REVOLUTION / WHEEL_DIAMETER_IN_INCHES;

    public VuforiaEngine vuforia;

    public ElapsedTime runTime = new ElapsedTime();

    public void init(HardwareMap hardwareMap, boolean drive, boolean glyph, boolean jewel, boolean vis) {
        drive_enabled = drive;
        glyph_enabled = glyph;
        jewel_enabled = jewel;
        /******************************************\
        |*************Initializations**************|
        \******************************************/
        //Motors
        if (drive) {
            frontLeftMotor = hardwareMap.dcMotor.get("DriveFrontLeft");
            frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRightMotor = hardwareMap.dcMotor.get("DriveFrontRight");
            frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeftMotor = hardwareMap.dcMotor.get("DriveBackLeft");
            backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRightMotor = hardwareMap.dcMotor.get("DriveBackRight");
            backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (glyph) {
            glyphLift = hardwareMap.dcMotor.get("GlyphCenter");
        }
        relicArm = hardwareMap.dcMotor.get("RelicArm");
        relicArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        relicArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Servos
        if (glyph) {
            glyphGrabberLeft = hardwareMap.servo.get("GlyphLeft");
            glyphGrabberRight = hardwareMap.servo.get("GlyphRight");
        }
        if (jewel) {
            jewelManipulator = hardwareMap.servo.get("JewelServo");
        }
        relicGrip = hardwareMap.servo.get("RelicGlyph");
        relicLift = hardwareMap.servo.get("RelicLift");
        relicLift.setPosition(1);

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
            glyphGrabberLeft.setPosition(1);
            glyphGrabberRight.setPosition(0);
        }
        if (jewel) {
            if (in_auto) {
                jewelManipulator.setPosition(0);
            } else {
                jewelManipulator.setPosition(0.25);
            }
            jewelColorSensor.enableLed(true);
        }
        speed = 2;
        mode = 0;
        if (vis) {
            vuforia = new VuforiaEngine(hardwareMap);
        }
    }

    public class VuforiaEngine {

        public VuforiaLocalizer vuforia;
        public VuforiaTrackable relicTemplate;
        public VuforiaTrackables relicTrackables;

        public VuforiaEngine(HardwareMap hardwareMap) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

            // OR...  Do Not Activate the Camera Monitor View, to save power
            // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code onthe next line, between the double quotes.
         */
            parameters.vuforiaLicenseKey = "AWfJQ6X/////AAAAGbJyF5FnvUsbtPt9EQOYHYNMNUKnV2cu28MZpZNKFdTvW6zcVwx7omhxqPMdDnwcy7yAjCyZf41jmO/8rvRlz93l64jgV1nGIQm9vxYGFnTUHweu4QklmGo7UF9hqsjCToPhT/E3jJKzwBwivjIq5X7m2QjHYl58e8FY0FrgLuijAFrGJb0JvAJ+OGuISAPnjWNzpNHG91CyHMgKf56/31ogx46FkqmUefurY0Znl8Dgt+HGQ9MVt4hRVw+9Fo+Uiy9GqzMTKCj1Nw7DjG52A9gcTuTxBSeqRtqgSwv+OB/T5j0UFBUWwjbh+k9nPFugdzYu+rb1THsrstP5w7C5SpsjQz1SFoFqnhdnxTJKSN31";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

            /**
             * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
             * in this data set: all three of the VuMarks in the game were created from this one template,
             * but differ in their instance id information.
             * @see VuMarkInstanceId
             */
            relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
            relicTemplate = relicTrackables.get(0);
            relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

            relicTrackables.activate();
        }

        public String getPattern() {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (RelicRecoveryVuMark.LEFT == vuMark) {
                return "left";
            } else if (vuMark == RelicRecoveryVuMark.CENTER) {
                return "center";
            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                return "right";
            } else {
                return "unknown";
            }
        }
    }

    public void grasp(double strength, double strength2) {
        if (glyph_enabled) {
            double leftStrength;//0+ or 0.5+
            double rightStrength;//1- or 0.5-
            if (strength2 > strength) {
                leftStrength = Range.clip(0.5 - 0.5 * strength2, 0, 1);
                rightStrength = Range.clip(0.65 + 0.5 * strength2, 0, 1);
            } else {
                leftStrength = Range.clip(0.5 + 0.5 * strength, 0, 1);
                rightStrength = Range.clip(0.65 - 0.5*strength, 0, 1);
            }
            glyphGrabberLeft.setPosition(leftStrength);
            glyphGrabberRight.setPosition(rightStrength);
        }
    }

    public void lift(Gamepad gamepad, Telemetry telemetry) {
        if (glyph_enabled) {
            double speed = -gamepad.left_stick_y;
            glyphLift.setPower(speed);
        }
    }

    public void lift_manual(int height) {
        if (glyph_enabled) {
            if (!glyphLift.isBusy()) {
                glyphLift.setPower(0);
                glyphLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            int total = 4300;
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
    }

    public void runToPosition(double distance, double speed) {
        int target = frontLeftMotor.getCurrentPosition() + (int)(distance * TICKS_PER_INCH);
        resetTimer();
        frontLeftMotor.setTargetPosition(target);
        frontRightMotor.setTargetPosition(target);
        backLeftMotor.setTargetPosition(target);
        backRightMotor.setTargetPosition(target);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        backRightMotor.setPower(speed);

        while (frontLeftMotor.isBusy()) {
            Thread.yield();
        }

        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);

        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public boolean jewelArm(Telemetry telemetry, double extension) {
        if (jewel_enabled) {
            jewelManipulator.setPosition(extension*0.75+0.25);
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

    public void relicArm(Gamepad gamepad) {
        float speed = -gamepad.right_stick_x;
        if ((relicArm.getCurrentPosition() == 60 && speed > 0) || (relicArm.getCurrentPosition() == 0 && speed < 0)) {
            speed = 0;
        }
        relicArm.setPower(speed);
        relicLift.setPosition(1-Math.abs(gamepad.right_stick_y)/2);
        if (gamepad.right_bumper) {
            relicGrip.setPosition(1);
        } else {
            relicGrip.setPosition(0);
        }
    }
}
