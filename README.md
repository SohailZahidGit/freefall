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
     free fall detector module.
     
```kotlin
class MyApplication : FreeFallApplication()      
```
or declare it in manifest

```xml
<application android:name="com.xbrid.freefalldetector.utils.FreeFallApplication">
<!-- other stuff here -->

</application>
```

 - Register receiver:  
 
     To get free fall detection callback when your app is on foreground please receiver in manifest.
     
```xml
<receiver android:name=".receiver.FreeFallDetectReceiver">
   <intent-filter>
       <action android:name="free.fall.detected" />
   </intent-filter>
</receiver>
```
 - Retrieve detection list:   
 
     You can get the list of all detection with given statement.
  
```kotlin
val detectionList = DatabaseHelper(context).getAllEvents()        
```

Other methods check out in sample.

Getting Help
============

To report a specific problem or feature request, [open a new issue on Github](https://github.com/SohailZahidGit/freefall/issues/new).

Learn More
==========

To know more about this topic checkout this link, [Detector document](https://dergipark.org.tr/tr/download/article-file/800065).

Author
======

Created by [Sohail Zahid](https://github.com/SohailZahidGit) - [@Gmail](mailto:sohail.bsse@gmail.com)

