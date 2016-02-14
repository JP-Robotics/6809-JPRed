package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by benjaminwilkinson on 10/10/15.
 * Lanier Project
 */


public class LanierTeleOp extends OpMode
{
    //These statements create motor references. Name your motors carefully
    // This is what you call them when programming, not neccessarily what they are called on the phones.
    DcMotor motorLeftFront;
    DcMotor motorRightFront;
    DcMotor motorLeftBack;
    DcMotor motorRightBack;
    DcMotor motorArm;

    Servo Servozero;
    public void init()
    {
        /*
        *the same motors will not always be attached since this is a test model.
        * This code will prevent a missing motor from crashing the program.
        */
        //Lenier, you need to type the names you called the motors on the phones inside the quotes.
        //Type the name you called the motor above before the equal sign.
        //Repeat this line for all motors.
        //Below, where you see motors in the format "motorLeftBack" are my names. You need to substitute your own.
        //

        motorLeftFront = hardwareMap.dcMotor.get("DC motor_1");
        motorLeftBack = hardwareMap.dcMotor.get("DC motor_2");
        motorRightFront = hardwareMap.dcMotor.get("DC motor_3");
        motorRightBack = hardwareMap.dcMotor.get("DC motor_4");
        //try{
        /*motorArm = hardwareMap.dcMotor.get("DC motor_5");}
        catch (Exception e)
        {
            telemetry.addData("Error","Please name the arm 'DC motor_6'");
        }*/

        //Servozero = hardwareMap.servo.get("Servo 1");//from zero to one

        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorLeftBack.setDirection(DcMotor.Direction.REVERSE);
        return;
    }

    public void loop()
    {
        //This code was copied from K9TankDrive. Modified for the Jackson Prep Robotics Team.

        //Lenier, this is the portion that makes it move. Replace the motor names, and ".setPower"
        //Either left for right, referring to the side of the robot the motor is driveing.
        //Your joysticks work like a tank drive, powering one side or the other.
        //I will help you some more on this this evening.

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
        motorLeftFront.setPower(left);
        motorRightFront.setPower(right);
        motorLeftBack.setPower(left);
        motorRightBack.setPower(right);
        //motorArm.setPower(gamepad2.right_stick_y/2);

        /*if (gamepad2.right_trigger >= 0.75)
        {
            Servozero.setPosition(0.0);
        }
        else
        {
            Servozero.setPosition(1.0);
        }*/

        // telemetry data
        // The string after the comma is displayed

        //Leneir, Ignore this for now. I will expain this evening.
        telemetry.addData("Header","###Debug Data###");
        telemetry.addData("left_Stick","Left Joystick: "+ gamepad1.left_stick_y);
        telemetry.addData("right_Stick","Right Joystick: "+ gamepad1.right_stick_y);
        telemetry.addData("Space","");
        telemetry.addData("left_Power","Left Power: "+ left);
        telemetry.addData("right_Power","Right Power: "+ right);

        return;
    }



    //This code is used above. Ignore it for now.
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
