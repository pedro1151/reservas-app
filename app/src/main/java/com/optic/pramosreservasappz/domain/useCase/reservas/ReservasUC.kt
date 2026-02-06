package com.optic.pramosreservasappz.domain.useCase.reservas

import com.optic.pramosreservasappz.domain.useCase.external.LoginGoogleUseCase
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.CreateClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.DeleteClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.GetClientPorProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.clients.UpdateClientUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.CreateServiceUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.GetServicesPorIdUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.GetServicesPorProviderUC
import com.optic.pramosreservasappz.domain.useCase.reservas.services.UpdateServiceUC
import com.optic.pramosreservasappz.domain.useCase.reservas.staff.GetStaffTotalesUC

data class ReservasUC(

    //clientes
    val getClientPorProviderUC: GetClientPorProviderUC,
    val getClientPorIdUC: GetClientPorIdUC,
    val createClientUC: CreateClientUC,
    val updateClientUC: UpdateClientUC,
    val deleteClientUC: DeleteClientUC,


    //services
    val getServicesPorProviderUC: GetServicesPorProviderUC,
    val createServiceUC: CreateServiceUC,
    val updateServiceUC: UpdateServiceUC,
    val getStaffTotalesUC: GetStaffTotalesUC,
    val getServicesPorIdUC: GetServicesPorIdUC
)
