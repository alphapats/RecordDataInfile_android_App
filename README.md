# RecordDataInfile_android_App
 App with following features:
-Persistence of user information and sensor selection even during change of orientation, app navigation, and app closure 
- Check that all the fields in the registration are filled in 
- Action bar contains tabs instead of a spinner and an icon 
- One activity with each tab implemented as a fragment 
- In the activity for recording, get from the user label (stationary or walking) of that activity 
- Save sensor data in CSV files 
- The CSV file contains 
0th row {first name, last name, mobile, email, gender, age} 
1st row {timestamp, lat, long, accelx, accely, accelz, label} 
2nd row onwards contain values as per the format in the 1st row 
- One file for each recording, stored in the data folder of your app 
- Files should be exportable to PC 
- The app should keep collecting data even though the app in not in the foreground (collect data in a background service as a thread) 
