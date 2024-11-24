package com.mdshahsamir.expensebook

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mdshahsamir.expensebook.service.ExpenseBookService
import com.mdshahsamir.expensebook.ui.ExpenseBookApp
import com.mdshahsamir.ui.theme.ExpenseBookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseBookTheme {
                ExpenseBookApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, ExpenseBookService::class.java).putExtra("a", 4000), BIND_IMPORTANT)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseBookTheme {
        Greeting("Android")
    }
}