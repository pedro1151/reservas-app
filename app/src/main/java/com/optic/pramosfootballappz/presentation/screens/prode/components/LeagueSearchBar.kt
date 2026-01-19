package com.optic.pramosfootballappz.presentation.screens.prode.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.optic.pramosfootballappz.presentation.ui.theme.Grafito
import androidx.compose.runtime.setValue

@Composable
fun LeagueSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var hadFocus by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // 🔥 Pedir foco SOLO cuando se expande
    LaunchedEffect(expanded) {
        if (expanded) {
            focusRequester.requestFocus()
            hadFocus = false
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        // 🔍 ICONO
        IconButton(
            onClick = {
                expanded = !expanded
                if (!expanded) {
                    onQueryChange("")
                    focusManager.clearFocus()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = "Buscar ligas",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Grafito.copy(alpha = 0.16f),
                    unfocusedContainerColor = Grafito.copy(alpha = 0.10f),
                    disabledContainerColor = Grafito.copy(alpha = 0.10f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(start = 6.dp)
                    .widthIn(min = 150.dp, max = 320.dp)
                    .height(50.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { state ->
                        if (state.isFocused) {
                            hadFocus = true
                        } else if (hadFocus) {
                            // 🔻 Solo cerrar si YA tuvo foco antes
                            expanded = false
                            onQueryChange("")
                        }
                    }
            )
        }
    }
}
