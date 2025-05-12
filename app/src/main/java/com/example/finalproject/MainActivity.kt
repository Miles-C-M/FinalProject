package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.WindowMetricsCalculator
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lastShakeTime: Long = 0
    private val shakeThreshold = 15.0 // Acceleration threshold to trigger shake (m/s²)
    private val debounceTime = 1000L   // 1 second debounce to avoid repeated triggers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = if (compactScreen())
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT else
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER


        setContentView(R.layout.activity_main)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startRegisterActivity()
        } else {
            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
            bottomNavigationView.selectedItemId = R.id.profile

            setCurrentFragment(SecondFragment())
            Toast.makeText(this, "Hello, ${currentUser.displayName}. Shake to log out.", Toast.LENGTH_LONG).show()

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.events -> setCurrentFragment(FirstFragment())
                    R.id.profile -> setCurrentFragment(SecondFragment())
                    R.id.music -> setCurrentFragment(ThirdFragment())
                }
                true
            }
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble())

            if (acceleration > shakeThreshold) {
                val now = System.currentTimeMillis()
                if (now - lastShakeTime > debounceTime) {
                    lastShakeTime = now
                    Log.d("MainActivity", "Shake detected with acceleration: $acceleration")
                    startRegisterActivity()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    private fun setCurrentFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .commit()
    }

    private fun startRegisterActivity() {
        AuthUI.getInstance().signOut(this)
            .addOnCompleteListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
    }

    /** Determines whether the device has a compact screen. **/
    private fun compactScreen() : Boolean {
        val metrics = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(this)
        val width = metrics.bounds.width()
        val height = metrics.bounds.height()
        val density = resources.displayMetrics.density
        val windowSizeClass = WindowSizeClass.compute(width/density, height/density)

        return windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT ||
                windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT
    }
}
