package com.github.compose.waveloading

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.compose.waveloading.ui.theme.ComposewaveloadingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposewaveloadingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Box(Modifier.fillMaxSize()) {
//                        WaveLoading(
//                            Modifier
//                                .width(200.dp)
//                                .height(200.dp)
//                                .align(Alignment.Center)
//                                .clip(RoundedCornerShape(100))
//                        )


                        WaveLoading2(
                            Modifier
                                .align(Alignment.Center)
                        ) {


                            Image(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(100)),
                                painter = painterResource(id = R.drawable.fundroid),
                                contentDescription = ""
                            )
//
//                            Row(Modifier.align(Alignment.Center)) {
//
//                                Image(
//                                    modifier = Modifier
//                                        .weight(1f)
//                                        .padding(10.dp)
//                                        .clip(RoundedCornerShape(100)),
//                                    painter = painterResource(id = R.drawable.logo_bmw),
//                                    contentDescription = ""
//                                )
//
//
//                            Text(
//                                modifier = Modifier
//                                    .weight(1f).align(Alignment.CenterVertically),
//                                text = "中秋",
//                                style = MaterialTheme.typography.h3,
//                                fontWeight = FontWeight.Bold,
//                                color = Color.Red
//                            )
//
//                                Image(
//                                    modifier = Modifier
//                                        .weight(1f)
//                                        .padding(10.dp)
//                                        .clip(RoundedCornerShape(100)),
//                                    painter = painterResource(id = R.drawable.logo_bmw),
//                                    contentDescription = ""
//                                )
//                            }

                        }


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
//            WaveLoading(
//                Modifier
//                    .align(Alignment.Center)
//                    .size(100.dp)
//                    .border(1.dp, Color.Black)
//            )

//
//            Image(
//                modifier = Modifier.wave(),
//                imageVector = Icons.Default.CheckCircle,
//                contentDescription = ""
//            )

            WaveLoading2 {
                Image(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = ""
                )
            }

        }

    }
}