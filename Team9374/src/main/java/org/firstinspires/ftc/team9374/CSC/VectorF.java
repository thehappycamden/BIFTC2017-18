package org.firstinspires.ftc.team9374.CSC;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by lego7_000 on 10/21/2017.
 */

public class VectorF {
    public float x = 0;
    public float y = 0;
    public float z = 0;

    public VectorF(float xVel, float yVel, float zVel) {
        x = xVel;
        y = yVel;
        z = zVel;
    };

    public VectorF(float xVel, float yVel) {
        x = xVel;
        y = yVel;
    }

    public VectorF(float xVel) {
        x = xVel;
    }

    public VectorF(Gamepad gamepad, int stick) {
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
    
    public VectorF() {}
    
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

    public void add(VectorF vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public void sub(VectorF vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
    }

    public void set(float xVel, float yVel, float zVel) {
        x = xVel;
        y = yVel;
        z = zVel;
    };

    public void set(float xVel, float yVel) {
        x = xVel;
        y = yVel;
    }

    public void set(float xVel) {
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
