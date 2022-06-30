package com.bestoffers.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bestoffers.R

sealed class NavItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val navRoute: String
) {
    object Home: NavItem(R.string.home, R.drawable.ic_home, NAV_HOME)
    object User: NavItem(R.string.user, R.drawable.ic_user, NAV_USER)
    object Products: NavItem(R.string.my_alerts, R.drawable.ic_notifications, NAV_MY_ALERTS)
}