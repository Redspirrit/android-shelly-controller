package de.shelly_controller.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.shelly_controller.ui.theme.ShellyControllerTheme
import de.shelly_controller.viewModel.ApiViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShellyControllerTheme {
                val params = mapOf(
                    "turn" to "on",
                    "red" to "255",
                    "green" to "0",
                    "blue" to "0",
                    "white" to "0",
                    "gain" to "100"
                )
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, viewModel: ApiViewModel = viewModel()) {
    val shellyAction = viewModel.initShellys()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                viewModel.ledAction(shellyAction)
            }
        ) { Text("White") }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ShellyControllerTheme {
//        Greeting("192.168.178.37/color/0?turn=off")
//    }
//}