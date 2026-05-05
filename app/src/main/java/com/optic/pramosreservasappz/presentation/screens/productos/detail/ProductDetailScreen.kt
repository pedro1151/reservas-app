package com.optic.pramosreservasappz.presentation.screens.productos.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.productos.ProductViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary
import com.optic.pramosreservasappz.presentation.util.getAvatarColor

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
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        is Resource.Success -> {
            ProductDetailContent(
                product = state.data,
                navController = navController
            )
        }

        is Resource.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se pudo cargar el producto",
                    color = TextSecondary,
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
    val primary = MaterialTheme.colorScheme.primary
    val avatarColor = remember(product.id) { getAvatarColor(product.id) }
    var expanded by remember { mutableStateOf(false) }

    val rotate by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(180),
        label = "expandRotation"
    )

    val priceText = remember(product.price) {
        "Bs. %,.0f".format(product.price)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextPrimary
                        )
                    }
                },
                title = {},
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(
                                ClientScreen.ABMServicio.createRoute(
                                    serviceId = product.id,
                                    editable = true
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "Más opciones",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = Color.Black.copy(alpha = 0.04f),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    )
                    .clip(RoundedCornerShape(28.dp))
                    .background(avatarColor.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = avatarColor.copy(alpha = 0.90f),
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = product.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = priceText,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )

            Spacer(Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TypeChip(
                    text = "Producto",
                    selected = true
                )

                TypeChip(
                    text = "Servicio",
                    selected = false
                )
            }

            Spacer(Modifier.height(22.dp))

            DetailCard {
                DetailSectionTitle("Descripción")

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Producto disponible para venta rápida dentro del catálogo de tu negocio.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            }

            Spacer(Modifier.height(14.dp))

            DetailCard {
                DetailRow(
                    label = "Nombre",
                    value = product.name
                )

                DetailDivider()

                DetailRow(
                    label = "Precio",
                    value = priceText
                )

                DetailDivider()

                DetailRow(
                    label = "Estado",
                    value = if (product.isActive) "Activo" else "Inactivo",
                    valueColor = if (product.isActive) ButtonSucessColor else TextSecondary
                )

                if (product.stock != null) {
                    DetailDivider()

                    DetailRow(
                        label = "Stock",
                        value = product.stock.toString()
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.03f),
                        spotColor = Color.Black.copy(alpha = 0.07f)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            expanded = !expanded
                        }
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Información adicional",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier
                            .size(22.dp)
                            .rotate(rotate)
                    )
                }

                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(tween(220)) + fadeIn(tween(180)),
                    exit = shrinkVertically(tween(180)) + fadeOut(tween(120))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp)
                    ) {
                        DetailDivider()

                        DetailRow(
                            label = "Creado por",
                            value = product.createdBy ?: "-"
                        )

                        DetailDivider()

                        DetailRow(
                            label = "Fecha creación",
                            value = product.created
                        )

                        DetailDivider()

                        DetailRow(
                            label = "Actualizado por",
                            value = product.updatedBy ?: "-"
                        )

                        DetailDivider()

                        DetailRow(
                            label = "Fecha actualización",
                            value = product.updated
                        )

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun DetailCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.03f),
                spotColor = Color.Black.copy(alpha = 0.07f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(18.dp),
        content = content
    )
}

@Composable
private fun DetailSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Black,
        color = TextPrimary
    )
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(105.dp)
        )

        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DetailDivider() {
    HorizontalDivider(
        color = BorderGray.copy(alpha = 0.65f),
        thickness = 0.8.dp
    )
}

@Composable
private fun TypeChip(
    text: String,
    selected: Boolean
) {
    val primary = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (selected) primary.copy(alpha = 0.10f)
                else Color(0xFFF8FAFC)
            )
            .padding(horizontal = 13.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(
                    if (selected) primary.copy(alpha = 0.16f)
                    else BorderGray.copy(alpha = 0.75f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        Spacer(Modifier.width(7.dp))

        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) primary else TextSecondary
        )
    }
}