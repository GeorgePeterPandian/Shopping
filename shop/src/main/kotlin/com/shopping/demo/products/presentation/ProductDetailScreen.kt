package com.shopping.demo.products.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.shopping.demo.R
import com.shopping.demo.products.presentation.model.ProductsUIModel
import com.shopping.demo.ui.theme.DiscountGradient
import com.shopping.demo.ui.theme.RatingGold
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun ProductDetailScreen(
    product: ProductsUIModel, onBackClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
    ) {
        ProductHero(
            product = product, onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProductSummary(product)

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
            )

            DetailSection(
                title = stringResource(R.string.product_detail_about)
            ) {
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
            )

            DetailSection(
                title = stringResource(R.string.product_detail_details)
            ) {
                DetailRow(
                    stringResource(R.string.product_detail_brand),
                    product.brand ?: stringResource(R.string.product_detail_not_specified)
                )
                DetailRow(
                    stringResource(R.string.product_detail_sku), product.sku
                )
                DetailRow(
                    stringResource(R.string.product_detail_availability), product.availabilityStatus
                )
                DetailRow(
                    stringResource(R.string.product_detail_minimum_order),
                    product.minimumOrderQuantity.toString()
                )
                DetailRow(
                    stringResource(R.string.product_detail_weight), "${product.weight}"
                )
            }

            DetailSection(
                title = stringResource(R.string.product_detail_dimensions)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DimensionCard(
                        label = stringResource(R.string.product_detail_width),
                        value = product.dimensions.width.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    DimensionCard(
                        label = stringResource(R.string.product_detail_height),
                        value = product.dimensions.height.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    DimensionCard(
                        label = stringResource(R.string.product_detail_depth),
                        value = product.dimensions.depth.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            DetailSection(
                title = stringResource(R.string.product_detail_delivery_returns)
            ) {
                DetailRow(
                    stringResource(R.string.product_detail_shipping), product.shippingInformation
                )
                DetailRow(
                    stringResource(R.string.product_detail_warranty), product.warrantyInformation
                )
                DetailRow(
                    stringResource(R.string.product_detail_returns), product.returnPolicy
                )
            }

            if (product.tags.isNotEmpty()) {
                DetailSection(
                    title = stringResource(R.string.product_detail_explore_tags)
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(end = 4.dp)
                    ) {
                        items(
                            items = product.tags, key = { it }) { tag ->
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp, vertical = 9.dp
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }

            if (product.reviews.isNotEmpty()) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                )

                DetailSection(
                    title = stringResource(
                        R.string.product_detail_customer_reviews
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star, contentDescription = stringResource(
                                R.string.product_detail_average_rating
                            ), modifier = Modifier.size(24.dp), tint = RatingGold
                        )

                        Text(
                            text = "%.2f".format(Locale.US, product.rating),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = stringResource(
                                R.string.product_detail_reviews_count, product.reviews.size
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        product.reviews.forEach { review ->
                            ReviewCard(
                                reviewerName = review.reviewerName,
                                rating = review.rating,
                                comment = review.comment,
                                date = review.date
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductHero(
    product: ProductsUIModel, onBackClick: () -> Unit
) {
    val galleryImages = remember(
        product.id, product.thumbnail, product.images
    ) {
        (listOf(product.thumbnail) + product.images).filter { it.isNotBlank() }.distinct()
    }

    var selectedImage by remember(product.id) {
        mutableStateOf(product.thumbnail)
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.95f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = selectedImage,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentScale = ContentScale.Fit
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface
            ) {
                IconButton(
                    onClick = onBackClick, modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            R.string.product_detail_back
                        ),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (product.discountPercentage > 0.0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(DiscountGradient)
                ) {
                    Text(
                        text = stringResource(
                            R.string.product_detail_discount, String.format(
                                Locale.US, "%.1f", product.discountPercentage
                            ).removeSuffix(".0")
                        ),
                        modifier = Modifier.padding(
                            horizontal = 10.dp, vertical = 7.dp
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        if (galleryImages.size > 1) {
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = galleryImages, key = { it }) { image ->
                    val isSelected = selectedImage == image

                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp, color = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.4f
                                    )
                                }, shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                selectedImage = image
                            }
                            .padding(5.dp), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = image, contentDescription = stringResource(
                                R.string.product_detail_view_image, product.title
                            ), modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductSummary(product: ProductsUIModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = product.category.uppercase(Locale.ROOT),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Star, contentDescription = stringResource(
                    R.string.product_detail_rating
                ), modifier = Modifier.size(19.dp), tint = RatingGold
            )

            Text(
                text = "%.2f".format(Locale.US, product.rating),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "·", color = MaterialTheme.colorScheme.outline
            )

            Text(
                text = stringResource(
                    R.string.product_detail_reviews_count, product.reviews.size
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.product_detail_price, "%.2f".format(Locale.US, product.discountedPrice)
                ),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            if (product.discountPercentage > 0.0) {
                Text(
                    text = stringResource(
                        R.string.product_detail_price, "%.2f".format(Locale.US, product.price)
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = TextDecoration.LineThrough,
                    maxLines = 1
                )
            }
        }

        if (product.discountPercentage > 0.0) {
            Text(
                text = stringResource(
                    R.string.product_detail_savings, "%.2f".format(
                        Locale.US, product.price - product.discountedPrice
                    ), String.format(
                        Locale.US, "%.1f", product.discountPercentage
                    ).removeSuffix(".0")
                ),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (product.stock > 0) {
                    Icons.Filled.CheckCircle
                } else {
                    Icons.Filled.Cancel
                },
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (product.stock > 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )

            Text(
                text = if (product.stock > 0) {
                    stringResource(
                        R.string.product_detail_stock_count, product.stock
                    )
                } else {
                    stringResource(R.string.product_detail_out_of_stock)
                }, style = MaterialTheme.typography.bodyMedium, color = if (product.stock > 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        if (product.stock > 0) {
            Text(
                text = product.shippingInformation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DetailSection(
    title: String, content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        content()
    }
}

@Composable
private fun DetailRow(
    label: String, value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier.width(112.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DimensionCard(
    label: String, value: String, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 14.dp, horizontal = 6.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReviewCard(
    reviewerName: String, rating: Int, comment: String, date: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reviewerName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Star, contentDescription = stringResource(
                            R.string.product_detail_review_rating
                        ), modifier = Modifier.size(16.dp), tint = RatingGold
                    )

                    Text(
                        text = rating.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = date.toDisplayDate(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun String.toDisplayDate(): String = try {
    val parser = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val formatter = SimpleDateFormat(
        "dd MMM yyyy", Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getDefault()
    }

    formatter.format(parser.parse(this)!!)
} catch (_: Exception) {
    this
}
