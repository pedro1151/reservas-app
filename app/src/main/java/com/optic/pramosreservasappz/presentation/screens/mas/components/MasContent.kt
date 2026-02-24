package com.optic.pramosreservasappz.presentation.screens.mas

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.authstate.AuthStateVM

@Composable
fun MasContent(
    paddingValues: PaddingValues,
    viewModel: MasViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val authStateVM: AuthStateVM = hiltViewModel()
    val userEmail by authStateVM.userEmail.collectAsState()

    // Estados para diálogos
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showSocialDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(8.dp))

        // 🎨 SECCIÓN: Apariencia
        SectionHeader(text = "Apariencia")

        MenuCard {
            MenuItem(
                icon = Icons.Outlined.Palette,
                title = "Tema",
                subtitle = "Claro, Oscuro o Sistema",
                onClick = { showThemeDialog = true }
            )

            Divider(color = Color(0xFFE0E0E0))

            MenuItem(
                icon = Icons.Outlined.Language,
                title = "Idioma",
                subtitle = "Español",
                onClick = { showLanguageDialog = true }
            )
        }

        Spacer(Modifier.height(24.dp))

        // 🌐 SECCIÓN: Social
        SectionHeader(text = "Síguenos")

        MenuCard {
            MenuItem(
                icon = Icons.Outlined.Groups,
                title = "Síguenos",
                subtitle = "Redes sociales",
                onClick = { showSocialDialog = true }
            )

            Divider(color = Color(0xFFE0E0E0))

            MenuItem(
                icon = Icons.Outlined.Share,
                title = "Compartir App Reservas",
                subtitle = "Invita a tus amigos",
                onClick = { shareApp(context) }
            )
        }

        Spacer(Modifier.height(24.dp))

        // ℹ️ SECCIÓN: Información
        SectionHeader(text = "Información")

        MenuCard {
            MenuItem(
                icon = Icons.Outlined.Shield,
                title = "Política de privacidad",
                subtitle = "Cómo protegemos tus datos",
                onClick = { showPrivacyDialog = true }
            )

            Divider(color = Color(0xFFE0E0E0))

            MenuItem(
                icon = Icons.Outlined.Info,
                title = "Versión de la app",
                subtitle = getAppVersion(),
                onClick = { showAboutDialog = true },
                showChevron = false
            )
        }

        Spacer(Modifier.height(24.dp))

        // 🚪 SECCIÓN: Cuenta
        SectionHeader(text = "Cuenta")

        MenuCard {
            MenuItem(
                icon = Icons.Default.Air,
                title = "Cerrar sesión",
                subtitle = userEmail,
                onClick = { viewModel.logout() },
                iconTint = MaterialTheme.colorScheme.primary,
                titleColor = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(32.dp))

        // Footer con créditos
        AppFooter()

        Spacer(Modifier.height(16.dp))
    }

    // 🎭 Diálogos
    if (showThemeDialog) {
        ThemeDialog(onDismiss = { showThemeDialog = false })
    }

    if (showLanguageDialog) {
        LanguageDialog(onDismiss = { showLanguageDialog = false })
    }

    if (showSocialDialog) {
        SocialDialog(
            onDismiss = { showSocialDialog = false },
            context = context
        )
    }

    if (showPrivacyDialog) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyDialog = false })
    }

    if (showAboutDialog) {
        AboutAppDialog(onDismiss = { showAboutDialog = false })
    }
}

// 📌 Componente: Header de sección
@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

// 🎴 Componente: Card contenedor de menú
@Composable
private fun MenuCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            content()
        }
    }
}

// 📋 Componente: Item de menú
@Composable
private fun MenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    titleColor: Color = Color(0xFF212121),
    showChevron: Boolean = true
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = iconTint.copy(alpha = 0.1f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            // Textos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = titleColor
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }

            // Chevron
            if (showChevron) {
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFBDBDBD),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// 👣 Componente: Footer de la app
@Composable
private fun AppFooter() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hecho con ❤️ en Bolivia",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF9E9E9E)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} Reservas App",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFBDBDBD)
        )
    }
}

// 🔧 Función: Obtener versión de la app
private fun getAppVersion(): String {
    // Puedes actualizar esto manualmente o conectarlo a tu sistema de versiones
    return "Versión 1.0.0 (1)"
}

// 📤 Función: Compartir app
private fun shareApp(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Reservas App")
        putExtra(
            Intent.EXTRA_TEXT,
            "¡Descarga Reservas App! La mejor aplicación para gestionar tus reservas. " +
                    "\n\nDescárgala aquí: [Link de tu app en Play Store]"
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Compartir vía"))
}

// 🎨 Diálogo: Tema
@Composable
private fun ThemeDialog(onDismiss: () -> Unit) {
    var selectedTheme by remember { mutableStateOf("Sistema") }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Palette,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text("Seleccionar tema")
        },
        text = {
            Column {
                ThemeOption("Claro", selectedTheme) { selectedTheme = "Claro" }
                ThemeOption("Oscuro", selectedTheme) { selectedTheme = "Oscuro" }
                ThemeOption("Sistema", selectedTheme) { selectedTheme = "Sistema" }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun ThemeOption(
    name: String,
    selected: String,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected == name,
            onClick = onSelect
        )
        Spacer(Modifier.width(8.dp))
        Text(name)
    }
}

// 🌐 Diálogo: Idioma
@Composable
private fun LanguageDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Language,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text("Seleccionar idioma")
        },
        text = {
            Column {
                Text("🇪🇸 Español")
                Spacer(Modifier.height(8.dp))
                Text(
                    "Próximamente: Inglés, Portugués",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

// 🌐 Diálogo: Redes sociales
@Composable
private fun SocialDialog(
    onDismiss: () -> Unit,
    context: Context
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Groups,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text("Síguenos en redes sociales")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SocialButton(
                    icon = "📘",
                    name = "Facebook",
                    onClick = {
                        openUrl(context, "https://facebook.com/reservasapp")
                    }
                )
                SocialButton(
                    icon = "📷",
                    name = "Instagram",
                    onClick = {
                        openUrl(context, "https://instagram.com/reservasapp")
                    }
                )
                SocialButton(
                    icon = "🐦",
                    name = "Twitter",
                    onClick = {
                        openUrl(context, "https://twitter.com/reservasapp")
                    }
                )
                SocialButton(
                    icon = "💼",
                    name = "LinkedIn",
                    onClick = {
                        openUrl(context, "https://linkedin.com/company/reservasapp")
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun SocialButton(
    icon: String,
    name: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF5F5F5),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// 🔒 Diálogo: Política de privacidad
@Composable
private fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text("Política de Privacidad")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                PrivacySection("1. Información que recopilamos",
                    "Recopilamos información personal como nombre, correo electrónico y teléfono " +
                            "únicamente para gestionar tus reservas y mejorar nuestro servicio.")

                PrivacySection("2. Uso de la información",
                    "Tu información se utiliza para procesar reservas, enviar notificaciones " +
                            "y mejorar la experiencia del usuario.")

                PrivacySection("3. Protección de datos",
                    "Implementamos medidas de seguridad para proteger tu información contra " +
                            "acceso no autorizado.")

                PrivacySection("4. Compartir información",
                    "No compartimos tu información personal con terceros sin tu consentimiento.")

                PrivacySection("5. Cookies",
                    "Utilizamos cookies para mejorar tu experiencia de navegación y recordar " +
                            "tus preferencias.")

                PrivacySection("6. Tus derechos",
                    "Tienes derecho a acceder, modificar o eliminar tu información personal " +
                            "en cualquier momento.")

                Spacer(Modifier.height(8.dp))

                Text(
                    "Última actualización: Febrero 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9E9E9E),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Entendido")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun PrivacySection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF424242)
        )
    }
}

// ℹ️ Diálogo: Acerca de la app
@Composable
private fun AboutAppDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text("Acerca de Reservas App")
        },
        text = {
            Column {
                Text(
                    text = "Versión ${getAppVersion()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Reservas App es tu solución completa para gestionar reservas de forma " +
                            "simple y eficiente.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "✨ Características:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                BulletPoint("Gestión de clientes y servicios")
                BulletPoint("Calendario de reservas")
                BulletPoint("Notificaciones en tiempo real")
                BulletPoint("Reportes y estadísticas")

                Spacer(Modifier.height(12.dp))

                Text(
                    "Desarrollado en Bolivia 🇧🇴",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text("• ", color = MaterialTheme.colorScheme.primary)
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF424242)
        )
    }
}

// 🔗 Función: Abrir URL
private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        // Manejar error
    }
}
