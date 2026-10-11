package com.shopping.demo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.shopping.demo.navigation.ShoppingNavHost
import com.shopping.demo.products.presentation.ProductsViewModel
import com.shopping.demo.ui.theme.ShoppingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ShoppingTheme {
                val navController = rememberNavController()
                ShoppingNavHost(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}