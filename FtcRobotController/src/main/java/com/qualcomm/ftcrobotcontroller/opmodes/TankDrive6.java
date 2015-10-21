package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by benjaminwilkinson on 10/10/15.
 * Intended for test tetrix robot to experiment with different drive trains
 * TODO: Extend exception handling to all motors
 */


public class TankDrive6 extends OpMode
{
    DcMotor motorLeftFront;         DcMotor motorRightFront;
    DcMotor motorLeftMiddle;        DcMotor motorRightMiddle;
    DcMotor motorLeftBack;          DcMotor motorRightBack;

    //constructor- What does that mean, and why does a lack of one cause errors?
    public TankDrive6()
    {}

    //All usable OpMode functions added, even if unused
    public void init()
    {
        motorLeftFront = hardwareMap.dcMotor.get("leftFront");      motorRightFront    = hardwareMap.dcMotor.get("rightFront");
        //the middle motors will not always be attatched.  Hopefully, this code will prevent this from being a fatal error.
        try {
            motorLeftMiddle = hardwareMap.dcMotor.get("leftMiddle");
        }
        catch (Exception e){telemetry.addData("ExceptionLM","DcMotor 'leftMiddle' is missing");}

        try {
            motorRightMiddle   = hardwareMap.dcMotor.get("rightMiddle");
        }
            catch (Exception e) {telemetry.addData("ExceptionRM","DcMotor 'rightMiddle' is missing");}

        motorLeftBack = hardwareMap.dcMotor.get("leftBack");        motorRightBack     = hardwareMap.dcMotor.get("rightBack");

        //since motors are mounted facing in, the right motors should be reversed, right?
        motorRightFront.setDirection(DcMotor.Direction.REVERSE);
        //motorRightMiddle.setDirection(DcMotor.Direction.REVERSE);
        motorRightBack.setDirection(DcMotor.Direction.REVERSE);
        return;
    }
    public void start()
    {
        return;
    }
    public void loop()//won't modify motor, servo, or sensor values until loop ends...same for other functions?
    {
        //This code was copied from K9TankDrive. Will be written from scratch for later programs

        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        motorLeftFront.setPower(left);          motorRightFront.setPower(right);
        //motorLeftMiddle.setPower(left);         motorRightMiddle.setPower(right);
        motorLeftBack.setPower(left);           motorRightBack.setPower(right);

        // telemetry data
        // The string after the comma is displayed
        telemetry.addData("Header","###Debug Data###");
        telemetry.addData("left_Stick","Left Joystick: "+ gamepad1.left_stick_y);
        telemetry.addData("right_Stick","Right Joystick: "+ gamepad1.right_stick_y);
        telemetry.addData("Space","");
        telemetry.addData("left_Power","Left Power: "+ left);
        telemetry.addData("right_Power","Right Power: "+ right);

        return;
    }
    public void stop()
    {
        return;
    }


    //A scalar function included in K9TankDrive. TODO: write a personal version of joystick scaler
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
