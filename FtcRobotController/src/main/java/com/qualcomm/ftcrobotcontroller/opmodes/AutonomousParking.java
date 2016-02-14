package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by benjaminwilkinson on 1/9/16.
 */
public class AutonomousParking extends LinearOpMode {
    DcMotor motorLeftFront;         DcMotor motorRightFront;
    DcMotor motorLeftBack;          DcMotor motorRightBack;

    DcMotor motorCollection;        DcMotor motorScoring;

    void moveLeft (double power)
    {
        motorLeftBack.setPower(power);
        motorLeftFront.setPower(power);
    }
    void moveRight (double power)
    {
        motorRightBack.setPower(power);
        motorRightFront.setPower(power);
    }
    void moveForward (double power)
    {
        moveRight(power);
        moveLeft(power);
    }
    @Override
    public void runOpMode() throws InterruptedException {
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

        waitForStart();
        sleep(15000);
        //Move in front of ramp
        moveForward(1.0);
        sleep(8000);
        moveForward(0.0);
    }
}
