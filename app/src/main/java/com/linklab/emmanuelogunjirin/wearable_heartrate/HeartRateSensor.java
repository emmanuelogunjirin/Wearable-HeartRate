package com.linklab.emmanuelogunjirin.wearable_heartrate;

// Imports

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

public class HeartRateSensor extends Service implements SensorEventListener     // This is the file heading, it listens to the physical Heart Rate Senor
{
    private final Preferences Preference = new Preferences();     // Gets an instance from the preferences module.
    private final com.linklab.emmanuelogunjirin.wearable_heartrate.SystemInformation SystemInformation = new SystemInformation();  // Gets an instance from the system information module
    private SensorManager mSensorManager;       // Creates the sensor manager that looks into the sensor
    private final String Heart_Rate = Preference.Heart_Rate;     // This is the file name set from preferences.
    private final String Subdirectory_Heartrate = Preference.Subdirectory_HeartRate;      // This is where the Heartrate data is kept

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)    /* Establishes the sensor and the ability to collect data at the start of the data collection */
    {
        CheckFiles();       // Checks that the files needed are present

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);       // Starts the sensor service for any sensor in the system.
        Sensor mHeartRate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);        // Makes sure it is for the Heart Rate sensor.
        mSensorManager.registerListener(this, mHeartRate, SensorManager.SENSOR_DELAY_FASTEST);      // Registers the listener for the HR sensor in the system.

        return START_NOT_STICKY;        // Makes sure the timer is started again if it is killed.
    }

    private void CheckFiles()
    {
        File HRSensors = new File(Preference.Directory + SystemInformation.Heart_Rate_Path);     // Gets the path to the Sensors from the system.
        if (!HRSensors.exists())      // If the file exists
        {
            Log.i("Heart Rate Sensor", "Creating Header");     // Logs on Console.

            DataLogger dataLogger = new DataLogger(Subdirectory_Heartrate, Heart_Rate, Preference.Heart_Rate_Data_Headers);        /* Logs the Sensors data in a csv format */
            dataLogger.LogData();       // Saves the data to the directory.
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)      // This is where the data collected by the sensor is saved into a csv file which can be accessed.
    {
        String HeartRateMonitor = String.valueOf(event.values[0]);      // This changes the value of the sensor data to a string.
        final String logstring = SystemInformation.getTimeStamp() + "," + event.timestamp + "," + HeartRateMonitor + "," + event.accuracy;     // Appends the Heart Rate value onto the string

        new Thread(new Runnable()       // Starts a new runnable file.
        {
            public void run()       // Runs when the runnable is called
            {
                DataLogger dataLogger = new DataLogger(Subdirectory_Heartrate, Heart_Rate, logstring);       // Logs the data into a file that can be retrieved.
                dataLogger.LogData();   // Logs the data to the computer.
            }
        }).start();     // Starts the runnable.
    }

    @Override
    public void onDestroy()     // A destroy all activity switch (kill switch)
    {
        Log.i("Heart Rate Sensor", "Destroying Sensor Service");     // Logs on Console.

        mSensorManager.unregisterListener(this);        // Kills the listener
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy)     // Changes the accuracy of the sensor data collected.
    {
        // Please do not remove this, the code needs this to function properly. Thank you :-)
    }

    @Override
    public IBinder onBind(Intent intent)    /* Unknown but necessary function */
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
