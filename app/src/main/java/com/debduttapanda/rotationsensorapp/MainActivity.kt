package com.debduttapanda.rotationsensorapp

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.debduttapanda.rotationsensorapp.ui.theme.RotationSensorAppTheme
import com.kviation.sample.orientation.Orientation
import kotlin.math.*

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

                            val halfWidth = canvasWidth/2f
                            val angle = roll.value
                            val h = halfWidth* tan(angle.toRadians)

                            val th = ((pitch.value*canvasHeight)/180f)
                            val tw = 0;//((yaw.value*canvasWidth)/180f)

                            val angleRad = angle / 180f * PI
                            val x = cos(angleRad).toFloat() //Fractional x
                            val y = sin(angleRad).toFloat() //Fractional y

                            val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
                            val offset = center + Offset(x * radius, y * radius)

                            val exactOffset = Offset(
                                x = min(offset.x.coerceAtLeast(0f), size.width),
                                y = size.height - min(offset.y.coerceAtLeast(0f), size.height)
                            )
                            drawLine(
                                Brush.linearGradient(
                                    colors = listOf(Color.Red,Color.Blue,Color.Green),
                                    start = Offset(size.width, size.height) - exactOffset,
                                    end = exactOffset
                                ),
                                start = Offset(x = 0f+tw, y = canvasHeight/2f-h.toFloat()+th),
                                end = Offset(x = canvasWidth+tw, y = canvasHeight/2f+h.toFloat()+th),
                                //color = Color.Red,
                                strokeWidth = 5f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f,20f),20f)
                            )
                        }
                        Box(
                            modifier = Modifier.size(100.dp).align(Alignment.Center)
                        ){
                            AndroidView(
                                modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
                                factory = { context ->
                                    // Creates custom view
                                    AttitudeIndicator(context)
                                },
                                update = { view ->
                                    view.setAttitude(pitch.value,roll.value)
                                }
                            )
                        }

                        Column() {
                            Text(roll.value.toString())
                            Text(pitch.value.toString())
                            Text(yaw.value.toString())
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