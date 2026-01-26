package com.optic.pramosreservasappz.domain.repository

import com.optic.pramosreservasappz.domain.model.AuthResponse
import com.optic.pramosreservasappz.domain.util.Resource

interface ExternalRepository {

    suspend fun login(id_token: String):  Resource<AuthResponse>
}