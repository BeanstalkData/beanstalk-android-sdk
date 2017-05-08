## Beanstalk SDK sample application.

In order to compile this application with push notifications support a special file `google-services.json` is required.
This file can be obtained using [Firebase console](https://firebase.google.com/console/). Follow instructions from this manual [Set up a GCM Client App on Android](https://developers.google.com/cloud-messaging/android/client).
The file `google-services.json` must be put into the `sample/` folder.

To activate location tracking feature following lines must be added to the AndroidManifest.xml
```xml
        <receiver
            android:name="com.beanstalkdata.android.service.AlarmReceiver"
            android:process=":remote" />
        <service
            android:name="com.beanstalkdata.android.service.ContactRelocationService"
            android:exported="false" />
```

Also run time permissions checks must be implemented in the application with the location tracking feature.
See the code example in the ProfileFragment.java in this sample app.
