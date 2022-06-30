package com.bestoffers.my_alerts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bestoffers.home.HomeViewModel
import com.bestoffers.repositories.room.entities.ProductOfInterest

@Composable
fun MyAlertsScreen(viewModel: HomeViewModel) {
    val alerts = viewModel.getAlerts().observeAsState()

    Column {
        TopAppBar(title = { Text("My Alerts") })
        AlertList(alerts = alerts.value.orEmpty(), viewModel)
    }
}

@Composable
fun AlertList(alerts: List<ProductOfInterest>, viewModel: HomeViewModel) {
    LazyColumn {
        items(alerts) { alert ->
            AlertCard(alert = alert, viewModel)
        }
    }
}

@Composable
fun AlertCard(alert: ProductOfInterest, viewModel: HomeViewModel) {
    val product = viewModel.getProductById(alert)

    if (product != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.name,
                    fontSize = 16.sp
                )
                Text(
                    text = "R$" + product.price.toString(),
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Preço desejado entre R$" + alert.startPrice + " e R$" + alert.endPrice)
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.Notifications,
                    tint = if (alert.alert) Color.Green else Color.Red,
                    contentDescription = "Status of Alert"
                )
            }
        }
    }
}