package com.optic.pramosfootballappz.domain.repository

import com.optic.pramosfootballappz.domain.model.AuthResponse
import com.optic.pramosfootballappz.domain.util.Resource

interface ExternalRepository {

    suspend fun login(id_token: String):  Resource<AuthResponse>
}