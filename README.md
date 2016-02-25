# IndoorAtlasExample3
Example which uses a Service to share LocationManager object across activities

Instantiating the IALocationManager object as early as possible is recommended in order to reduce the time of first fix.

## 1. Basics

All the basic concepts of IndoorAtlas API apply here. A quick demo is written in [IndoorAtlasExample](https://github.com/bhorkarg/IndoorAtlasExample/blob/master/README.md)


## 2. Sharing ``` IALocationManager ```

In this example, I have used a Bound Service to share a single IALocationManager object with other activities.

The service contains ```getLocationManager()``` and ```getResourceManager()``` which return the location and resource managers respectively.
```java
public class LocationService extends Service {
    private IALocationManager mLocationManager; //Single instance
    private IAResourceManager mResourceManager;

    public IALocationManager getLocationManager() {
        return mLocationManager;
    }

    public IAResourceManager getResourceManager() {
        return mResourceManager;
    }
}
```

Initialize the objects described in the ```onCreate()``` event of the service.

```java
public void onCreate() {
    super.onCreate();
    mLocationManager = IALocationManager.create(this);
    mResourceManager = IAResourceManager.create(this);
}
```

Since the service is a Bound Service, you need to return an ```IBinder``` object from the ```onBind``` method. Check the LocationService.java file for implementation.

Thereafter, once this service is started (```startService()```) from the MainActivity, other activities can *bind* to this service to access the require objects.

Bind to the service in any activity as follows:
```java
Intent intentService = new Intent(this, LocationService.class);
bindService(intentService, mServiceConnection, Context.BIND_AUTO_CREATE);
```

Make sure that a ```ServiceConnection``` (mServiceConnection) is created. When the service is connected, we can get the required objects. Below snippet show this:
```java
private ServiceConnection mServiceConnection = new ServiceConnection() {
  public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      //When LocationService is Connected, set mLocationService
      LocationService.MyBinder binder = (LocationService.MyBinder) iBinder;
      mLocationService = binder.getService();

      //set LocationManager
      mLocationManager = mLocationService.getLocationManager();
  }
```

Once we get the required objects we can make use of them.

## 3. Advantages

A service is very useful when we have to share some functionality with other applications as well. For example, you can use this LocationService you created in one app in other applications you have created. Thus a single service (with a single LocationManager object) can be used in multiple activities.

Creating a service for a very simple task is not recommended since you have to write more code as compared to other simpler ways.
