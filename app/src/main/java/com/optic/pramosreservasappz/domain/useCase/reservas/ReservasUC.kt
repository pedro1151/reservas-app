package com.optic.pramosreservasappz.domain.useCase.reservas

import com.optic.pramosreservasappz.domain.useCase.external.LoginGoogleUseCase
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.GetServicesPorProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.staff.GetStaffTotalesUC

data class ReservasUC(
    val getClientPorProviderUC: GetClientPorProviderUC,
    val getServicesPorProviderUC: GetServicesPorProviderUC,
    val getStaffTotalesUC: GetStaffTotalesUC
)
