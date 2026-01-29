package com.optic.pramosreservasappz.domain.useCase.reservas

import com.optic.pramosreservasappz.domain.useCase.external.LoginGoogleUseCase
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.CreateServiceUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.GetServicesPorIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.GetServicesPorProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.UpdateServiceUC
import com.optic.pramosreservasappz.domain.useCase.reservas.staff.GetStaffTotalesUC

data class ReservasUC(
    val getClientPorProviderUC: GetClientPorProviderUC,

    //services
    val getServicesPorProviderUC: GetServicesPorProviderUC,
    val createServiceUC: CreateServiceUC,
    val updateServiceUC: UpdateServiceUC,
    val getStaffTotalesUC: GetStaffTotalesUC,
    val getServicesPorIdUC: GetServicesPorIdUC
)
