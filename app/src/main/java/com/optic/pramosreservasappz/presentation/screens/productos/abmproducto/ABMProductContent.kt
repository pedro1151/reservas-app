package com.optic.pramosreservasappz.presentation.screens.productos.abmproducto

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.screens.productos.ProductViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

@Composable
fun ABMProductContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    productId: Int? = null,
    editable: Boolean = false,
    viewModel: ProductViewModel
) {
    val scrollState = rememberScrollState()

    val formState by viewModel.formState.collectAsState()
    val createState = viewModel.createProductState.value
    val updateState = viewModel.updateProductState.value
    val productState = viewModel.oneProductState.value

    LaunchedEffect(productState) {
        if (productState is Resource.Success && editable) {
            val p = productState.data

            viewModel.onFormChange(
                formState.copy(
                    name = p.name,
                    price = p.price.toString()
                )
            )
        }
    }

    LaunchedEffect(createState) {
        if (createState is Resource.Success) {
            viewModel.resetCreateState()
            navController.popBackStack()
        }
    }

    LaunchedEffect(updateState) {
        if (updateState is Resource.Success) {
            viewModel.resetUpdateState()
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(paddingValues)
            .verticalScroll(scrollState)
            .padding(horizontal = 22.dp)
    ) {
        Spacer(Modifier.height(18.dp))

        Text(
            text = if (editable) "Actualiza los datos de tu producto" else "Agrega un nuevo producto o servicio",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = (-0.4).sp
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Completa la información que verá tu negocio al momento de vender.",
            fontSize = 14.sp,
            color = TextSecondary,
            lineHeight = 20.sp
        )

        Spacer(Modifier.height(26.dp))

        SectionTitle("Información básica")

        FormSection {
            FieldLabel("Nombre", required = true)

            ProductDefaultField(
                value = formState.name,
                onValueChange = {
                    viewModel.onFormChange(formState.copy(name = it))
                },
                placeholder = "Ej: Hamburguesa doble"
            )

            Spacer(Modifier.height(20.dp))

            FieldLabel("Precio", required = true)

            ProductDefaultField(
                value = formState.price,
                onValueChange = {
                    viewModel.onFormChange(formState.copy(price = it))
                },
                placeholder = "0",
                keyboardType = KeyboardType.Decimal,
                prefix = {
                    Text(
                        text = "$ ",
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
            Spacer(Modifier.height(20.dp))

            FieldLabel("Descripción")

           ProductDefaultField(
                value = formState.description,
                onValueChange = {
                    viewModel.onFormChange(formState.copy(description = it))
                },
                placeholder = "Describe brevemente qué incluye...(Opcional)",
                singleLine = false,
                minLines = 4,
                maxLines = 6,
                keyboardType = KeyboardType.Text,
                modifier = Modifier.heightIn(min = 118.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        SectionTitle("Tipo")

        FormSection {
            Text(
                text = "Selecciona cómo quieres manejar este ítem dentro de tu negocio.",
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TypeChip(
                    modifier = Modifier.weight(1f),
                    text = "Producto",
                    description = "Venta física",
                    icon = Icons.Outlined.Inventory2,
                    selected = formState.type == "product",
                    onClick = {
                        viewModel.onFormChange(formState.copy(type = "product"))
                    }
                )

                TypeChip(
                    modifier = Modifier.weight(1f),
                    text = "Servicio",
                    description = "Atención o reserva",
                    icon = Icons.Outlined.RoomService,
                    selected = formState.type == "service",
                    onClick = {
                        viewModel.onFormChange(formState.copy(type = "service"))
                    }
                )
            }
        }


        Spacer(Modifier.height(30.dp))


    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = TextSecondary,
        letterSpacing = 0.2.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun FormSection(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.75f),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 18.dp, vertical = 18.dp),
        content = content
    )
}

@Composable
private fun FieldLabel(
    text: String,
    required: Boolean = false
) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        if (required) {
            Text(
                text = " *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TypeChip(
    modifier: Modifier = Modifier,
    text: String,
    description: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary

    val backgroundColor by animateColorAsState(
        targetValue = if (selected) primary.copy(alpha = 0.10f) else Color(0xFFF8FAFC),
        label = "chipBackgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) primary else BorderGray,
        label = "chipBorderColor"
    )

    val iconBackground by animateColorAsState(
        targetValue = if (selected) primary else Color.White,
        label = "chipIconBackground"
    )

    val iconTint by animateColorAsState(
        targetValue = if (selected) Color.White else TextSecondary,
        label = "chipIconTint"
    )

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.03f else 1f,
        animationSpec = spring(),
        label = "chipScale"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (selected) 1.4.dp else 1.dp,
        label = "chipBorderWidth"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                if (selected) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) primary else TextPrimary
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}