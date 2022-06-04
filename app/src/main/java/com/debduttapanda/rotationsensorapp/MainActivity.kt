package com.debduttapanda.rotationsensorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.debduttapanda.rotationsensorapp.ui.theme.RotationSensorAppTheme
import com.kviation.sample.orientation.Orientation
import kotlin.math.tan

private val Float.toRadians: Double
    get() {
        return this* Math.PI / 180f
    }

class MainActivity : ComponentActivity(), Orientation.Listener {
    private val pitch = mutableStateOf(0f)
    private val roll = mutableStateOf(0f)
    private val yaw = mutableStateOf(0f)
    private lateinit var mOrientation: Orientation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mOrientation = Orientation(this)
        setContent {
            RotationSensorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        drawLine(
                            start = Offset(x = 0f, y = canvasHeight/2f),
                            end = Offset(x = canvasWidth, y = canvasHeight/2f),
                            color = Color.Blue,
                            strokeWidth = 5f
                        )

                        val halfWidth = canvasWidth/2f
                        val angle = roll.value
                        val h = halfWidth* tan(angle.toRadians)

                        drawLine(
                            start = Offset(x = 0f, y = canvasHeight/2f-h.toFloat()),
                            end = Offset(x = canvasWidth, y = canvasHeight/2f+h.toFloat()),
                            color = Color.Red,
                            strokeWidth = 5f
                        )
                    }
                    Column() {
                        Text(roll.value.toString())
                        Text(pitch.value.toString())
                    }

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mOrientation.startListening(this)
    }

    override fun onStop() {
        super.onStop()
        mOrientation.stopListening()
    }

    override fun onOrientationChanged(pitch: Float, roll: Float, yaw: Float) {
        this.pitch.value = pitch
        this.roll.value = roll
        this.yaw.value = yaw
    }
}