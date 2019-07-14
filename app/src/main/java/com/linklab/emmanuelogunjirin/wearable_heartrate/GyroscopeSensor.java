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

public class GyroscopeSensor extends Service implements SensorEventListener     // This initializes the Gyroscope sensor.
{
    private SensorManager mSensorManager;       // Creates the sensor manager that looks into the sensor
    private PowerManager.WakeLock wakeLock;     // Creates the ability for the screen to turn on partially.
    private final Preferences Preference = new Preferences();     // Gets an instance from the preferences module.
    private final SystemInformation SystemInformation = new SystemInformation();  // Gets an instance from the system information module
    private final String Gyroscope = Preference.Gyroscope + "_" + SystemInformation.getDateStamp() + ".csv";     // This is the file name set from preferences.
    private final int MaxDataCount = Preference.GyDataCount;        // Gets the Data count number from preferences.
    private final String Subdirectory_Gyroscope = Preference.Subdirectory_Gyroscope;       // This is where the Gyroscope data is kept
    private int currentCount = 0;       // This is the initial data count for the sensor
    private StringBuilder stringBuilder;

    @SuppressLint("WakelockTimeout")        // Stops the error message from the wakelock
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)        // Establishes the sensor and the ability to collect data at the start of the data collection
    {
        Log.i("Gyroscope", "Started Gyroscope Sensor Service");     // Logs on Console.

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);    // Controls the power distribution of the system.
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GyService:wakeLock");      // Gets partial power to run the sensor.
        wakeLock.acquire();     // Turns on the wakelock and acquires what is needed.

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);       // Initializes the ability to get a sensor from the system.
        Sensor mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);     // Gets the specific sensor called Gyroscope.
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_UI);       // It listens to the data acquires from the Gyroscope
        stringBuilder = new StringBuilder();
        return START_STICKY;        // Restarts the sensor if it is killed by the system.
    }

    @Override
    public void onSensorChanged(SensorEvent event)      // This is where the data collected by the sensor is saved into a csv file which can be accessed.
    {
        Log.i("Gyroscope", "Logging to StringBuilder");     // Logs to console

        currentCount ++;
        double[] linear_Gyroscope = new double[3];      // Initializes the Gyroscope value from the sensor.

        linear_Gyroscope[0] = event.values[0];     // Gyroscope value with gravity on the x-axis
        linear_Gyroscope[1] = event.values[1];     // Gyroscope value with gravity on the y-axis
        linear_Gyroscope[2] = event.values[2];     // Gyroscope value with gravity on the z-axis

        final String gyroscopeValues =      // Shows the values in a string.
                SystemInformation.getTimeStamp() + "," + event.timestamp + "," +          // Starts a new string line.
                        linear_Gyroscope[0] + "," +         // Rotation value on x-axis
                        linear_Gyroscope[1] + "," +         // Rotation value on y-axis
                        linear_Gyroscope[2];        // Rotation value on z-axis

        stringBuilder.append(gyroscopeValues);      // Appends the values to the string builder
        stringBuilder.append("\n");     // Makes a new line

        if ((currentCount >= MaxDataCount) && (stringBuilder != null))      // If the string builder length is thing and it is not empty
        {
            new Thread(new Runnable()       // Runs this when one or more of the values change
            {
                public void run()       // Re-runs every time.
                {
                    File Gyroscopes = new File(Preference.Directory + SystemInformation.Gyrosope_Path);     // Gets the path to the Gyroscope from the system.
                    if (!Gyroscopes.exists())      // If the file exists
                    {
                        Log.i("Gyroscope", "Creating Header");     // Logs on Console.

                        DataLogger dataLogger = new DataLogger(Subdirectory_Gyroscope, Gyroscope, Preference.Accelerometer_Data_Headers);        /* Logs the Gyroscope data in a csv format */
                        dataLogger.LogData();       // Saves the data to the directory.
                    }

                    Log.i("Gyroscope", "Saving Gyroscope Sensor Service Values");     // Logs on Console.

                    DataLogger dataLogger = new DataLogger(Subdirectory_Gyroscope, Gyroscope, stringBuilder.toString());       // Logs the data into a file that can be retrieved from the watch.
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
        Log.i("Gyroscope", "Destroying Gyroscope Sensor Service");     // Logs on Console.

        mSensorManager.unregisterListener(this);    // Kills the service that listens to the Gyroscope sensor.
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
