package com.open.compose.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.open.compose.ui.theme.ComposeTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Hello $name!",
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
        )
        Text(
            text = "First Compose Demo",
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
        )
        Row(modifier = modifier) {
            Text(
                text = "Row First!",
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Row Compose Demo",
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Row(name,modifier)
    }
}

@Composable
fun Row(name: String, modifier: Modifier = Modifier) {
    Row() {
        Text(
            text = "Row First ${name}!",
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Row Compose Demo${name}",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .padding(10.dp)
                .clickable {
                    Log.d("ComposeActivity","clickable")
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        Greeting("Android")
    }
}