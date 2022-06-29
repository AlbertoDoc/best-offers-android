package com.bestoffers.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bestoffers.navigation.NAV_HOME
import com.bestoffers.navigation.NAV_PRODUCTS
import com.bestoffers.navigation.NAV_USER
import com.bestoffers.repositories.room.entities.Product
import com.bestoffers.ui.composables.AppBottomNavigation
import com.bestoffers.ui.theme.BestOffersTheme

class HomeActivity : ComponentActivity() {

    private val viewModel = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getErrorMessage().observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loadDatabase(this)

        setContent {
            HomeScreen(viewModel, this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProducts()
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel, lifecycleOwner: LifecycleOwner) {
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
                            viewModel = viewModel,
                            lifecycleOwner = lifecycleOwner
                        )
                    }
                    composable(NAV_USER) { UserScreen(navController = navController, text = "User Screen") }
                    composable(NAV_PRODUCTS) { ProductsScreen(navController = navController, text = "Products Screen") }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel, lifecycleOwner: LifecycleOwner) {
    val products = viewModel.getProducts().observeAsState()

    Column {
        TopAppBar(title = { Text("Home") })
        SearchField(viewModel)
        ProductList(products = products.value.orEmpty())
    }
}

@Composable
fun UserScreen(navController: NavController, text: String) {
    Text(text = "text2")
}

@Composable
fun ProductsScreen(navController: NavController, text: String) {
    Text(text = "text3")
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
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            ProductCard(product = product)
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Column {
        Text(text = product.name)
        Text(text = product.price.toString())
    }
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
                    //ProductList(products = viewModel.getProducts())
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewProductsList() {
//    BestOffersTheme {
//        ProductList(products = SampleData.productsSample())
//    }
//}

@Preview
@Composable
fun SearchFieldPreview() {
    SearchField(
        HomeViewModel()
    )
}