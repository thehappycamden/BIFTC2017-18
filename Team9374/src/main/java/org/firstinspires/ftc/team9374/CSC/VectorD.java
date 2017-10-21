package org.firstinspires.ftc.team9374.CSC;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by lego7_000 on 10/21/2017.
 */

public class VectorD {
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public VectorD(double xVel, double yVel, double zVel) {
        x = xVel;
        y = yVel;
        z = zVel;
    };

    public VectorD(double xVel, double yVel) {
        x = xVel;
        y = yVel;
    }

    public VectorD(double xVel) {
        x = xVel;
    }

    public VectorD(Gamepad gamepad, int stick) {
        if (stick == 0) {
            x = -gamepad.left_stick_x;
            y = -gamepad.left_stick_y;
        } else if (stick == 1) {
            x = -gamepad.right_stick_x;
            y = -gamepad.right_stick_y;
        } else if (stick == 2) {
            if (gamepad.dpad_up) {
                y = 1;
            } else if (gamepad.dpad_down) {
                y = -1;
            } else {
                y = 0;
            }
            if (gamepad.dpad_right) {
                x = 1;
            } else if (gamepad.dpad_left) {
                x = -1;
            } else {
                x = 0;
            }
        }
    }

    public VectorD() {}

    public void getController(Gamepad gamepad, int stick) {
        if (stick == 0) {
            x = -gamepad.left_stick_x;
            y = -gamepad.left_stick_y;
        } else if (stick == 1) {
            x = -gamepad.right_stick_x;
            y = -gamepad.right_stick_y;
        } else if (stick == 2) {
            if (gamepad.dpad_up) {
                y = 1;
            } else if (gamepad.dpad_down) {
                y = -1;
            } else {
                y = 0;
            }
            if (gamepad.dpad_right) {
                x = 1;
            } else if (gamepad.dpad_left) {
                x = -1;
            } else {
                x = 0;
            }
        }
    }

    public void add(VectorD vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public void sub(VectorD vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
    }

    public void set(double xVel, double yVel, double zVel) {
        x = xVel;
        y = yVel;
        z = zVel;
    };

    public void set(double xVel, double yVel) {
        x = xVel;
        y = yVel;
    }

    public void set(double xVel) {
        x = xVel;
    }

    public void set(Gamepad gamepad, int stick) {
        if (stick == 0) {
            x = -gamepad.left_stick_x;
            y = -gamepad.left_stick_y;
        } else if (stick == 1) {
            x = -gamepad.right_stick_x;
            y = -gamepad.right_stick_y;
        } else if (stick == 2) {
            if (gamepad.dpad_up) {
                y = 1;
            } else if (gamepad.dpad_down) {
                y = -1;
            } else {
                y = 0;
            }
            if (gamepad.dpad_right) {
                x = 1;
            } else if (gamepad.dpad_left) {
                x = -1;
            } else {
                x = 0;
            }
        }
    }

    public void reset() {
        x = 0;
        y = 0;
        z = 0;
    }
}
