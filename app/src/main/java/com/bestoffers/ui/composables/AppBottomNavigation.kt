package com.bestoffers.ui.composables

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bestoffers.R
import com.bestoffers.navigation.NavItem

@Composable
fun AppBottomNavigation(
    navController: NavController
) {
    val navItems = listOf(NavItem.Home, NavItem.User, NavItem.Products)

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_700)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach {
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = it.icon), contentDescription = "") },
                label = { Text(text = stringResource(id = it.title)) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                selected = currentRoute == it.navRoute,
                onClick = { navController.navigate(it.navRoute) })
        }
    }
}