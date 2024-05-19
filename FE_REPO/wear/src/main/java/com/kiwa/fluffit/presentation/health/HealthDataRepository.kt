package com.kiwa.fluffit.presentation.health

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.PassiveMonitoringClient
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import java.util.concurrent.Executors


class HealthRepository(context: Context) {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val heartRateSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    private val passiveMonitoringClient: PassiveMonitoringClient = HealthServices.getClient(context).passiveMonitoringClient

    fun startTracking(onStepsUpdated: (Long) -> Unit, onDistanceUpdated: (Double) -> Unit, onCaloriesUpdated: (Double) -> Unit) {
        val config = PassiveListenerConfig.builder()
            .setDataTypes(setOf(DataType.STEPS_DAILY, DataType.DISTANCE_DAILY, DataType.CALORIES_DAILY))
            .build()

        passiveMonitoringClient.setPassiveListenerCallback(
            config,
            Executors.newSingleThreadExecutor(),
            object : PassiveListenerCallback {
                override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
                    val stepsDataPoints = dataPoints.getData(DataType.STEPS_DAILY)
                    stepsDataPoints.forEach { dataPoint ->
                        val steps = dataPoint.value
                        onStepsUpdated(steps)
                    }

                    val distanceDataPoints = dataPoints.getData(DataType.DISTANCE_DAILY)
                    distanceDataPoints.forEach { dataPoint ->
                        val distance = dataPoint.value
                        onDistanceUpdated(distance)
                    }

                    val caloriesDataPoints = dataPoints.getData(DataType.CALORIES_DAILY)
                    caloriesDataPoints.forEach { dataPoint ->
                        val calories = dataPoint.value
                        onCaloriesUpdated(calories)
                    }
                }
            }
        )
    }

    fun startHeartRateMeasurement(onHeartRateUpdated: (Int) -> Unit) {
        heartRateSensor?.also {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    val heartRate = event?.values?.firstOrNull()?.toInt()
                    heartRate?.let { onHeartRateUpdated(it) }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Handle sensor accuracy changes if needed
                }
            }, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopHeartRateMeasurement() {
//        sensorManager.unregisterListener(sensorEventListener)
    }
}
