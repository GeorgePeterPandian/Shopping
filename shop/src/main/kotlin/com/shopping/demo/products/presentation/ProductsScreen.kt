package com.shopping.demo.products.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.shopping.demo.R
import com.shopping.demo.products.presentation.model.ProductsUIModel
import com.shopping.demo.ui.theme.DiscountGradient
import com.shopping.demo.ui.theme.RatingGold
import com.shopping.products.domain.model.Product
import java.util.Locale

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel, onProductClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()
    val gridState = rememberLazyGridState()

    val screenModifier = modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.safeDrawing)

    /*
     * Prioritise an initial-load error over an empty Paging result.
     *
     * ConnectionErrorScreen handles its own safeDrawing insets,
     * so pass the original modifier rather than screenModifier.
     */
    if (uiState.error != null && products.itemCount == 0) {
        ConnectionErrorScreen(
            message = uiState.error ?: stringResource(R.string.connection_error_message),
            onRetry = viewModel::retryProducts,
            modifier = modifier.fillMaxSize()
        )
        return
    }

    when (val refreshState = products.loadState.refresh) {
        is LoadState.Loading -> {
            when (products.itemCount) {
                0 -> LoadingContent(modifier = screenModifier)
                else -> ProductsContent(
                    searchQuery = uiState.searchQuery,
                    products = products,
                    onSearch = viewModel::searchProducts,
                    onProductClick = onProductClick,
                    gridState = gridState,
                    modifier = screenModifier
                )
            }
        }

        is LoadState.Error -> {
            when (products.itemCount) {
                0 -> ConnectionErrorScreen(
                    message = refreshState.error.localizedMessage
                        ?: stringResource(R.string.connection_error_message),
                    onRetry = products::retry,
                    modifier = modifier.fillMaxSize()
                )

                else ->
                    // Keep already loaded products visible if refreshing fails.
                    ProductsContent(
                        searchQuery = uiState.searchQuery,
                        products = products,
                        onSearch = viewModel::searchProducts,
                        onProductClick = onProductClick,
                        gridState = gridState,
                        modifier = screenModifier
                    )
            }
        }

        is LoadState.NotLoading -> {
            when (products.itemCount) {
                0 -> Column(modifier = screenModifier) {
                    ProductsHeader(
                        searchQuery = uiState.searchQuery,
                        productCount = 0,
                        onSearch = viewModel::searchProducts
                    )

                    EmptyContent(modifier = Modifier.weight(1f))
                }

                else -> ProductsContent(
                    searchQuery = uiState.searchQuery,
                    products = products,
                    onSearch = viewModel::searchProducts,
                    onProductClick = onProductClick,
                    gridState = gridState,
                    modifier = screenModifier
                )
            }
        }
    }
}

@Composable
private fun ProductsContent(
    searchQuery: String,
    products: LazyPagingItems<ProductsUIModel>,
    onSearch: (String) -> Unit,
    onProductClick: (Int) -> Unit,
    gridState: LazyGridState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ProductsHeader(
            searchQuery = searchQuery, productCount = products.itemCount, onSearch = onSearch
        )

        ProductsList(
            products = products,
            onProductClick = onProductClick,
            modifier = Modifier.weight(1f),
            gridState = gridState
        )
    }
}

@Composable
private fun ProductsHeader(
    searchQuery: String,
    productCount: Int,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSearchVisible by rememberSaveable { mutableStateOf(searchQuery.isNotBlank()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.products_header_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = stringResource(R.string.products_header_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable {
                        isSearchVisible = !isSearchVisible
                        when {
                            !isSearchVisible -> onSearch("")
                        }
                    }, shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when {
                            isSearchVisible -> Icons.Default.Close
                            else -> Icons.Default.Search
                        }, contentDescription = stringResource(
                            when {
                                isSearchVisible -> R.string.products_close_search
                                else -> R.string.products_search
                            }
                        ), tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (isSearchVisible) {
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.products_search_placeholder))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = null
                    )
                },
                trailingIcon = {
                    when {
                        searchQuery.isNotEmpty() -> IconButton(onClick = { onSearch("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.products_clear_search)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(
                        when {
                            searchQuery.isBlank() -> R.string.products_featured_title
                            else -> R.string.products_search_results
                        }
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(R.string.products_collection_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = stringResource(R.string.products_item_count, productCount),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductsList(
    products: LazyPagingItems<ProductsUIModel>,
    gridState: LazyGridState,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = products.itemCount, key = products.itemKey { product -> product.id }) { index ->
            val product = products[index]
            if (product != null) {
                ProductCard(product = product, onClick = { onProductClick(product.id) })
            } else {
                ProductCardPlaceholder()
            }
        }

        when (val appendState = products.loadState.append) {
            is LoadState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp), strokeWidth = 2.dp
                        )
                    }
                }
            }

            is LoadState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = appendState.error.localizedMessage ?: stringResource(
                                R.string.products_load_more_error
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )

                        TextButton(onClick = products::retry) {
                            Text(stringResource(R.string.common_retry))
                        }
                    }
                }
            }

            is LoadState.NotLoading -> Unit
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductsUIModel, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.95f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (product.discountPercentage > 0.0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(DiscountGradient)
            ) {
                Text(
                    text = stringResource(
                        R.string.products_discount, String.format(
                            Locale.US, "%.1f", product.discountPercentage
                        ).removeSuffix(".0")
                    ),
                    modifier = Modifier.padding(
                        horizontal = 7.dp, vertical = 4.dp
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
        }

        Text(
            text = product.category.uppercase(
                LocalLocale.current.platformLocale
            ),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = product.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Star, contentDescription = stringResource(
                    R.string.products_rating
                ), modifier = Modifier.size(17.dp), tint = RatingGold
            )

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                text = product.rating.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (product.discountPercentage > 0.0) {
            Text(
                text = stringResource(
                    R.string.products_price, "%.2f".format(Locale.US, product.discountedPrice)
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Text(
                text = stringResource(
                    R.string.products_price, "%.2f".format(Locale.US, product.price)
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough,
                maxLines = 1
            )
        } else {
            Text(
                text = stringResource(
                    R.string.products_price, "%.2f".format(Locale.US, product.price)
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = if (product.stock > 0) {
                    Icons.Filled.CheckCircle
                } else {
                    Icons.Filled.Cancel
                }, contentDescription = stringResource(
                    when {
                        product.stock > 0 -> R.string.products_in_stock
                        else -> R.string.products_out_of_stock
                    }
                ), modifier = Modifier.size(18.dp), tint = when {
                    product.stock > 0 -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.error
                }
            )

            Text(
                text = stringResource(
                    when {
                        product.stock > 0 -> R.string.products_in_stock
                        else -> R.string.products_out_of_stock
                    }
                ),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                color = when {
                    product.stock > 0 -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.error
                },
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ProductCardPlaceholder(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.95f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.products_empty_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
