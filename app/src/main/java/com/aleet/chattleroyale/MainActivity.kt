package com.aleet.chattleroyale

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import com.aleet.chattleroyale.presentation.login.NavGraphs
import com.aleet.chattleroyale.presentation.theme.ChattleRoyaleTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChattleRoyaleTheme {
                Surface { DestinationsNavHost(navGraph = NavGraphs.root) }
            }
        }
    }
}