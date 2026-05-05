package com.optic.pramosreservasappz.domain.useCase.external

import com.optic.pramosreservasappz.domain.useCase.external.googleplaybilling.GetMyEntitlementUC
import com.optic.pramosreservasappz.domain.useCase.external.googleplaybilling.GetUserEntitlementsUC
import com.optic.pramosreservasappz.domain.useCase.external.googleplaybilling.GetUserPurchasesUC
import com.optic.pramosreservasappz.domain.useCase.external.googleplaybilling.GooglePlayVerifyPurchasesUC

data class ExternalUseCase(
    val login: LoginGoogleUseCase,

    // google play billings

    val googlePlayVerifyPurchasesUC: GooglePlayVerifyPurchasesUC,
    val getUserPurchasesUC: GetUserPurchasesUC,
    val getMyEntitlementUC: GetMyEntitlementUC,
    val getUserEntitlementsUC: GetUserEntitlementsUC
)
