package com.bestoffers.details

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bestoffers.ui.theme.BestOffersTheme

class DetailsActivity : ComponentActivity() {

    private val viewModel = DetailsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.productUid = intent.getStringExtra("productUid").toString()

        viewModel.loadDatabase(this)

        viewModel.loadProduct()

        viewModel.getErrorMessage().observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.getSuccessMessage().observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
        }

        setContent {
            DetailsScreen(viewModel.product.name, viewModel.product.price, viewModel)
        }
    }
}

@Composable
fun DetailsScreen(name: String, price: Double, viewModel: DetailsViewModel) {
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
                    Text(text = name)
                    Text(text = "R$$price")
                    Box(modifier = Modifier.height(15.dp))
                    Text(text = "Preencha o formulário abaixo para criar um alerta:")
                    OutlinedTextField(
                        value = viewModel.startPrice.toString(),
                        onValueChange = { viewModel.startPrice = it.toDouble() },
                        label = { Text("Preço Inicial") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = viewModel.endPrice.toString(),
                        onValueChange = { viewModel.endPrice = it.toDouble() },
                        label = { Text("Preço Final") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(
                        onClick = { viewModel.submitAlert() },
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 12.dp,
                            end = 20.dp,
                            bottom = 12.dp
                        )
                    ) {
                        Text("Criar Alerta")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen("nome teste", 300.0, DetailsViewModel())
}