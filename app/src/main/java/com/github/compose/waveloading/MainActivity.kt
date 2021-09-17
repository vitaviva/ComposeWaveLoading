package com.github.compose.waveloading

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.compose.waveloading.ui.theme.ComposewaveloadingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposewaveloadingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    var _progress by remember { mutableStateOf(0.5f) }
                    var _velocity by remember { mutableStateOf(1.0f) }
                    var _amplitude by remember { mutableStateOf(0.2f) }
                    var _backImage by remember { mutableStateOf(true) }

                    Column(Modifier.padding(start = 10.dp, end = 10.dp)) {

                        Spacer(modifier = Modifier.height(20.dp))

                        WaveLoading(
                            modifier = Modifier.weight(1f),
                            backDrawType = if (_backImage) DrawType.DrawImage else DrawType.None,
                            progress = _progress,
                            velocity = _velocity,
                            amplitude = _amplitude,

                            ) {

                            Row {
                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_tiktok),
                                    contentDescription = ""
                                )

                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_nba),
                                    contentDescription = ""
                                )

                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_chrome),
                                    contentDescription = ""
                                )


                            }


                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        WaveLoading(
                            Modifier
//                                .size(200.dp)
                                .weight(1f),
                            backDrawType = if (_backImage) rememberDrawColor() else DrawType.None,
                            progress = _progress,
                            velocity = _velocity,
                            amplitude = _amplitude
                        ) {



//
//                            Image(
//                                modifier = Modifier
////                                    .size(100.dp)
//                                    .padding(10.dp)
//                                    .clip(RoundedCornerShape(100)),
//                                painter = painterResource(id = R.drawable.fundroid),
//                                contentDescription = "",
//                            )
//
                            Row {

                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_windows),
                                    contentDescription = ""
                                )


                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_huawei),
                                    contentDescription = ""
                                )

                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_flutter),
                                    contentDescription = ""
                                )
                            }

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        WaveLoading(
                            Modifier.weight(1f),
                            drawType = DrawType.DrawColor(Color.Cyan),
                            backDrawType = if (_backImage) rememberDrawColor() else DrawType.None,
                            progress = _progress,
                            velocity = _velocity,
                            amplitude = _amplitude,
                        ) {

                            Row {

                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_windows),
                                    contentDescription = ""
                                )


                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_react),
                                    contentDescription = ""
                                )

                                Image(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    painter = painterResource(id = R.drawable.logo_flutter),
                                    contentDescription = ""
                                )
                            }

                        }

                        Spacer(modifier = Modifier.height(10.dp))


                        WaveLoading(
                            Modifier.weight(1f),
                            backDrawType = if (_backImage) rememberDrawColor() else DrawType.None,
                            progress = _progress,
                            velocity = _velocity,
                            amplitude = _amplitude
                        ) {

                            val animate by rememberInfiniteTransition().animateFloat(
                                initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
                                    animation = tween(2000, easing = LinearEasing),
                                    repeatMode = RepeatMode.Restart
                                )
                            )

                            Row {
                                Text(
                                    modifier = Modifier.rotate(animate),
                                    text = "Hello ",
                                    style = MaterialTheme.typography.h3,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = Color.Green
                                )

                                Text(
                                    text = "Compose",
                                    style = MaterialTheme.typography.h3,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = Color.Magenta
                                )
                            }


                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LabelCheckBox(
                            label = "Back Image",
                            checked = _backImage,
                            onChecked = { _backImage = it }
                        )

                        LabelSlider(
                            label = "Progress",
                            value = _progress,
                            onValueChange = { _progress = it },
                            range = 0f..1f
                        )

                        LabelSlider(
                            label = "Velocity",
                            value = _velocity,
                            onValueChange = { _velocity = it },
                            range = 0f..1f
                        )

                        LabelSlider(
                            label = "Amplitude",
                            value = _amplitude,
                            onValueChange = { _amplitude = it },
                            range = 0f..1f
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }


                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposewaveloadingTheme {

        Box {

            WaveLoading(progress = 0.5f) {

                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "Hello World",
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }

        }

    }
}


@Composable
private fun LabelCheckBox(
    label: String,
    checked: Boolean = false,
    onChecked: (Boolean) -> Unit
) {
    Row(Modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(label, modifier = Modifier.width(100.dp))
        Checkbox(
            checked = checked,
            onCheckedChange = onChecked
        )
    }
}

@Composable
private fun LabelSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>
) {
    Row(Modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            label, modifier = Modifier
                .width(100.dp)
                .align(Alignment.CenterVertically)
        )
        Slider(
            modifier = Modifier.align(Alignment.CenterVertically),
            value = value,
            onValueChange = onValueChange,
            valueRange = range
        )
    }
}
