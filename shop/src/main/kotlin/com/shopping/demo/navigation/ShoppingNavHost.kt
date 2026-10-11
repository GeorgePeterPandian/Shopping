package com.shopping.demo.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shopping.demo.products.presentation.ConnectionErrorScreen
import com.shopping.demo.products.presentation.ProductDetailScreen
import com.shopping.demo.products.presentation.ProductDetailViewModel
import com.shopping.demo.products.presentation.ProductsScreen
import com.shopping.demo.products.presentation.ProductsViewModel
import androidx.compose.ui.res.stringResource
import com.shopping.demo.R

@Composable
fun ShoppingNavHost(navController: NavHostController, viewModel: ProductsViewModel) {
    NavHost(navController = navController, startDestination = "products") {
        composable("products") {
            ProductsScreen(
                viewModel = viewModel, onProductClick = { productId ->
                    navController.navigate("product/$productId")
                })
        }

        composable("product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            val detailViewModel: ProductDetailViewModel = hiltViewModel()
            val uiState by detailViewModel.uiState.collectAsState()

            LaunchedEffect(productId) { productId?.let(detailViewModel::loadProduct) }

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                uiState.error != null -> {
                    ConnectionErrorScreen(
                        message = uiState.error
                            ?: stringResource(R.string.product_detail_load_error),
                        onRetry = { productId?.let(detailViewModel::loadProduct) })
                }


                else -> {
                    uiState.product?.let { product ->
                        ProductDetailScreen(
                            product = product, onBackClick = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
