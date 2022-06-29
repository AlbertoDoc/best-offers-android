package com.bestoffers.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.AppRegistration
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Login
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bestoffers.home.HomeActivity
import com.bestoffers.login.LoginActivity
import com.bestoffers.register.RegisterActivity
import com.bestoffers.ui.theme.BestOffersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainActivityViewModel by viewModels()

        viewModel.getNavigationMessage().observe(this) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        viewModel.loadDatabase(this)
        viewModel.automaticLogin()

        setContent {
            BestOffersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold {
                        Column {
                            LoginButton { startLoginActivity() }
                            RegisterButton { startRegisterActivity() }
                        }
                    }
                }
            }
        }
    }

    private fun startLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    private fun startRegisterActivity() {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent);
    }
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        )
    ) {
        Icon(
            Icons.Rounded.Login,
            contentDescription = "Login",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Login")
    }
}

@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(
            start = 20.dp,
            top = 12.dp,
            end = 20.dp,
            bottom = 12.dp
        )
    ) {
        Icon(
            Icons.Rounded.AppRegistration,
            contentDescription = "Register",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Register")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BestOffersTheme {
        Column {
            LoginButton(onClick = {})
            RegisterButton(onClick = {})
        }
    }
}