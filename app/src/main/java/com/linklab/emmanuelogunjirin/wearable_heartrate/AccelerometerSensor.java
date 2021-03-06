package com.linklab.emmanuelogunjirin.wearable_heartrate;

// Imports

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;

public class AccelerometerSensor extends Service implements SensorEventListener     // This initializes the accelerometer sensor.
{
    private SensorManager mSensorManager;       // Creates the sensor manager that looks into the sensor
    private PowerManager.WakeLock wakeLock;     // Creates the ability for the screen to turn on partially.
    private final Preferences Preference = new Preferences();     // Gets an instance from the preferences module.
    private final SystemInformation SystemInformation = new SystemInformation();  // Gets an instance from the system information module
    private final String Accelerometer = Preference.Accelerometer + "_" + SystemInformation.getDateStamp() + ".csv";     // This is the file name set from preferences.
    private final int MaxDataCount = Preference.AccelDataCount;        // Gets the Data count number from preferences.
    private final String Subdirectory_Accelerometer = Preference.Subdirectory_Accelerometer;       // This is where the accelerometer data is kept
    private int currentCount = 0;       // This is the initial data count for the sensor
    private StringBuilder stringBuilder;

    @SuppressLint("WakelockTimeout")        // Stops the error message from the wakelock
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)        // Establishes the sensor and the ability to collect data at the start of the data collection
    {
        Log.i("Accelerometer", "Started Accelerometer Sensor Service");     // Logs on Console.

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);    // Controls the power distribution of the system.
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AccelService:wakeLock");      // Gets partial power to run the sensor.
        wakeLock.acquire();     // Turns on the wakelock and acquires what is needed.

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);       // Initializes the ability to get a sensor from the system.
        Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);     // Gets the specific sensor called accelerometer.
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);       // It listens to the data acquires from the accelerometer
        stringBuilder = new StringBuilder();
        return START_STICKY;        // Restarts the sensor if it is killed by the system.
    }

    @Override
    public void onSensorChanged(SensorEvent event)      // This is where the data collected by the sensor is saved into a csv file which can be accessed.
    {
        Log.i("Accelerometer", "Logging to StringBuilder");     // Logs to console

        currentCount ++;
        double[] linear_accel = new double[3];      // Initializes the accelerometer value from the sensor.

        linear_accel[0] = event.values[0];     // Accelerometer value with gravity on the x-axis
        linear_accel[1] = event.values[1];     // Accelerometer value with gravity on the y-axis
        linear_accel[2] = event.values[2];     // Accelerometer value with gravity on the z-axis

        final String accelerometerValues =      // Shows the values in a string.
                SystemInformation.getTimeStamp() + "," + event.timestamp + "," +          // Starts a new string line.
                linear_accel[0] + "," +         // Acceleration value on x-axis
                linear_accel[1] + "," +         // Acceleration value on y-axis
                linear_accel[2];        // Acceleration value on z-axis

        stringBuilder.append(accelerometerValues);      // Appends the values to the string builder
        stringBuilder.append("\n");     // Makes a new line

        if ((currentCount >= MaxDataCount) && (stringBuilder != null))      // If the string builder length is thing and it is not empty
        {
            new Thread(new Runnable()       // Runs this when one or more of the values change
            {
                public void run()       // Re-runs every time.
                {
                    File accelerometer = new File(Preference.Directory + SystemInformation.Accelerometer_Path);     // Gets the path to the accelerometer from the system.
                    if (!accelerometer.exists())      // If the file exists
                    {
                        Log.i("Accelerometer", "Creating Header");     // Logs on Console.

                        DataLogger dataLogger = new DataLogger(Subdirectory_Accelerometer, Accelerometer, Preference.Accelerometer_Data_Headers);        /* Logs the Accelerometer data in a csv format */
                        dataLogger.LogData();       // Saves the data to the directory.
                    }

                    Log.i("Accelerometer", "Saving Accelerometer Sensor Service Values");     // Logs on Console.

                    DataLogger dataLogger = new DataLogger(Subdirectory_Accelerometer, Accelerometer, stringBuilder.toString());       // Logs the data into a file that can be retrieved from the watch.
                    dataLogger.LogData();       // Logs the data to a folder on the watch.
                    stringBuilder.setLength(0);     //Empties the stringBuilder before next set.
                    currentCount = 0;       // Reset the count
                }
            }).start();     // This starts the runnable thread.
        }
    }

    @Override
    public void onDestroy()     // A destroy service switch (kill switch)
    {
        Log.i("Accelerometer", "Destroying Accelerometer Sensor Service");     // Logs on Console.

        mSensorManager.unregisterListener(this);    // Kills the service that listens to the accelerometer sensor.
        wakeLock.release();     // Releases the wakelock on the service.
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)     // Ability to increase the accuracy of the sensor.
    {
        // Please do not remove this, the code needs this to function properly. Thank you :-)
    }

    @Override
    /* Unknown but necessary function */
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
