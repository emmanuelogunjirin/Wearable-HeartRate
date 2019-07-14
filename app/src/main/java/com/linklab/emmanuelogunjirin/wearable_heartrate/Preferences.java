package com.linklab.emmanuelogunjirin.wearable_heartrate;

import android.os.Environment;

@SuppressWarnings("ALL")    // Service wide suppression for the Errors.
public class Preferences        // System wide one stop place to set all settings for a particular individual
{
    /* ------------------------------------------------------------------------------- Settings for Deployment, Read Notes Carefully ------------------------------------------------------------------------------*/

    // There should be **NO CHARACTERS OTHER THAN LETTERS, NUMBERS, - or _ ** in file or directory names!
    public String DeviceID = "Huawei";        // Internal ID of Device assigned to Dyad
    public String Directory = Environment.getExternalStorageDirectory() + "/Wearables/";        // Directory on the watch where all files are saved

    // Settings for Vibration | Time is in ms |
    public int HapticFeedback = 50;           // How should the system vibrate when a button is clicked

    // Settings for the Accelerometer Sensor
    public int AccelDataCount = 400;     // How many data do you want to check for with accelerometer

    // Settings for the Gyroscope Sensor
    public int GyDataCount = 400;     // How many data do you want to check for.

    /* Settings for Changing Individual File Name <----------------------------------------- This is where you change the file names, it updates everywhere */
    public String Heart_Rate = "HeartRate_Data.csv";           // This is the system file for the Heart Rate Sensor
    public String Gyroscope = "Gyroscope_Data";         // This is the gyroscope file for the sensor
    public String Accelerometer = "Accelerometer_Data";     // This is the Accelerometer File (DO NOT ADD .CSV here as the accelerometer data are saved differently)

    /* Settings for Changing the Subdirectories in the Main Directory */
    public String Subdirectory_HeartRate = "Heart_Rate";        // This is where the Heartrate data is kept
    public String Subdirectory_Gyroscope = "Gyroscope";     // This is where the Gyroscope data is kept
    public String Subdirectory_Accelerometer = "Accelerometer";        // This is where the accelerometer data is kept

    /* Headers to individual files that are being logged to <--------------------------------------------- This is the order that the headers will appear in */
    public String Heart_Rate_Data_Headers = "Date --- Time, System Time Stamp, Heart Rate Value, Confidence Level";         // Column Headers for Heart_Rate_Data
    public String Accelerometer_Data_Headers = "Date --- Time, System Time Stamp, X-Value, Y-Value, Z-Value";      // Column Headers for Accelerometer_Data
    public String Gyroscope_Data_Headers = "Date --- Time, System Time Stamp, X-Value, Y-Value, Z-Value";      // Column Headers for Gyroscope_Data
}
