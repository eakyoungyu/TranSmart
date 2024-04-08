package com.kong.transmart.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kong.transmart.view.HomeView
import com.kong.transmart.view.WebView


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.HomeScreen,
        Screen.WebScreen
    )

    Box {
        Divider(
            color = MaterialTheme.colorScheme.tertiary,
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.TopCenter)
        )


        BottomNavigation (
            backgroundColor = MaterialTheme.colorScheme.primary,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                val selected = currentRoute == item.route
                val selectedContentColor = MaterialTheme.colorScheme.secondary
                val unselectedContentColor = MaterialTheme.colorScheme.onTertiary
                BottomNavigationItem(
                    icon = { Icon(imageVector = item.icon, contentDescription = item.title, tint = if (selected) selectedContentColor else unselectedContentColor) },
                    label = { Text(text = item.title) },
                    selectedContentColor = selectedContentColor,
                    unselectedContentColor = unselectedContentColor,
                    alwaysShowLabel = true,
                    selected = selected,
                    onClick =  {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }


}
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeView()
        }
        composable(route = Screen.WebScreen.route) {
            WebView()
        }
    }

}

sealed class Screen(val route: String, var icon: ImageVector, var title: String) {
    object HomeScreen: Screen(
        route = "home",
        icon = Icons.Default.Home,
        title = "Home"
    )
    object WebScreen: Screen(
        route = "web",
        icon = Icons.Default.Search,
        title = "Web"
    )
}