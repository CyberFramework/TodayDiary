package com.mh.todaydiary.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mh.todaydiary.data.repository.Diary
import com.mh.todaydiary.ui.adddiary.AddEditDiaryScreen
import com.mh.todaydiary.ui.diarylist.DiaryListScreen

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = "diary_list") {
        composable("diary_list") {
            DiaryListScreen(
                addDiary = { navController.navigate("add_diary") },
                editDiary = { navController.navigate("add_diary?time=$it") }
            )
        }

        composable(
            "add_diary?time={time}",
            arguments = listOf(navArgument("time") {
                type = NavType.LongType
                defaultValue = 0
            })
        ) {
            AddEditDiaryScreen(
                onComplete = { navController.popBackStack() },
            )
        }
    }
}