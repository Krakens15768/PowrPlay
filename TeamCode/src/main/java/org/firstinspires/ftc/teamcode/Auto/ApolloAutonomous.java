package org.firstinspires.ftc.teamcode.Auto;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.ApolloHardware;

@Autonomous(name = "Apollo Autonomous", group = "Apollo")
public class ApolloAutonomous extends LinearOpMode {
    ApolloHardware robot = new ApolloHardware();

    private ElapsedTime runtime = new ElapsedTime();

    static final double CENTIMETERS_TO_INCHES = 2.54;

    static final double COUNTS_PER_MOTOR_REV    = (134.4 * 4); // PPR is 134.4; CPR = PPR * 4
    static final double DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.7;
    static final double     TURN_SPEED              = 0.6;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();

        encoderDrive(DRIVE_SPEED, 100 * CENTIMETERS_TO_INCHES);
        encoderTurn(TURN_SPEED, 90);
        encoderDrive(DRIVE_SPEED, 500 * CENTIMETERS_TO_INCHES);
    }

    public void encoderDrive(double speed, double inches) {
        int target;

        if (opModeIsActive()) {
            target = getCurrentRobotPosition() + (int)(inches * COUNTS_PER_INCH);
            robot.setDriveTarget(target);

            robot.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.setDrivePower(Math.abs(speed));

            Log.d(robot.TAG, "Started running to " + target);

            while (opModeIsActive() && robot.amountOfMotorsBusy()) {
                telemetry.addData("Running to",  " %7d", target);
                telemetry.addData("Currently at", " %7d",  getCurrentRobotPosition());
                telemetry.update();
            }

            robot.setDrivePower(0);

            robot.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

            Log.d(robot.TAG, "Done running to " + target);

            sleep(100);
        }
    }

    public void encoderTurn(double speed, double degrees) {

    }

    public int getCurrentRobotPosition() {
        int average;

        // Right-Back motor currently does not work
        average = robot.LeftFront.getCurrentPosition()
                + robot.LeftBack.getCurrentPosition()
                + -robot.RightFront.getCurrentPosition()
                / 3;

        return average;
    }
}
