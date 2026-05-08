package com.optic.pramozventicoappz.domain.useCase.reservas

import com.optic.pramozventicoappz.domain.useCase.reservas.clients.CreateClientUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.DeleteClientUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.GetClientPorBusinessUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.GetClientPorIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.clients.UpdateClientUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.CreateProductSafeUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.CreateProductUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.DeleteProductHardUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.DeleteProductSoftUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.GetProductByIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.GetProductsByBusinessUC
import com.optic.pramozventicoappz.domain.useCase.reservas.product.UpdateProductUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.CreateReservationUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.GetReservationByIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.GetReservationsByProviderUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.GetReservationsUC
import com.optic.pramozventicoappz.domain.useCase.reservas.reservation.UpdateReservationUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.CreateSaleItemBulkUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.CreateSaleItemUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.DeleteSaleItemHardUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.DeleteSaleItemSoftUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.GetItemsBySaleUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.GetSaleItemByIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.saleitem.UpdateSaleItemUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.CreateSaleUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.CreateSaleWithItemsUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.DeleteSaleHardUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.DeleteSaleSoftUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.GetSaleByIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.GetSalesByBusinessUC
import com.optic.pramozventicoappz.domain.useCase.reservas.sales.UpdateSaleUC
import com.optic.pramozventicoappz.domain.useCase.reservas.salestats.GetSaleStatsUC
import com.optic.pramozventicoappz.domain.useCase.reservas.services.CreateServiceUC
import com.optic.pramozventicoappz.domain.useCase.reservas.services.GetServicesPorIdUC
import com.optic.pramozventicoappz.domain.useCase.reservas.services.GetServicesPorProviderUC
import com.optic.pramozventicoappz.domain.useCase.reservas.services.UpdateServiceUC
import com.optic.pramozventicoappz.domain.useCase.reservas.staff.GetStaffTotalesUC

data class ReservasUC(

    //clientes
    val getClientPorBusinessUC: GetClientPorBusinessUC,
    val getClientPorIdUC: GetClientPorIdUC,
    val createClientUC: CreateClientUC,
    val updateClientUC: UpdateClientUC,
    val deleteClientUC: DeleteClientUC,


    //services
    val getServicesPorProviderUC: GetServicesPorProviderUC,
    val createServiceUC: CreateServiceUC,
    val updateServiceUC: UpdateServiceUC,
    val getStaffTotalesUC: GetStaffTotalesUC,
    val getServicesPorIdUC: GetServicesPorIdUC,

    //reservas

    val getReservationsUC: GetReservationsUC,
    val createReservationUC: CreateReservationUC,
    val updateReservationUC: UpdateReservationUC,
    val getReservationByIdUC: GetReservationByIdUC,
    val getReservationsByProviderUC: GetReservationsByProviderUC,

    // sales
    val createSaleUC: CreateSaleUC,
    val createSaleWithItemsUC: CreateSaleWithItemsUC,
    val getSalesByBusinessUC: GetSalesByBusinessUC,
    val getSaleByIdUC: GetSaleByIdUC,
    val updateSaleUC: UpdateSaleUC,
    val deleteSaleHardUC: DeleteSaleHardUC,
    val deleteSaleSoftUC: DeleteSaleSoftUC,

    //ITEMS
    val createSaleItemUC: CreateSaleItemUC,
    val createSaleItemBulkUC: CreateSaleItemBulkUC,
    val getItemsBySaleUC: GetItemsBySaleUC,
    val getSaleItemByIdUC: GetSaleItemByIdUC,
    val updateSaleItemUC: UpdateSaleItemUC,
    val deleteSaleItemHardUC: DeleteSaleItemHardUC,
    val deleteSaleItemSoftUC: DeleteSaleItemSoftUC,

    //products

    val createProductUC: CreateProductUC,
    val createProductSafeUC: CreateProductSafeUC,
    val updateProductUC: UpdateProductUC,
    val getProductByIdUC: GetProductByIdUC,
    val getProductsByBusinessUC: GetProductsByBusinessUC,
    val deleteProductHardUC: DeleteProductHardUC,
    val deleteProductSoftUC: DeleteProductSoftUC,


    // sale stats

    val getSaleStatsUC: GetSaleStatsUC




)
