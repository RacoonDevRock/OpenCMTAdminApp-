package com.cmt.openctmadminapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cmt.openctmadminapp.core.navigation.AppNavGraph
import com.cmt.openctmadminapp.core.network.CheckInternetScreen
import com.cmt.openctmadminapp.ui.theme.OpenCTMAdminAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenCTMAdminAppTheme {
                CheckInternetScreen {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val navigationController = rememberNavController()
                        AppNavGraph(Modifier.padding(innerPadding), navigationController)
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenCTMAdminAppTheme {
    }
}