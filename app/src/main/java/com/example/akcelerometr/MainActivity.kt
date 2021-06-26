package com.example.akcelerometr

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), SensorEventListener {

    // X
    val AxisSeekBarX: SeekBar by lazy { findViewById(R.id.seekBarX) }
    val AxisTxtVIX: TextView by lazy { findViewById(R.id.txtViAxisX) }
    // Y
    val AxisSeekBarY: SeekBar by lazy { findViewById(R.id.seekBarY) }
    val AxisTxtVIY: TextView by lazy { findViewById(R.id.txtViAxisY) }
    // Z
    val AxisSeekBarZ: SeekBar by lazy { findViewById(R.id.seekBarZ) }
    val AxisTxtVIZ: TextView by lazy { findViewById(R.id.txtViAxisZ) }

    //Humidity
    val HumidityTxtVi: TextView by lazy { findViewById(R.id.txtViHumidity) }
    // Temperature
    val TemperatureTxtVi: TextView by lazy {findViewById(R.id.txtViTemperature)}

    val sensorManager: SensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){

            val alpha :Float = 0.8f
            val gravity = FloatArray(3)
            val linearAcceleration = FloatArray(3)

            // X
            gravity[0] = alpha * gravity[0] + (1-alpha) * event.values[0]

            linearAcceleration[0] = event.values[0] - gravity[0]

            AxisTxtVIX.text = String.format("%.3f", linearAcceleration[0])
            AxisSeekBarX.progress = 1000 + (linearAcceleration[0] * 100).roundToInt()

            // Y
            gravity[1] = alpha * gravity[1] + (1-alpha) * event.values[1]

            linearAcceleration[1] = event.values[1] - gravity[1]

            AxisTxtVIY.text = String.format("%.3f", linearAcceleration[1])
            AxisSeekBarY.progress = 1000 + (linearAcceleration[1] * 100).roundToInt()

            // Z
            gravity[2] = alpha * gravity[2] + (1-alpha) * event.values[2]

            linearAcceleration[2] = event.values[2] - gravity[2]

            AxisTxtVIZ.text = String.format("%.3f", linearAcceleration[2])
            AxisSeekBarZ.progress = 1000 + (linearAcceleration[2] * 100).roundToInt()
        }


        if(event?.sensor?.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            HumidityTxtVi.text = event.values[0].toString()
        }


        if(event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            if(event.values[0] < 0) {
                TemperatureTxtVi.setTextColor(applicationContext.getColor(R.color.teal_700))
                TemperatureTxtVi.text = event.values[0].toString()
            } else if(event.values[0] < 25) {
                TemperatureTxtVi.setTextColor(applicationContext.getColor(R.color.black))
                TemperatureTxtVi.text = event.values[0].toString()
            } else {
                TemperatureTxtVi.setTextColor(applicationContext.getColor(R.color.red))
                TemperatureTxtVi.text = event.values[0].toString()
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}