
package com.optic.pramosreservasappz.presentation.screens.newsale.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.compose.ui.platform.LocalFocusManager
import com.optic.pramosreservasappz.presentation.screens.productos.ProductViewType

// ─────────────────────────────────────────────────────────────
// 🎨 PEDIDOS YA STYLE TOKENS
// ─────────────────────────────────────────────────────────────
private val Primary = Color(0xFFE91E63)
private val PrimaryDark = Color(0xFFD81B60)
private val PrimarySoft = Color(0xFFFCE4EC)

private val Accent = Color(0xFFFFC107)

private val TextPrimary = Color(0xFF1F2937)
private val TextSecondary = Color(0xFF6B7280)
private val TextHint = Color(0xFF9CA3AF)

private val BorderSoft = Color(0xFFF1D5E0)
private val BorderNeutral = Color(0xFFE5E7EB)

private val Surface = Color.White
private val SurfaceSoft = Color(0xFFFFF7FA)
private val PageBg = Color(0xFFFFFBFC)

@Composable
fun RapidSaleSearchToolbar(
    query: String,
    onQueryChange: (String) -> Unit,
    hasQuery: Boolean,
    totalCount: Int,
    filteredCount: Int,
    viewType: ProductViewType,
    onViewTypeChange: (ProductViewType) -> Unit,
    onAddClick: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    val badgeText =
        if (hasQuery) "$filteredCount / $totalCount"
        else "$totalCount"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // 🔎 SEARCH
        Row(
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation = if (isFocused) 5.dp else 1.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Primary.copy(alpha = 0.10f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Surface)
                .border(
                    width = if (isFocused) 1.4.dp else 1.dp,
                    color = if (isFocused) Primary.copy(alpha = 0.45f) else BorderNeutral,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {

            Icon(
                Icons.Outlined.Search,
                contentDescription = null,
                tint = if (isFocused) Primary else TextHint,
                modifier = Modifier.size(17.dp)
            )

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush = SolidColor(Primary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { focusManager.clearFocus() }
                ),
                decorationBox = { inner ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "Buscar productos...",
                                fontSize = 14.sp,
                                color = TextHint
                            )
                        }
                        inner()
                    }
                }
            )

            // 🔢 BADGE
            AnimatedContent(
                targetState = badgeText,
                transitionSpec = {
                    fadeIn(tween(180)) togetherWith
                            fadeOut(tween(130))
                },
                label = "badge_count"
            ) { label ->

                Box(
                    modifier = Modifier
                        .background(
                            if (hasQuery) PrimarySoft else SurfaceSoft,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (hasQuery) Primary else TextSecondary
                    )
                }
            }

            // ❌ CLEAR
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = scaleIn(tween(150)) + fadeIn(tween(150)),
                exit = scaleOut(tween(100)) + fadeOut(tween(100))
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(BorderSoft)
                        .clickable(
                            remember { MutableInteractionSource() },
                            null
                        ) {
                            onQueryChange("")
                            focusManager.clearFocus()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "Limpiar",
                        tint = PrimaryDark,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }

        // 🔄 TOGGLE VIEW
        Row(
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(Surface)
                .border(
                    1.dp,
                    BorderNeutral,
                    RoundedCornerShape(14.dp)
                )
        ) {

            ViewToggleButton(
                icon = Icons.Outlined.ViewList,
                isSelected = viewType == ProductViewType.LIST,
                onClick = {
                    onViewTypeChange(ProductViewType.LIST)
                },
                isStart = true
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(BorderNeutral)
            )

            ViewToggleButton(
                icon = Icons.Outlined.GridView,
                isSelected = viewType == ProductViewType.GRID,
                onClick = {
                    onViewTypeChange(ProductViewType.GRID)
                },
                isStart = false
            )
        }

        // ➕ ADD BUTTON
        Box(
            modifier = Modifier
                .size(42.dp)
                .shadow(
                    4.dp,
                    CircleShape,
                    ambientColor = Primary.copy(alpha = 0.25f)
                )
                .clip(CircleShape)
                .background(Primary)
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    isStart: Boolean
) {

    val bgColor by animateColorAsState(
        targetValue =
        if (isSelected) Primary
        else Color.Transparent,
        animationSpec = tween(200),
        label = "toggleBg"
    )

    val iconTint by animateColorAsState(
        targetValue =
        if (isSelected) Color.White
        else TextHint,
        animationSpec = tween(200),
        label = "toggleTint"
    )

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(
                RoundedCornerShape(
                    topStart = if (isStart) 14.dp else 0.dp,
                    bottomStart = if (isStart) 14.dp else 0.dp,
                    topEnd = if (!isStart) 14.dp else 0.dp,
                    bottomEnd = if (!isStart) 14.dp else 0.dp
                )
            )
            .background(bgColor)
            .clickable(
                remember { MutableInteractionSource() },
                null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
    }
}