package com.bestoffers.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bestoffers.repositories.room.entities.Product
import com.bestoffers.ui.theme.BestOffersTheme
import com.bestoffers.util.SampleData

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = HomeViewModel()

        setContent {
            BestOffersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold {
                        Column {
                            TopAppBar(title = { Text("Home") })
                            ProductList(products = viewModel.getProducts())
                        }
                    }
                }
            }
        }
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
fun PreviewProductsList() {
    BestOffersTheme {
        ProductList(products = SampleData.productsSample())
    }
}