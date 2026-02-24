package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectServiceDialog(
    onDismiss: () -> Unit,
    onServiceSelected: (ServiceResponse) -> Unit,
    serviceViewModel: ServiceViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val services by serviceViewModel.localServicesList.collectAsState()

    val filteredServices = remember(searchQuery, services) {
        if (searchQuery.isBlank()) services
        else services.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.description?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null,
        modifier = Modifier.fillMaxHeight(0.96f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Seleccionar un servicio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W500,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 17.sp
                )
            }

            HorizontalDivider(color = Color(0xFFF0F0F0))

            // Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                placeholder = {
                    Text(
                        "Buscar",
                        fontSize = 15.sp,
                        color = Color(0xFFAAAAAA)
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null,
                        tint = Color(0xFFAAAAAA),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint = Color(0xFFAAAAAA),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Lista de servicios
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp)
            ) {
                if (filteredServices.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Search,
                                    null,
                                    tint = Color(0xFFDDDDDD),
                                    modifier = Modifier.size(36.dp)
                                )
                                Text(
                                    "Sin resultados",
                                    fontSize = 13.sp,
                                    color = Color(0xFFBBBBBB)
                                )
                            }
                        }
                    }
                } else {
                    items(filteredServices) { service ->
                        ServiceItem(
                            service = service,
                            onClick = { onServiceSelected(service) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceItem(
    service: ServiceResponse,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = service.name.take(1).uppercase(),
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFF424242)
            )
        }

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = service.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.W500,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${service.durationMinutes} mins",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    text = "·",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    text = if (service.price == null || service.price == 0.0) "Gratis" else "Bs ${service.price}",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
            }
        }

        // Arrow
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFFCCCCCC)
        )
    }
}
