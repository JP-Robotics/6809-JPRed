package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by benjaminwilkinson on 12/8/15.
 * This will be the official program for TeleOp.
 * Major experimentation will be carried out seperately.
 */
public class TeleOp extends OpMode
{
    DcMotor motorLeftFront;         DcMotor motorRightFront;
    DcMotor motorLeftBack;          DcMotor motorRightBack;

    DcMotor motorCollection;        DcMotor motorScoring;
    public void motorRegistration(){
        /*
        * This code will prevent a missing motor from crashing the program.
        */
        try{
            motorLeftFront = hardwareMap.dcMotor.get("leftFront");
        } catch (Exception e) {telemetry.addData("ExceptionLF","DcMotor 'leftFront' is missing");motorLeftFront = null;}
        try{
            motorRightFront = hardwareMap.dcMotor.get("rightFront");
        } catch (Exception e) {telemetry.addData("ExceptionRF","DcMotor 'rightFront' is missing");}
        try{
            motorLeftBack = hardwareMap.dcMotor.get("leftBack");
        } catch (Exception e) {telemetry.addData("ExceptionLB","DcMotor 'leftBack' is missing");}
        try {
            motorRightBack = hardwareMap.dcMotor.get("rightBack");
        }catch (Exception e) {telemetry.addData("ExceptionRB","DcMotor 'rightBack' is missing");}
        //added 'Collection' motor here
        try {
            motorCollection = hardwareMap.dcMotor.get("collection");
        }catch (Exception e) {telemetry.addData("ExceptionC","DcMotor 'collection' is missing");}
        //added 'Scoring' motor here
        try {
            motorScoring = hardwareMap.dcMotor.get("scoring");
        }catch (Exception e) {telemetry.addData("ExceptionS","DcMotor 'scoring' is missing");}
        //since motors are mounted facing in, the right motors should be reversed by default
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotor.Direction.REVERSE);
        motorCollection.setDirection(DcMotor.Direction.REVERSE);
        motorScoring.setDirection(DcMotor.Direction.REVERSE);
        return;
    }       // Must update AutonomousBeacon
    public void init()
    {
        motorRegistration();
    }
    public void start()
    {
        return;
    }
    public void loop()//won't modify motor, servo, or sensor values until loop ends...same for other functions?
    {
        //This code was copied from K9TankDrive. Will be written from scratch for later programs

        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = gamepad1.left_stick_y;
        float right = gamepad1.right_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        motorLeftFront.setPower(left);          motorRightFront.setPower(right);
        motorLeftBack.setPower(left);           motorRightBack.setPower(right);

        boolean brushesAreOn = false;
        if (gamepad1.a^gamepad2.a)
        {
            brushesAreOn = true;
            motorCollection.setPower(1.0);
        }
        if (gamepad1.b^gamepad2.b)
        {
            brushesAreOn = true;
            motorCollection.setPower(-1.0);
        }
        if (brushesAreOn == false)
        {motorCollection.setPower(0.0);}

        // telemetry data
        // The string after the comma is displayed
        telemetry.addData("Header","###Proffessor Noodles###");
        telemetry.addData("Header2","\tSystem Data");
        telemetry.addData("left_Stick","Left Joystick: "+ gamepad1.left_stick_y);
        telemetry.addData("right_Stick","Right Joystick: "+ gamepad1.right_stick_y);
        telemetry.addData("Space","");
        telemetry.addData("left_Power","Left Power: "+ left);
        telemetry.addData("right_Power","Right Power: "+ right);


        boolean collectionOn = false;
        if ((gamepad1.right_trigger >= 0.75) ^ (gamepad2.right_trigger >= 0.75))
        {
            motorScoring.setPower(0.50);
            collectionOn = true;
        }
        if ((gamepad1.left_trigger >=0.75) ^ (gamepad2.left_trigger >=0.75))
        {
            motorScoring.setPower(-0.50);
            collectionOn = true;
        }
        if (collectionOn == false)
        {
            motorScoring.setPower(0.0);
        }
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
