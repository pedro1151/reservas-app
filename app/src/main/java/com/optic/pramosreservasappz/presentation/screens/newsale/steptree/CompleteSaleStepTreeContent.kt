package com.optic.pramosreservasappz.presentation.screens.newsale.steptree

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.MiniProductResponse
import com.optic.pramosreservasappz.domain.model.sales.types.SaleType
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.screens.newsale.steptree.components.ResumeHeroCard
import com.optic.pramosreservasappz.presentation.screens.newsale.steptree.components.ResumenClientSummaryBlock
import com.optic.pramosreservasappz.presentation.screens.newsale.steptree.components.ResumenPaymentSummaryBlock
import com.optic.pramosreservasappz.presentation.screens.newsale.steptree.components.ResumenProductSummaryRow
import com.optic.pramosreservasappz.presentation.screens.newsale.steptree.components.ResumenSummaryInfoBlock
import com.optic.pramosreservasappz.presentation.screens.newsale.steptree.components.ResumenTotalSummaryBar
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

@Composable
fun CompleteSaleStepTreeContent(
    selectedProducts: List<Pair<MiniProductResponse, Int>>,
    paddingValues: PaddingValues,
    total: Double,
    totalItems: Int,
    viewModel: NewSaleViewModel,
    navController: NavHostController
) {
    val primary = MaterialTheme.colorScheme.primary
    val background = MaterialTheme.colorScheme.background
    val surface = MaterialTheme.colorScheme.surface
    val borderSoft = BorderGray.copy(alpha = 0.62f)

    val accent = Color(0xFFFFC233)
    val accentSoft = Color(0xFFFFF6D8)
    val neutralSoft = Color(0xFFFAFBFC)

    val isRapidSale = viewModel.saleFlowType == SaleType.RAPID

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 14.dp,
                bottom = 26.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ResumeHeroCard(
                    total = total,
                    totalItems = totalItems,
                    borderSoft = borderSoft,
                    accent = accent,
                    accentSoft = accentSoft
                )
            }

            item {
                SaleInfoSummaryCard(
                    isRapidSale = isRapidSale,
                    saleName = viewModel.saleName,
                    selectedClientId = viewModel.selectedClientId,
                    selectedClientName = viewModel.selectedClientName,
                    selectedClientEmail = viewModel.selectedClientEmail,
                    selectedClientPhone = viewModel.selectedClientPhone,
                    paymentMethod = viewModel.paymentMethod,
                    primary = primary,
                    surface = surface,
                    borderSoft = borderSoft,
                    accent = accent,
                    accentSoft = accentSoft,
                    neutralSoft = neutralSoft,
                    navController = navController
                )
            }

            item {
                ProductsSectionHeader(
                    totalItems = totalItems,
                    accent = accent,
                    accentSoft = accentSoft
                )
            }

            items(
                items = selectedProducts,
                key = { it.first.id }
            ) { (product, quantity) ->
                ResumenProductSummaryRow(
                    product = product,
                    quantity = quantity,
                    subtotal = product.price * quantity,
                    borderSoft = borderSoft,
                    accentSoft = accentSoft
                )
            }

            item {
                Spacer(Modifier.height(88.dp))
            }
        }

        ResumenTotalSummaryBar(
            total = total,
            totalItems = totalItems,
            primary = primary,
            surface = surface
        )
    }
}



@Composable
private fun ProductsSectionHeader(
    totalItems: Int,
    accent: Color,
    accentSoft: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, start = 2.dp, end = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Productos",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = "$totalItems items agregados",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
        }

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(accentSoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                tint = Color(0xFF9A6A00),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SaleInfoSummaryCard(
    isRapidSale: Boolean,
    saleName: String,
    selectedClientId: Int?,
    selectedClientName: String?,
    selectedClientEmail: String?,
    selectedClientPhone: String?,
    paymentMethod: String?,
    primary: Color,
    surface: Color,
    borderSoft: Color,
    accent: Color,
    accentSoft: Color,
    neutralSoft: Color,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.025f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = borderSoft.copy(alpha = 0.55f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(28.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(neutralSoft)
                .border(
                    width = 1.dp,
                    color = borderSoft.copy(alpha = 0.65f),
                    shape = RoundedCornerShape(999.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isRapidSale) "Venta rápida" else "Venta completa",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (!isRapidSale) {
            Spacer(Modifier.height(14.dp))

            ResumenSummaryInfoBlock(
                label = "Nombre de venta",
                value = saleName.ifBlank { "Sin nombre" }
            )
        }

        Spacer(Modifier.height(14.dp))

        ResumenClientSummaryBlock(
            selectedClientId = selectedClientId,
            selectedClientName = selectedClientName,
            selectedClientEmail = selectedClientEmail,
            selectedClientPhone = selectedClientPhone,
            primary = primary,
            borderSoft = borderSoft,
            accentSoft = accentSoft,
            neutralSoft = neutralSoft,
            navController = navController
        )

        if (!isRapidSale && paymentMethod != null) {
            Spacer(Modifier.height(12.dp))

            ResumenPaymentSummaryBlock(
                paymentMethod = paymentMethod,
                borderSoft = borderSoft,
                accent = accent,
                accentSoft = accentSoft,
                neutralSoft = neutralSoft
            )
        }
    }
}

