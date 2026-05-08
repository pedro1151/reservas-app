package com.optic.pramozventicoappz.domain.useCase.reservas.product


import com.optic.pramozventicoappz.domain.model.product.ProductCreateRequest
import com.optic.pramozventicoappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleCreateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class CreateProductUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        request: ProductCreateRequest
    ) = repository.createProduct(request)
}