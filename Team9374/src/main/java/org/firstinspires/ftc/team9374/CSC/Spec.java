package org.firstinspires.ftc.team9374.CSC;

/**
 * Created by lego7_000 on 10/21/2017.
 */

public class Spec {
    public static int boolNeg(int num, boolean is_neg) {
        if (is_neg != true) {
            return (-1 * num);
        } else {
            return num;
        }
    }
    public static long boolNeg(long num, boolean is_neg) {
        if (is_neg != true) {
            return -num;
        } else {
            return num;
        }
    }

    public static float boolNeg(float num, boolean is_neg) {
        if (is_neg != true) {
            return (-num);
        } else {
            return num;
        }
    }

    public static double boolNeg(double num, boolean is_neg) {
        if (is_neg != true) {
            return (-1 * num);
        } else {
            return num;
        }
    }
}
