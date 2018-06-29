package com.joel.phonerecord;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by asus on 2018/6/28.
 */

public class SensorManagerHelper implements SensorEventListener {
    private static final int SPEED_SHRESHOLD = 4000;
    private static final int UPTATE_INTERVAL_TIME = 54;
    private SensorManager sensorManager;
    private Sensor sensor;
    private OnShakeListener onShakeListener;
    private Context context;
    private float lastX;
    private float lastY;
    private float lastZ;
    private long lastUpdateTime;

    public SensorManagerHelper(Context c)
    {
        context = c;
        start();
    }

    public void start()
    {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null)
        {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        if (sensor != null)
        {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void stop()
    {
        sensorManager.unregisterListener(this);
    }

    public interface OnShakeListener
    {
        public void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener)
    {
        onShakeListener = listener;
    }

    public void onSensorChanged(SensorEvent event)
    {
        long currentUpdateTime = System.currentTimeMillis();
        long timeInterval = currentUpdateTime - lastUpdateTime;
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        lastUpdateTime = currentUpdateTime;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;
        lastX = x;
        lastY = y;
        lastZ = z;
        double speed = Math
                .sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;
        if (speed >= SPEED_SHRESHOLD)
            onShakeListener.onShake();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }


}
