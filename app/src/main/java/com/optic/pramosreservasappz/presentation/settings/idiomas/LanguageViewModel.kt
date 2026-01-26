package com.optic.pramosreservasappz.presentation.settings.idiomas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.data.dataSource.local.datastore.AuthDatastore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val repository: AuthDatastore
) : ViewModel() {

    val language = repository.languageFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        "es"
    )

    fun changeLanguage(language: AppLanguage) {
        viewModelScope.launch {
            repository.setLanguage(language.code)
        }
    }

}

