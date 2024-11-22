package com.open.compose.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.open.compose.R
import com.open.compose.ui.theme.ComposeTheme

class ComposeActivity : ComponentActivity() {
    private var mViewModel = ComposeViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeContent(mViewModel)
                }
            }
        }
    }


}

@Composable
private fun ComposeContent(viewModel: ComposeViewModel,modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Text(
            text = viewModel.message.value,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier,
        )

        Button(onClick = {
            Log.d("Button", "onClick")
            viewModel.message.value = if (viewModel.message.value == "Hello Compose") {
                "Hello open9527"
            } else {
                "Hello Compose"
            }

        }) {
            Text(text = "点击有惊喜", color = Color.White)
        }

        Image(
            painter = painterResource(id = R.mipmap.ic_compose),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(50.dp),
        )
//        AsyncImage(
//            model = viewModel.imageUrl.value,
//            contentDescription = null,
//        )

    }
}

@Preview(showBackground = true)
@Composable
fun ComposePreview() {
    ComposeTheme {
        ComposeContent(ComposeViewModel())
    }
}

