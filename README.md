Free Fall Detector
======================

Free fall detector is build on top of android sensor service.
It will detect an event when you device is in free fall state and fire notification 
when free fall detect there might some false/positive result based on sensor info.

When detection is happened we will store free fall duration and stamp in database.

Setup
======

To use `FreeFallDetector` follow given instructions
 
 - Extend your application class with `FreeFallApplication` class which will initialize
     free fall detector.
     
     ```xml
             <application android:name="com.xbrid.freefalldetector.utils.FreeFallApplication">
                
             </application>
     ```

```kotlin
class MyApplication : FreeFallApplication()
               
```
    
 - Register receiver:  
     allows you to set the maximum scale of center top page.
     
     ```xml
             <receiver android:name=".receiver.FreeFallDetectReceiver">
                 <intent-filter>
                     <action android:name="free.fall.detected" />
                 </intent-filter>
             </receiver>
     ```
    
   
you can get the list of all detection like this.
  
```kotlin
val detectionList = DatabaseHelper(context).getAllEvents()
               
```

Other methods check out in sample.

Getting Help
============

To report a specific problem or feature request, [open a new issue on Github](https://github.com/SohailZahidGit/freefall/issues/new).

Author
======

Created by [Sohail Zahid](https://github.com/SohailZahidGit) - [@gigamole](mailto:sohail.bsse@gmail.com)

