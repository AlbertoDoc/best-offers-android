package com.bestoffers.details

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bestoffers.ui.theme.BestOffersTheme

class DetailsActivity : ComponentActivity() {

    private val viewModel = DetailsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.productUid = intent.getStringExtra("productUid").toString()

        setContent {
            DetailsScreen(productUid = viewModel.productUid)
        }
    }
}

@Composable
fun DetailsScreen(productUid: String) {
    BestOffersTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    TopAppBar(title = { Text("Details") })
                    Text(text = productUid)
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen(productUid = "productUid")
}