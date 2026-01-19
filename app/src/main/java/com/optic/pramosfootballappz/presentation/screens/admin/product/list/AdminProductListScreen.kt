package com.optic.pramosfootballappz.presentation.screens.admin.product.list

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.optic.pramosfootballappz.presentation.screens.admin.product.list.components.AdminProductListContent

@Composable
fun AdminProductListScreen() {

    Scaffold() {paddingValues ->
        AdminProductListContent(paddingValues = paddingValues)
    }

}