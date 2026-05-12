package com.optic.pramozventicoappz.presentation.screens.productos.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.product.ProductResponse
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.productos.ProductViewModel
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

// ── Tokens locales ───────────────────────────────────────────────────────────
private val PageBg = Color(0xFFFAFAFA)

@Composable
fun ProductDetailScreen(
    navController: NavHostController,
    productId: Int,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val productState by viewModel.oneProductState

    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }

    when (val state = productState) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PageBg),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color       = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp,
                    modifier    = Modifier.size(28.dp)
                )
            }
        }

        is Resource.Success -> {
            ProductDetailContent(
                product       = state.data,
                navController = navController
            )
        }

        is Resource.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PageBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text     = "No se pudo cargar el producto",
                    color    = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailContent(
    product: ProductResponse,
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val primary     = MaterialTheme.colorScheme.primary
    var expanded    by remember { mutableStateOf(false) }

    val rotate by animateFloatAsState(
        targetValue   = if (expanded) 180f else 0f,
        animationSpec = tween(200),
        label         = "expandRotation"
    )

    val heroTint  = primary.copy(alpha = 0.10f)
    val priceText = remember(product.price) { "%,.0f".format(product.price) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    RoundIconButton(
                        icon = Icons.Outlined.ArrowBack,
                        contentDescription = "Volver",
                        onClick = { navController.popBackStack() }
                    )
                },
                title = {},
                actions = {
                    RoundIconButton(
                        icon = Icons.Outlined.MoreVert,
                        contentDescription = "Más opciones",
                        onClick = { }
                    )
                    Spacer(Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = heroTint
                )
            )
        },
        bottomBar = {
            BottomActionBar(
                primary = primary,
                onEdit  = {
                    navController.navigate(
                        ClientScreen.ABMServicio.createRoute(
                            serviceId = product.id,
                            editable  = true
                        )
                    )
                }
            )
        },
        containerColor = PageBg
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {

            // ── HERO ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                heroTint,
                                primary.copy(alpha = 0.04f),
                                PageBg
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Blobs decorativos sutiles
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .offset(x = (-50).dp, y = (-30).dp)
                        .clip(CircleShape)
                        .background(primary.copy(alpha = 0.05f))
                        .align(Alignment.TopStart)
                )
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .offset(x = 30.dp, y = 20.dp)
                        .clip(CircleShape)
                        .background(primary.copy(alpha = 0.04f))
                        .align(Alignment.BottomEnd)
                )

                // Ícono principal flotante
                Box(
                    modifier = Modifier
                        .size(124.dp)
                        .shadow(
                            elevation    = 24.dp,
                            shape        = RoundedCornerShape(34.dp),
                            ambientColor = primary.copy(alpha = 0.25f),
                            spotColor    = primary.copy(alpha = 0.40f)
                        )
                        .clip(RoundedCornerShape(34.dp))
                        .background(Color.White)
                        .border(1.dp, primary.copy(alpha = 0.10f), RoundedCornerShape(34.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(74.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        primary.copy(alpha = 0.18f),
                                        primary.copy(alpha = 0.08f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Inventory2,
                            contentDescription = null,
                            tint     = primary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            // ── CONTENIDO ───────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                // Status badge
                StatusBadge(isActive = product.isActive)

                Spacer(Modifier.height(12.dp))

                // Nombre del producto
                Text(
                    text          = product.name,
                    fontSize      = 26.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    color         = TextPrimary,
                    lineHeight    = 32.sp,
                    letterSpacing = (-0.6).sp,
                    maxLines      = 3,
                    overflow      = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text       = "Producto · Disponible en catálogo",
                    fontSize   = 13.sp,
                    color      = TextSecondary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(20.dp))

                // Precio gigante
                Row(
                    verticalAlignment     = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text       = "Bs.",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = primary,
                        modifier   = Modifier.padding(bottom = 7.dp)
                    )
                    Text(
                        text          = priceText,
                        fontSize      = 38.sp,
                        fontWeight    = FontWeight.Black,
                        color         = primary,
                        letterSpacing = (-1.2).sp,
                        lineHeight    = 42.sp
                    )
                }

                Spacer(Modifier.height(22.dp))

                // Cards de info rápida
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickInfoCard(
                        modifier = Modifier.weight(1f),
                        icon     = Icons.Outlined.Category,
                        label    = "Tipo",
                        value    = "Producto",
                        accent   = primary
                    )
                    QuickInfoCard(
                        modifier = Modifier.weight(1f),
                        icon     = Icons.Rounded.Bolt,
                        label    = "Stock",
                        value    = product.stock?.toString() ?: "—",
                        accent   = primary
                    )
                    QuickInfoCard(
                        modifier = Modifier.weight(1f),
                        icon     = Icons.Outlined.CheckCircle,
                        label    = "Estado",
                        value    = if (product.isActive) "Activo" else "Inactivo",
                        accent   = if (product.isActive) ButtonSucessColor else TextSecondary
                    )
                }

                Spacer(Modifier.height(22.dp))

                // Card de descripción
                DetailCard {
                    SectionHeader(
                        icon    = Icons.Outlined.Inventory2,
                        title   = "Descripción",
                        primary = primary
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text       = "Producto disponible para venta rápida dentro del catálogo de tu negocio.",
                        fontSize   = 14.sp,
                        color      = TextSecondary,
                        lineHeight = 22.sp
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Card expandible — Auditoría
                DetailCard(padding = 0.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication        = null
                            ) { expanded = !expanded }
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(primary.copy(alpha = 0.10f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.CalendarMonth, null,
                                tint     = primary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text          = "Información del registro",
                                fontSize      = 15.sp,
                                fontWeight    = FontWeight.ExtraBold,
                                color         = TextPrimary,
                                letterSpacing = (-0.2).sp
                            )
                            Text(
                                text     = if (expanded) "Toca para ocultar" else "Toca para ver más",
                                fontSize = 12.sp,
                                color    = TextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (expanded) primary.copy(alpha = 0.10f)
                                    else BorderGray.copy(alpha = 0.50f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ExpandMore,
                                contentDescription = null,
                                tint     = if (expanded) primary else TextSecondary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .rotate(rotate)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter   = expandVertically(tween(240)) + fadeIn(tween(200)),
                        exit    = shrinkVertically(tween(200)) + fadeOut(tween(140))
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                start  = 18.dp,
                                end    = 18.dp,
                                bottom = 14.dp
                            )
                        ) {
                            HorizontalDivider(
                                color     = BorderGray.copy(alpha = 0.55f),
                                thickness = 0.8.dp,
                                modifier  = Modifier.padding(bottom = 4.dp)
                            )
                            AuditRow(
                                icon  = Icons.Outlined.Person,
                                label = "Creado por",
                                value = product.createdBy ?: "—"
                            )
                            AuditRow(
                                icon  = Icons.Outlined.CalendarMonth,
                                label = "Fecha de creación",
                                value = product.created
                            )
                            AuditRow(
                                icon  = Icons.Outlined.Person,
                                label = "Actualizado por",
                                value = product.updatedBy ?: "—"
                            )
                            AuditRow(
                                icon  = Icons.Outlined.Update,
                                label = "Última actualización",
                                value = product.updated
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ─── Helpers UI ──────────────────────────────────────────────────────────────

@Composable
private fun RoundIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 12.dp)
            .size(40.dp)
            .shadow(4.dp, CircleShape, ambientColor = Color.Black.copy(alpha = 0.06f))
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = contentDescription,
            tint               = TextPrimary,
            modifier           = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun StatusBadge(isActive: Boolean) {
    val color = if (isActive) ButtonSucessColor else TextSecondary
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(color.copy(alpha = 0.10f))
            .border(1.dp, color.copy(alpha = 0.22f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text          = if (isActive) "ACTIVO" else "INACTIVO",
            fontSize      = 10.sp,
            fontWeight    = FontWeight.ExtraBold,
            color         = color,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun QuickInfoCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    accent: Color
) {
    Column(
        modifier = modifier
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.02f),
                spotColor    = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, BorderGray.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(accent.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = accent, modifier = Modifier.size(16.dp))
        }
        Text(
            text          = label,
            fontSize      = 11.sp,
            color         = TextSecondary,
            fontWeight    = FontWeight.Medium,
            letterSpacing = 0.3.sp
        )
        Text(
            text          = value,
            fontSize      = 14.sp,
            fontWeight    = FontWeight.Bold,
            color         = TextPrimary,
            maxLines      = 1,
            overflow      = TextOverflow.Ellipsis,
            letterSpacing = (-0.2).sp
        )
    }
}

@Composable
private fun DetailCard(
    padding: Dp = 18.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 6.dp,
                shape        = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor    = Color.Black.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .padding(padding),
        content = content
    )
}

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    primary: Color
) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(primary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = primary, modifier = Modifier.size(16.dp))
        }
        Text(
            text          = title,
            fontSize      = 15.sp,
            fontWeight    = FontWeight.ExtraBold,
            color         = TextPrimary,
            letterSpacing = (-0.2).sp
        )
    }
}

@Composable
private fun AuditRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(BorderGray.copy(alpha = 0.40f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, null,
                tint     = TextSecondary,
                modifier = Modifier.size(14.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = label,
                fontSize   = 11.sp,
                color      = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text       = value,
                fontSize   = 13.sp,
                color      = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BottomActionBar(
    primary: Color,
    onEdit: () -> Unit
) {
    Surface(
        color           = Color.White,
        shadowElevation = 14.dp,
        modifier        = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation    = 12.dp,
                        shape        = RoundedCornerShape(16.dp),
                        ambientColor = primary.copy(alpha = 0.30f),
                        spotColor    = primary.copy(alpha = 0.40f)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(primary, primary.copy(alpha = 0.88f))
                        )
                    )
                    .clickable(onClick = onEdit),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null,
                        tint     = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text          = "Editar producto",
                        color         = Color.White,
                        fontSize      = 15.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }
    }
}