package com.kong.transmart.view

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.kong.transmart.navigation.BottomNavigationBar
import com.kong.transmart.navigation.Navigation

@Composable
fun MainView() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        it
        Navigation(navController = navController,)
    }

}