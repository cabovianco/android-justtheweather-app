package com.cabovianco.justtheweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cabovianco.justtheweather.presentation.ui.screen.MainScreen
import com.cabovianco.justtheweather.presentation.ui.theme.JustTheWeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JustTheWeatherTheme {
                MainScreen(viewModel = hiltViewModel())
            }
        }
    }
}
