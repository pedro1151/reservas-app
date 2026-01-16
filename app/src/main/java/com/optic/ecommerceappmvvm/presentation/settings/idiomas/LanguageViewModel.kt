package com.optic.ecommerceappmvvm.presentation.settings.idiomas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.ecommerceappmvvm.data.dataSource.local.datastore.AuthDatastore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val repository: AuthDatastore
) : ViewModel() {

    val language = repository.languageFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        "es"
    )

    fun changeLanguage(language: AppLanguage) {
        viewModelScope.launch {
            repository.setLanguage(language = language.code)
        }
    }
}
