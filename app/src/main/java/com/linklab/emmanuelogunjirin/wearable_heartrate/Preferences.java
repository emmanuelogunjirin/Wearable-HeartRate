package com.linklab.emmanuelogunjirin.wearable_heartrate;

import android.os.Environment;

@SuppressWarnings("ALL")    // Service wide suppression for the Errors.
public class Preferences        // System wide one stop place to set all settings for a particular individual
{
    /* ------------------------------------------------------------------------------- Settings for Deployment, Read Notes Carefully ------------------------------------------------------------------------------*/

    // There should be **NO CHARACTERS OTHER THAN LETTERS, NUMBERS, - or _ ** in file or directory names!
    public String DeviceID = "Test";        // Internal ID of Device assigned to Dyad
    public String Directory = Environment.getExternalStorageDirectory() + "/Wearable-HeartRate/";        // Directory on the watch where all files are saved

    // Settings for Vibration | Time is in ms |
    public int HapticFeedback = 50;           // How should the system vibrate when a button is clicked

    /* Settings for Changing Individual File Name <----------------------------------------- This is where you change the file names, it updates everywhere */
    public String Heart_Rate = "HeartRate_Data.csv";           // This is the system file for the Heart Rate Sensor

    /* Settings for Changing the Subdirectories in the Main Directory */
    public String Subdirectory_HeartRate = "Heart_Rate";        // This is where the Heartrate data is kept

    /* Headers to individual files that are being logged to <--------------------------------------------- This is the order that the headers will appear in */
    public String Heart_Rate_Data_Headers = "Date --- Time, System Time Stamp, Heart Rate Value, Confidence Level";         // Column Headers for Heart_Rate_Data
}
