package com.mh.todaydiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mh.todaydiary.ui.MainNavigation
import com.mh.todaydiary.ui.theme.TodayDiaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodayDiaryTheme {
                MainNavigation()
            }
        }
    }
}