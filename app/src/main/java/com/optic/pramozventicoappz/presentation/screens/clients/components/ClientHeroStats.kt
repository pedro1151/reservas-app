package com.optic.pramozventicoappz.presentation.screens.clients.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Pink600  = Color(0xFFE91E63)
private val Pink50   = Color(0xFFFFF0F3)
private val Slate900 = Color(0xFF0F172A)
private val Slate400 = Color(0xFF94A3B8)
private val Slate200 = Color(0xFFE2E8F0)

// ─── Hero Stats ─────────────────────────────────────────────────────────────────
@Composable
fun ClientHeroStats(
    totalClients : Int,
    withPhone    : Int,
    withEmail    : Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .shadow(
                elevation    = 2.dp,
                shape        = RoundedCornerShape(18.dp),
                ambientColor = Pink600.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Slate200, RoundedCornerShape(18.dp))
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            // ── Total clients ──
            ClientStatItem(
                icon       = Icons.Outlined.Group,
                value      = "$totalClients",
                label      = "Clientes",
                iconBg     = Pink50,
                iconTint   = Pink600,
                valueColor = Slate900
            )

            Box(modifier = Modifier.width(1.dp).height(32.dp).background(Slate200))

            // ── With email ──
            ClientStatItem(
                icon       = Icons.Outlined.Mail,
                value      = "$withEmail",
                label      = "Con email",
                iconBg     = Color(0xFFF0FDF4),
                iconTint   = Color(0xFF059669),
                valueColor = Slate900
            )

            Box(modifier = Modifier.width(1.dp).height(32.dp).background(Slate200))

            // ── With phone ──
            ClientStatItem(
                icon       = Icons.Outlined.Phone,
                value      = "$withPhone",
                label      = "Con tel.",
                iconBg     = Color(0xFFFFF7ED),
                iconTint   = Color(0xFFEA580C),
                valueColor = Slate900
            )
        }
    }
}

@Composable
private fun ClientStatItem(
    icon       : androidx.compose.ui.graphics.vector.ImageVector,
    value      : String,
    label      : String,
    iconBg     : Color,
    iconTint   : Color,
    valueColor : Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier            = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(15.dp))
        }
        Text(
            text          = value,
            fontSize      = 18.sp,
            fontWeight    = FontWeight.Black,
            color         = valueColor,
            letterSpacing = (-0.5).sp,
            lineHeight    = 20.sp
        )
        Text(
            text          = label,
            fontSize      = 10.sp,
            color         = Slate400,
            fontWeight    = FontWeight.Medium,
            letterSpacing = 0.1.sp
        )
    }
}
