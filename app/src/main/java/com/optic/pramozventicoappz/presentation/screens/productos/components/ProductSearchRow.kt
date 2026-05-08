package com.optic.pramozventicoappz.presentation.screens.productos.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import com.optic.pramozventicoappz.domain.model.product.ProductViewType
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.GrisSuave
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

@Composable
fun ProductSearchRow(
    query: String,
    onQueryChange: (String) -> Unit,
    hasQuery: Boolean,
    totalCount: Int,
    filteredCount: Int,
    viewType: ProductViewType,
    onViewTypeChange: (ProductViewType) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val focusManager = LocalFocusManager.current

    var isFocused by remember { mutableStateOf(false) }

    val badgeText = if (hasQuery) "$filteredCount/$totalCount" else "$totalCount"

    val searchBorderColor by animateColorAsState(
        targetValue = if (isFocused) primary.copy(alpha = 0.65f) else BorderGray,
        animationSpec = tween(180),
        label = "searchBorderColor"
    )

    val searchElevation by animateDpAsState(
        targetValue = if (isFocused) 5.dp else 1.dp,
        animationSpec = tween(180),
        label = "searchElevation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation = searchElevation,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = primary.copy(alpha = if (isFocused) 0.10f else 0.03f),
                    spotColor = primary.copy(alpha = if (isFocused) 0.08f else 0.02f)
                )
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .border(
                    width = if (isFocused) 1.4.dp else 1.dp,
                    color = searchBorderColor,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 13.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = if (isFocused) primary else TextSecondary.copy(alpha = 0.65f),
                modifier = Modifier.size(18.dp)
            )

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Normal
                ),
                cursorBrush = SolidColor(primary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { focusManager.clearFocus() }
                ),
                decorationBox = { inner ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "Buscar producto...",
                                fontSize = 14.sp,
                                color = TextSecondary.copy(alpha = 0.58f)
                            )
                        }
                        inner()
                    }
                }
            )

            AnimatedContent(
                targetState = badgeText,
                transitionSpec = {
                    fadeIn(tween(160)) + scaleIn(tween(160), initialScale = 0.92f) togetherWith
                            fadeOut(tween(110)) + scaleOut(tween(110), targetScale = 0.92f)
                },
                label = "badge_count"
            ) { label ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            if (hasQuery) primary.copy(alpha = 0.10f)
                            else GrisSuave.copy(alpha = 0.70f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (hasQuery) primary.copy(alpha = 0.18f) else BorderGray,
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (hasQuery) primary else TextSecondary,
                        letterSpacing = 0.1.sp
                    )
                }
            }

            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = scaleIn(tween(140), initialScale = 0.75f) + fadeIn(tween(140)),
                exit = scaleOut(tween(90), targetScale = 0.75f) + fadeOut(tween(90))
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(TextSecondary.copy(alpha = 0.12f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onQueryChange("")
                            focusManager.clearFocus()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Limpiar búsqueda",
                        tint = TextSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Black.copy(alpha = 0.03f),
                    spotColor = Color.Black.copy(alpha = 0.05f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = BorderGray,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ViewToggleButton(
                icon = Icons.AutoMirrored.Outlined.ViewList,
                isSelected = viewType == ProductViewType.LIST,
                onClick = { onViewTypeChange(ProductViewType.LIST) },
                isStart = true
            )

            Spacer(Modifier.width(3.dp))

            ViewToggleButton(
                icon = Icons.Outlined.GridView,
                isSelected = viewType == ProductViewType.GRID,
                onClick = { onViewTypeChange(ProductViewType.GRID) },
                isStart = false
            )
        }
    }
}

@Composable
private fun ViewToggleButton(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    isStart: Boolean
) {
    val primary = MaterialTheme.colorScheme.primary

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) primary else Color.Transparent,
        animationSpec = tween(180),
        label = "toggleBg"
    )

    val iconTint by animateColorAsState(
        targetValue = if (isSelected) Color.White else TextSecondary,
        animationSpec = tween(180),
        label = "toggleTint"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1f,
        animationSpec = spring(),
        label = "toggleScale"
    )

    Box(
        modifier = Modifier
            .size(38.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(
                RoundedCornerShape(
                    topStart = if (isStart) 13.dp else 11.dp,
                    bottomStart = if (isStart) 13.dp else 11.dp,
                    topEnd = if (!isStart) 13.dp else 11.dp,
                    bottomEnd = if (!isStart) 13.dp else 11.dp
                )
            )
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
    }
}