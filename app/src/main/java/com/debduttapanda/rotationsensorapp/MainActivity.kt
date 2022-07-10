package com.debduttapanda.rotationsensorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.debduttapanda.rotationsensorapp.ui.theme.RotationSensorAppTheme

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
                    Box(){
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height

                            drawLine(
                                start = Offset(x = 0f, y = canvasHeight/2f),
                                end = Offset(x = canvasWidth, y = canvasHeight/2f),
                                color = Color.Blue,
                                strokeWidth = 5f
                            )

                            val th = ((pitch.value*canvasHeight)/180f)
                            val tw = ((yaw.value*canvasWidth)/180f)

                            val start = Offset(x = 0f+tw, y = canvasHeight/2f+th)
                            val end = Offset(x = canvasWidth+tw, y = canvasHeight/2f+th)

                            val rect = Rect(Offset.Zero, size)

                            rotate(degrees = roll.value, rect.center) {
                                drawLine(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.Red,
                                            Color.Green,
                                            Color.Blue,
                                            Color.Yellow,
                                            Color.Cyan,
                                            Color.Magenta
                                        ),
                                        start = start,
                                        end = end
                                    ),
                                    start = start,
                                    end = end,
                                    strokeWidth = 60f,
                                )
                            }
                        }
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