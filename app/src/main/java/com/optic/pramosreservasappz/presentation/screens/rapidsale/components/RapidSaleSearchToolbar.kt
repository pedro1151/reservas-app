package com.optic.pramosreservasappz.presentation.screens.rapidsale.components


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.sales.Components.SGray400
import com.optic.pramosreservasappz.presentation.sales.Components.SRed
import com.optic.pramosreservasappz.presentation.screens.productos.ProductViewType

import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.MiniCart
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.NewRapidProduct
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.RapidProductCard
import com.optic.pramosreservasappz.presentation.screens.rapidsale.components.RapidSaleSearchBar
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground
import kotlinx.coroutines.launch
import java.time.format.TextStyle

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Blue700  = Color(0xFF1D4ED8)
private val Blue600  = Color(0xFF2563EB)
private val Blue500  = Color(0xFF3B82F6)
private val Blue400  = Color(0xFF60A5FA)
private val Blue100  = Color(0xFFDBEAFE)
private val Blue50   = Color(0xFFEFF6FF)
private val Slate900 = Color(0xFF0F172A)
private val Slate600 = Color(0xFF475569)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)
private val Slate100 = Color(0xFFF1F5F9)
private val Red500   = Color(0xFFEF4444)
private val PageBg   = Color(0xFFF8FAFC)

@Composable
fun RapidSaleSearchToolbar(
    query            : String,
    onQueryChange    : (String) -> Unit,
    hasQuery         : Boolean,
    totalCount       : Int,
    filteredCount    : Int,
    viewType         : ProductViewType,
    onViewTypeChange : (ProductViewType) -> Unit,
    onAddClick       : () -> Unit // 🔥 NUEVO
) {
    val focusManager = LocalFocusManager.current
    var isFocused    by remember { mutableStateOf(false) }

    val badgeText = if (hasQuery) "$filteredCount / $totalCount" else "$totalCount"

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── Search field ──
        Row(
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation    = if (isFocused) 4.dp else 1.dp,
                    shape        = RoundedCornerShape(16.dp),
                    ambientColor = Blue500.copy(alpha = if (isFocused) 0.10f else 0.03f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(
                    width = if (isFocused) 1.5.dp else 1.dp,
                    color = if (isFocused) Blue500.copy(alpha = 0.50f) else Slate200,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Icon(
                Icons.Outlined.Search, null,
                tint     = if (isFocused) Blue600 else Slate400,
                modifier = Modifier.size(17.dp)
            )

            BasicTextField(
                value           = query,
                onValueChange   = onQueryChange,
                modifier        = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle       = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = Slate900,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush     = SolidColor(Blue600),
                singleLine      = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                decorationBox   = { inner ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                "Buscar en catálogo...",
                                fontSize = 14.sp,
                                color    = Slate400
                            )
                        }
                        inner()
                    }
                }
            )

            // Count badge
            AnimatedContent(
                targetState  = badgeText,
                transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(130)) },
                label        = "badge_count"
            ) { label ->
                Box(
                    modifier = Modifier
                        .background(
                            if (hasQuery) Blue50 else Slate100,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        label,
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color      = if (hasQuery) Blue600 else Slate400,
                        letterSpacing = 0.2.sp
                    )
                }
            }

            // Clear button
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter   = scaleIn(tween(150)) + fadeIn(tween(150)),
                exit    = scaleOut(tween(100)) + fadeOut(tween(100))
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Slate200)
                        .clickable(remember { MutableInteractionSource() }, null) {
                            onQueryChange("")
                            focusManager.clearFocus()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Close, "Limpiar",
                        tint     = Slate600,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }

        // ── View type toggle ──
        Row(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .border(1.dp, Slate200, RoundedCornerShape(14.dp))
        ) {
            ViewToggleButton(
                icon       = Icons.Outlined.ViewList,
                isSelected = viewType == ProductViewType.LIST,
                onClick    = { onViewTypeChange(ProductViewType.LIST) },
                isStart    = true
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(Slate200)
                    .align(Alignment.CenterVertically)
            )
            ViewToggleButton(
                icon       = Icons.Outlined.GridView,
                isSelected = viewType == ProductViewType.GRID,
                onClick    = { onViewTypeChange(ProductViewType.GRID) },
                isStart    = false
            )
        }

        // 🔥 NUEVO BOTÓN +
        Box(
            modifier = Modifier
                .size(42.dp)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(Blue600)
                .clickable { onAddClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Nuevo producto",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ViewToggleButton(
    icon       : androidx.compose.ui.graphics.vector.ImageVector,
    isSelected : Boolean,
    onClick    : () -> Unit,
    isStart    : Boolean
) {
    val bgColor  by animateColorAsState(
        targetValue   = if (isSelected) Blue600 else Color.Transparent,
        animationSpec = tween(200),
        label         = "toggleBg"
    )
    val iconTint by animateColorAsState(
        targetValue   = if (isSelected) Color.White else Slate400,
        animationSpec = tween(200),
        label         = "toggleTint"
    )
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(
                RoundedCornerShape(
                    topStart    = if (isStart) 14.dp else 0.dp,
                    bottomStart = if (isStart) 14.dp else 0.dp,
                    topEnd      = if (!isStart) 14.dp else 0.dp,
                    bottomEnd   = if (!isStart) 14.dp else 0.dp
                )
            )
            .background(bgColor)
            .clickable(remember { MutableInteractionSource() }, null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = iconTint, modifier = Modifier.size(18.dp))
    }
}