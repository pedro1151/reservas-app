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
    val context     = LocalContext.current
    val authStateVM: AuthStateVM = hiltViewModel()
    val userEmail   by authStateVM.userEmail.collectAsState()

    var showAboutDialog  by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog  by remember { mutableStateOf(false) }   // ← NUEVO
    var showSocialDialog by remember { mutableStateOf(false) }
    var showThemeDialog  by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(8.dp))

        SectionHeader(text = "Apariencia")
        MenuCard {
            MenuItem(icon = Icons.Outlined.Palette, title = "Tema", subtitle = "Claro, Oscuro o Sistema", onClick = { showThemeDialog = true })
            Divider(color = Color(0xFFE0E0E0))
            MenuItem(icon = Icons.Outlined.Language, title = "Idioma", subtitle = "Español", onClick = { showLanguageDialog = true })
        }

        Spacer(Modifier.height(24.dp))

        SectionHeader(text = "Síguenos")
        MenuCard {
            MenuItem(icon = Icons.Outlined.Groups, title = "Síguenos", subtitle = "Redes sociales", onClick = { showSocialDialog = true })
            Divider(color = Color(0xFFE0E0E0))
            MenuItem(icon = Icons.Outlined.Share, title = "Compartir App Reservas", subtitle = "Invita a tus amigos", onClick = { shareApp(context) })
        }

        Spacer(Modifier.height(24.dp))

        // ── Información (con Términos y Condiciones añadido) ──────────────────
        SectionHeader(text = "Información")
        MenuCard {
            MenuItem(
                icon     = Icons.Outlined.Shield,
                title    = "Política de privacidad",
                subtitle = "Cómo protegemos tus datos",
                onClick  = { showPrivacyDialog = true }
            )
            Divider(color = Color(0xFFE0E0E0))
            // ── NUEVO ─────────────────────────────────────────────────────────
            MenuItem(
                icon     = Icons.Outlined.Gavel,
                title    = "Términos y condiciones",
                subtitle = "Reglas de uso del servicio",
                onClick  = { showTermsDialog = true }
            )
            Divider(color = Color(0xFFE0E0E0))
            // ─────────────────────────────────────────────────────────────────
            MenuItem(
                icon        = Icons.Outlined.Info,
                title       = "Versión de la app",
                subtitle    = getAppVersion(),
                onClick     = { showAboutDialog = true },
                showChevron = false
            )
        }

        Spacer(Modifier.height(24.dp))

        SectionHeader(text = "Cuenta")
        MenuCard {
            MenuItem(
                icon       = Icons.Default.Air,
                title      = "Cerrar sesión",
                subtitle   = userEmail,
                onClick    = { viewModel.logout() },
                iconTint   = MaterialTheme.colorScheme.primary,
                titleColor = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(32.dp))
        AppFooter()
        Spacer(Modifier.height(16.dp))
    }

    if (showThemeDialog)    ThemeDialog(onDismiss = { showThemeDialog = false })
    if (showLanguageDialog) LanguageDialog(onDismiss = { showLanguageDialog = false })
    if (showSocialDialog)   SocialDialog(onDismiss = { showSocialDialog = false }, context = context)
    if (showPrivacyDialog)  PrivacyPolicyDialog(onDismiss = { showPrivacyDialog = false })
    if (showAboutDialog)    AboutAppDialog(onDismiss = { showAboutDialog = false })
    // ── NUEVO ─────────────────────────────────────────────────────────────────
    if (showTermsDialog)    TermsAndConditionsDialog(onDismiss = { showTermsDialog = false })
}

// ── Términos y condiciones dialog ─────────────────────────────────────────────
@Composable
private fun TermsAndConditionsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Gavel,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = { Text("Términos y Condiciones") },
        text  = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TermsSection(
                    "1. Aceptación de los términos",
                    "Al usar Reservas App, aceptás estos términos en su totalidad. Si no estás de acuerdo con alguna parte, por favor dejá de usar la aplicación."
                )
                TermsSection(
                    "2. Uso del servicio",
                    "Reservas App está diseñado exclusivamente para la gestión de reservas y citas. Queda prohibido su uso para fines ilegales, fraudulentos o contrarios a estos términos."
                )
                TermsSection(
                    "3. Cuentas de usuario",
                    "Sos responsable de mantener la confidencialidad de tus credenciales de acceso. Notificanos de inmediato ante cualquier uso no autorizado de tu cuenta."
                )
                TermsSection(
                    "4. Propiedad intelectual",
                    "Todo el contenido, diseño y código de Reservas App es propiedad exclusiva de sus desarrolladores y está protegido por las leyes de propiedad intelectual aplicables."
                )
                TermsSection(
                    "5. Limitación de responsabilidad",
                    "Reservas App se provee \"tal como está\". No garantizamos disponibilidad ininterrumpida ni ausencia de errores. No somos responsables por pérdidas derivadas del uso del servicio."
                )
                TermsSection(
                    "6. Datos y privacidad",
                    "El tratamiento de tus datos personales está regulado por nuestra Política de Privacidad, la cual forma parte integral de estos términos."
                )
                TermsSection(
                    "7. Modificaciones",
                    "Nos reservamos el derecho de modificar estos términos en cualquier momento. Los cambios entrarán en vigencia al publicarse dentro de la aplicación."
                )
                TermsSection(
                    "8. Cancelación",
                    "Podés cancelar tu cuenta en cualquier momento. Nos reservamos el derecho de suspender cuentas que violen estos términos sin previo aviso."
                )
                TermsSection(
                    "9. Legislación aplicable",
                    "Estos términos se rigen por las leyes de Bolivia. Cualquier disputa será resuelta ante los tribunales competentes de la ciudad de La Paz."
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Última actualización: Marzo 2026",
                    style      = MaterialTheme.typography.bodySmall,
                    color      = Color(0xFF9E9E9E),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Entendido") }
        },
        containerColor = Color.White,
        shape          = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun TermsSection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(
            text       = title,
            style      = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text  = content,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF424242)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Todo lo de abajo es idéntico al original — sin cambios
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(text: String) {
    Text(
        text       = text,
        style      = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color      = MaterialTheme.colorScheme.primary,
        modifier   = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
private fun MenuCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) { content() }
    }
}

@Composable
private fun MenuItem(
    icon:        ImageVector,
    title:       String,
    subtitle:    String,
    onClick:     () -> Unit,
    modifier:    Modifier = Modifier,
    iconTint:    Color = MaterialTheme.colorScheme.primary,
    titleColor:  Color = Color(0xFF212121),
    showChevron: Boolean = true
) {
    Surface(onClick = onClick, color = Color.Transparent, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = iconTint.copy(alpha = 0.1f), modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = titleColor)
                Spacer(Modifier.height(2.dp))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF757575))
            }
            if (showChevron) {
                Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null, tint = Color(0xFFBDBDBD), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun AppFooter() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Hecho con ❤️ en Bolivia", style = MaterialTheme.typography.bodySmall, color = Color(0xFF9E9E9E))
        Spacer(Modifier.height(4.dp))
        Text("© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} Reservas App", style = MaterialTheme.typography.bodySmall, color = Color(0xFFBDBDBD))
    }
}

private fun getAppVersion(): String = "Versión 1.0.0 (1)"

private fun shareApp(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Reservas App")
        putExtra(Intent.EXTRA_TEXT, "¡Descarga Reservas App! La mejor aplicación para gestionar tus reservas.\n\nDescárgala aquí: [Link de tu app en Play Store]")
    }
    context.startActivity(Intent.createChooser(shareIntent, "Compartir vía"))
}

@Composable
private fun ThemeDialog(onDismiss: () -> Unit) {
    var selectedTheme by remember { mutableStateOf("Sistema") }
    AlertDialog(
        onDismissRequest = onDismiss,
        icon  = { Icon(Icons.Outlined.Palette, null, tint = MaterialTheme.colorScheme.primary) },
        title = { Text("Seleccionar tema") },
        text  = {
            Column {
                ThemeOption("Claro",   selectedTheme) { selectedTheme = "Claro" }
                ThemeOption("Oscuro",  selectedTheme) { selectedTheme = "Oscuro" }
                ThemeOption("Sistema", selectedTheme) { selectedTheme = "Sistema" }
            }
        },
        confirmButton  = { TextButton(onClick = onDismiss) { Text("Aceptar") } },
        containerColor = Color.White, shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun ThemeOption(name: String, selected: String, onSelect: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected == name, onClick = onSelect)
        Spacer(Modifier.width(8.dp))
        Text(name)
    }
}

@Composable
private fun LanguageDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon  = { Icon(Icons.Outlined.Language, null, tint = MaterialTheme.colorScheme.primary) },
        title = { Text("Seleccionar idioma") },
        text  = {
            Column {
                Text("🇪🇸 Español")
                Spacer(Modifier.height(8.dp))
                Text("Próximamente: Inglés, Portugués", style = MaterialTheme.typography.bodySmall, color = Color(0xFF757575))
            }
        },
        confirmButton  = { TextButton(onClick = onDismiss) { Text("Aceptar") } },
        containerColor = Color.White, shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun SocialDialog(onDismiss: () -> Unit, context: Context) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon  = { Icon(Icons.Outlined.Groups, null, tint = MaterialTheme.colorScheme.primary) },
        title = { Text("Síguenos en redes sociales") },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SocialButton("📘", "Facebook")   { openUrl(context, "https://facebook.com/reservasapp") }
                SocialButton("📷", "Instagram")  { openUrl(context, "https://instagram.com/reservasapp") }
                SocialButton("🐦", "Twitter")    { openUrl(context, "https://twitter.com/reservasapp") }
                SocialButton("💼", "LinkedIn")   { openUrl(context, "https://linkedin.com/company/reservasapp") }
            }
        },
        confirmButton  = { TextButton(onClick = onDismiss) { Text("Cerrar") } },
        containerColor = Color.White, shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun SocialButton(icon: String, name: String, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = RoundedCornerShape(12.dp), color = Color(0xFFF5F5F5), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(12.dp))
            Text(name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon  = { Icon(Icons.Outlined.Shield, null, tint = MaterialTheme.colorScheme.primary) },
        title = { Text("Política de Privacidad") },
        text  = {
            Column(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp).verticalScroll(rememberScrollState())) {
                PrivacySection("1. Información que recopilamos", "Recopilamos información personal como nombre, correo electrónico y teléfono únicamente para gestionar tus reservas y mejorar nuestro servicio.")
                PrivacySection("2. Uso de la información", "Tu información se utiliza para procesar reservas, enviar notificaciones y mejorar la experiencia del usuario.")
                PrivacySection("3. Protección de datos", "Implementamos medidas de seguridad para proteger tu información contra acceso no autorizado.")
                PrivacySection("4. Compartir información", "No compartimos tu información personal con terceros sin tu consentimiento.")
                PrivacySection("5. Cookies", "Utilizamos cookies para mejorar tu experiencia de navegación y recordar tus preferencias.")
                PrivacySection("6. Tus derechos", "Tienes derecho a acceder, modificar o eliminar tu información personal en cualquier momento.")
                Spacer(Modifier.height(8.dp))
                Text("Última actualización: Febrero 2026", style = MaterialTheme.typography.bodySmall, color = Color(0xFF9E9E9E), fontWeight = FontWeight.Medium)
            }
        },
        confirmButton  = { TextButton(onClick = onDismiss) { Text("Entendido") } },
        containerColor = Color.White, shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun PrivacySection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text(content, style = MaterialTheme.typography.bodySmall, color = Color(0xFF424242))
    }
}

@Composable
private fun AboutAppDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon  = { Icon(Icons.Outlined.Info, null, tint = MaterialTheme.colorScheme.primary) },
        title = { Text("Acerca de Reservas App") },
        text  = {
            Column {
                Text("Versión ${getAppVersion()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text("Reservas App es tu solución completa para gestionar reservas de forma simple y eficiente.", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                Text("✨ Características:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                BulletPoint("Gestión de clientes y servicios")
                BulletPoint("Calendario de reservas")
                BulletPoint("Notificaciones en tiempo real")
                BulletPoint("Reportes y estadísticas")
                Spacer(Modifier.height(12.dp))
                Text("Desarrollado en Bolivia 🇧🇴", style = MaterialTheme.typography.bodySmall, color = Color(0xFF757575), fontWeight = FontWeight.Medium)
            }
        },
        confirmButton  = { TextButton(onClick = onDismiss) { Text("Cerrar") } },
        containerColor = Color.White, shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun BulletPoint(text: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("• ", color = MaterialTheme.colorScheme.primary)
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color(0xFF424242))
    }
}

private fun openUrl(context: Context, url: String) {
    try { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
    catch (e: Exception) { /* ignorar */ }
}
