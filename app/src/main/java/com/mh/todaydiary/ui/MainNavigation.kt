package com.mh.todaydiary.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mh.todaydiary.ui.adddiary.AddEditDiaryScreen
import com.mh.todaydiary.ui.diarylist.DiaryListScreen

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),

) {
    NavHost(navController = navController, startDestination = "diary_list") {
        composable("diary_list") {
            DiaryListScreen(
                addDiary = { navController.navigate("add_diary") }
            )
        }

        composable("add_diary") {
            AddEditDiaryScreen(
                onComplete = { navController.popBackStack() }
            )
        }
    }
}