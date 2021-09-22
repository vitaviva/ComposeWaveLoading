package com.github.compose.waveloading.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.compose.waveloading.DrawType
import com.github.compose.waveloading.WaveLoading
import com.github.compose.waveloading.rememberDrawColor
import com.github.compose.waveloading.sample.ui.theme.ComposewaveloadingTheme
import com.github.compose.waveloading.waveLoading

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposewaveloadingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {


//                    DrawTypeExample()

                    Dashboard()

                }
            }
        }
    }
}


@Composable
fun Dashboard() {

    var _progress by remember { mutableStateOf(0.5f) }
    var _velocity by remember { mutableStateOf(1.0f) }
    var _amplitude by remember { mutableStateOf(0.2f) }
    var _backImage by remember { mutableStateOf(true) }


    Column(Modifier.padding(start = 10.dp, end = 10.dp)) {

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .waveLoading(
                    backDrawType = if (_backImage) DrawType.DrawImage else DrawType.None,
                    progress = _progress,
                    velocity = _velocity,
                    amplitude = _amplitude,
                ),
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

        Box(
            Modifier
                .weight(1f)
                .waveLoading(
                    backDrawType = if (_backImage) rememberDrawColor() else DrawType.None,
                    progress = _progress,
                    velocity = _velocity,
                    amplitude = _amplitude
                ),
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

        Box(
            Modifier
                .weight(1f)
                .waveLoading(
                    foreDrawType = DrawType.DrawColor(Color.Cyan),
                    backDrawType = if (_backImage) rememberDrawColor() else DrawType.None,
                    progress = _progress,
                    velocity = _velocity,
                    amplitude = _amplitude,
                ),
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


        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .waveLoading(
                    backDrawType = if (_backImage) rememberDrawColor() else DrawType.None,
                    progress = _progress,
                    velocity = _velocity,
                    amplitude = _amplitude,
                ),
            contentAlignment = Alignment.Center
        ) {


            Row {
                Text(
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

@Composable
fun DrawTypeExample() {

    var _progress by remember { mutableStateOf(0.5f) }

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.weight(1f)
        )
        {
            LabelWave(
                modifier = Modifier.weight(1f),
                "1",
                progress = _progress,
                backDrawType = DrawType.DrawImage,
                foreDrawType = DrawType.DrawImage
            )

            LabelWave(
                modifier = Modifier.weight(1f),
                "2",
                progress = _progress,
                backDrawType = DrawType.DrawColor(Color.LightGray),
                foreDrawType = DrawType.DrawImage
            )

        }

        Row(modifier = Modifier.weight(1f)) {

            LabelWave(
                modifier = Modifier.weight(1f),
                "3",
                progress = _progress,
                backDrawType = DrawType.DrawColor(Color.LightGray),
                foreDrawType = DrawType.DrawColor(Color.Cyan)
            )

            LabelWave(
                modifier = Modifier.weight(1f),
                "4",
                progress = _progress,
                backDrawType = DrawType.None,
                foreDrawType = DrawType.DrawColor(Color.Cyan)
            )

        }


        LabelSlider(
            label = "Progress",
            value = _progress,
            onValueChange = { _progress = it },
            range = 0f..1f
        )

    }


}

@Composable
fun LabelWave(
    modifier: Modifier,
    label: String, progress: Float = 0.5f,
    foreDrawType: DrawType, backDrawType: DrawType = DrawType.DrawColor(Color.LightGray)
) {
    Column(modifier) {
        Text(label, Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
        WaveLoading(
            Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally),
            progress = progress,
            foreDrawType = foreDrawType,
            backDrawType = backDrawType
        ) {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.logo_windows),
                contentDescription = ""
            )

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
