package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Benjamin Wilkinson on 2/4/16.
 * FTC Team 6809, FlowTech
 */

public class AutonomousBeacon extends OpMode implements SensorEventListener{
    private SensorManager SM;
    private Sensor phoneAccelerometer;
    //private Sensor phoneRotation;
    GyroSensor hiTechnicGyro;

    private float accelerationTimestamp = (float)time;
    private float[] acceleration = {0.0f,0.0f,0.0f};    // SI units (m/s^2)
    //values[0]: Acceleration minus Gx on the x-axis
    //values[1]: Acceleration minus Gy on the y-axis
    //values[2]: Acceleration minus Gz on the z-axis
    private float[] position     = {0.0f,0.0f,0.0f};    // SI units (m)
    //values[0]: Position on the x-axis
    //values[1]: Position on the y-axis
    //values[2]: Position on the z-axis

    private float rotation = 0.0f;        // NOT SI units (rad/s)
    //private float[] orientation = {0.0f,0.0f,0.0f};     // NOT: Absolute orientation,relative
                                                        // to starting position.
    DcMotor motorLeftFront;         DcMotor motorRightFront;
    DcMotor motorLeftBack;          DcMotor motorRightBack;

    DcMotor motorCollection;        DcMotor motorScoring;
    private void motorRegistration2(){
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
    }              // Must be updated independantly of TeleOp

    @Override
    public void init() {
        SM = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        phoneAccelerometer = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //phoneRotation = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        motorRegistration2();
        SM.registerListener(this, phoneAccelerometer, SM.SENSOR_DELAY_UI);  //SENSOR_DELAY_NORMAL might
        //SM.registerListener(this, phoneRotation, SM.SENSOR_DELAY_UI);       //be better for battery.
        hiTechnicGyro = hardwareMap.gyroSensor.get("gyro");
    }

    @Override
    public void init_loop() {//calibrate
        totalGyroValue();
        telemetry.addData("Welcome",     "Autonomous Program: Beacon");
        telemetry.addData("Break",       "~~~~~~~~~~~~~~~~~~~~~~~~~~");
        telemetry.addData("Description1","Starting position: ");
        telemetry.addData("Description2","Current Position: "+position[0]+","+position[1]+","+position[2]);
        telemetry.addData("Description3","Current Orientation: "+rotation);
    }

    @Override
    public void start() {
    // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
        SM.unregisterListener(this);
        SM.registerListener(this, phoneAccelerometer, SM.SENSOR_DELAY_NORMAL);  //SENSOR_DELAY_NORMAL might
        //SM.registerListener(this, phoneRotation, SM.SENSOR_DELAY_FASTEST);       //be better for battery.
    }//Change from UI to FASTEST Delay

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        SM.unregisterListener(this);
    }
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] mAccelerometer = event.values;  //Latest Sensor values
            acceleration[0] = mAccelerometer[0]; // Acceleration minus Gx on the x-axis
            acceleration[1] = mAccelerometer[1]; // Acceleration minus Gy on the y-axis
            acceleration[2] = mAccelerometer[2]; // Acceleration minus Gz on the z-axis
            position[0] += acceleration[0]*(event.timestamp-accelerationTimestamp);
            position[1] += acceleration[1]*(event.timestamp-accelerationTimestamp);
            position[2] += acceleration[2]*(event.timestamp-accelerationTimestamp);

            accelerationTimestamp = event.timestamp;
            return;
        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not sure if needed, placeholder just in case
    }//Unused
    public void moveForward(float distance)
    {
        position[0] = 0;    position[1] = 0;    position[2] = 0;
        float direction = distance/Math.abs((float)distance);
        while (position[0]<Math.abs(distance))
        {
            motorLeftFront.setPower(1.0);      motorRightFront.setPower(1.0);
            motorLeftBack.setPower(1.0);       motorRightBack.setPower(1.0);
        }
        motorLeftFront.setPower(0);     motorRightFront.setPower(0);
        motorRightBack.setPower(0);     motorRightBack.setPower(0);
    }
    void totalGyroValue()
    {

        rotation -= hiTechnicGyro.getRotation();
    }
}
