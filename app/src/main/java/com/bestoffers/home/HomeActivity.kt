package com.bestoffers.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bestoffers.check_alert.CheckAlertRunnable
import com.bestoffers.details.DetailsActivity
import com.bestoffers.my_alerts.MyAlertsScreen
import com.bestoffers.navigation.NAV_HOME
import com.bestoffers.navigation.NAV_MY_ALERTS
import com.bestoffers.navigation.NAV_USER
import com.bestoffers.repositories.retrofit.RetrofitClient
import com.bestoffers.repositories.room.database.BestOffersDatabase
import com.bestoffers.repositories.room.entities.Product
import com.bestoffers.ui.composables.AppBottomNavigation
import com.bestoffers.ui.theme.BestOffersTheme
import com.bestoffers.util.SampleData
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class HomeActivity : ComponentActivity() {

    private val BACKGROUND_ALERT_CHECK_FREQUENCY = 20L

    private val viewModel = HomeViewModel()

    private lateinit var backgroundAlertCheckTask: ScheduledFuture<Any>
    private lateinit var backgroundAlertExecutorService: ScheduledExecutorService

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
             viewModel.loadAlerts()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getErrorMessage().observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.getNavigateToDetailsPage().observe(this) {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("productUid", it)
            startActivity(intent)
        }

        viewModel.loadDatabase(this)

        backgroundAlertExecutorService = Executors.newSingleThreadScheduledExecutor()
        backgroundAlertCheckTask = backgroundAlertExecutorService.scheduleAtFixedRate(
            CheckAlertRunnable(BestOffersDatabase().getDatabase(this),
                RetrofitClient().getRetrofitInstance(),
                this
            ), 0, BACKGROUND_ALERT_CHECK_FREQUENCY, TimeUnit.SECONDS
        ) as ScheduledFuture<Any>

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(messageReceiver, IntentFilter("updateAlertList"))

        setContent {
            HomeScreen(viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProducts()
        viewModel.loadAlerts()
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val navController = rememberNavController()

    BestOffersTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                bottomBar = { AppBottomNavigation(navController = navController) }
            ) {
                NavHost(navController = navController, startDestination = NAV_HOME ) {
                    composable(NAV_HOME) {
                        HomeScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable(NAV_USER) { UserScreen(navController = navController, text = "User Screen") }
                    composable(NAV_MY_ALERTS) { MyAlertsScreen(viewModel) }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val products = viewModel.getProducts().observeAsState()

    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        stickyHeader { SearchField(viewModel) }

        items(products.value.orEmpty()) {
            product -> ProductCard(product = product, viewModel)
        }
    }
}

@Composable
fun UserScreen(navController: NavController, text: String) {
    Text(text = "text2")
}

@Composable
fun SearchField(
    viewModel: HomeViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.text,
            onValueChange = {
                viewModel.text = it
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Search here...",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = { viewModel.loadProducts() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (viewModel.text.isNotEmpty()) {
                            viewModel.text = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.White.copy(
                    alpha = ContentAlpha.medium
                )
            )
        )
    }
}

@Composable
fun ProductList(products: List<Product>, viewModel: HomeViewModel) {
    Column {
        products.forEach { product ->
            ProductCard(product = product, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductCard(product: Product, viewModel: HomeViewModel) {
    Card(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        onClick = {
            viewModel.navigateToDetailsPage(product.uid)
        },
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = product.name,
                fontSize = 16.sp
            )
            Text(
                text = "R$" + product.price.toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
    Divider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        color = MaterialTheme.colors.primary.copy(alpha = 0.6f),
        thickness = 1.dp
    )
}

@Preview
@Composable
fun PreviewPage() {
    BestOffersTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold {
                Column {
                    TopAppBar(title = { Text("Home") })
                    SearchFieldPreview()
                    ProductList(products = SampleData.productsSample(), HomeViewModel())
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewProductsList() {
    BestOffersTheme {
        ProductList(products = SampleData.productsSample(), HomeViewModel())
    }
}

@Preview
@Composable
fun SearchFieldPreview() {
    SearchField(
        HomeViewModel()
    )
}