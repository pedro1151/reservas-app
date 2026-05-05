package com.optic.pramosreservasappz.data.dataSource.remote.service


import com.optic.pramosreservasappz.domain.model.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.product.MiniProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationResponse
import com.optic.pramosreservasappz.domain.model.reservations.ReservationUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservations.completeresponse.ReservationResponseComplete
import com.optic.pramosreservasappz.domain.model.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.model.staff.StaffResponse
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemResponse
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemUpdateRequest
import com.optic.pramosreservasappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleUpdateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramosreservasappz.domain.model.sales.SalesStatsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservasService {
/* Se utiliza Response de retrofit, en los archivos de servicios */

    // venta simple
    @POST("/reservas/sales")
    suspend fun createSale(
        @Body request: SaleCreateRequest
    ): Response<SaleResponse>

    // venta con items
    @POST("/reservas/sales/with-items")
    suspend fun createSaleWithItems(
        @Body request: CreateSaleWithItemsRequest
    ): Response<SaleResponse>

     // crear vebta evitando duplicados
    @POST("/reservas/sales/safe")
    suspend fun createSaleSafe(
        @Body request: SaleCreateRequest
    ): Response<SaleResponse>

    // get ventas por business
    @GET("/reservas/sales")
    suspend fun getSalesByBusiness(
        @Query("business_id") businessId: Int,
        @Query("limit") limit: Int
    ): Response<List<SaleResponse>>

     // get sale por id
    @GET("/reservas/sales/{sale_id}")
    suspend fun getSaleById(
        @Path("sale_id") saleId: Int,
    ): Response<SaleWithItemsResponse>

    // actualizar venta
    @PUT("/reservas/sales/{sale_id}")
    suspend fun updateSale(
        @Path("sale_id") saleId: Int,
        @Body request: SaleUpdateRequest
    ): Response<SaleResponse>

    // ELIMINAR venta borrado logico
    @DELETE("/reservas/sales/{sale_id}/soft")
    suspend fun deleteSaleSoft(
        @Path("sale_id") saleId: Int
    ): Response<DefaultResponse>

    // ELIMINAR venta borrado definitivo
    @DELETE("/reservas/sales/{sale_id}/hard")
    suspend fun deleteSaleHard(
        @Path("sale_id") saleId: Int
    ): Response<DefaultResponse>

    // SALE STATS    --------------------------->
    @GET("/reservas/sales/stats")
    suspend fun getSalesStats(
        @Query("business_id") businessId: Int,
        @Query("year") year: Int
    ): Response<SalesStatsResponse>



    //sales items---------------------->

    @POST("/reservas/sale-items")
    suspend fun createSaleItem(
        @Body request: SaleItemCreateRequest
    ): Response<SaleItemResponse>


    @POST("/reservas/sale-items/bulk")
    suspend fun createSaleItemBulk(
        @Body request: List<SaleItemCreateRequest>
    ): Response<List<SaleItemResponse>>


    @GET("/reservas/sale-items/sale/{sale_id}")
    suspend fun getItemsBySale(
        @Path("sale_id") saleId: Int,
    ): Response<List<SaleItemResponse>>


    @GET("/reservas/sale-items/{item_id}")
    suspend fun getSaleItemById(
        @Path("item_id") itemId: Int,
    ): Response<SaleItemResponse>

    @PUT("/reservas/sale-items/{item_id}")
    suspend fun updateSaleItem(
        @Path("item_id") itemId: Int,
        @Body request: SaleItemUpdateRequest
    ): Response<SaleItemResponse>


    @DELETE("/reservas/sale-items/{item_id}/hard")
    suspend fun deleteSaleItemHard(
        @Path("item_id") itemId: Int
    ): Response<DefaultResponse>


    @DELETE("/reservas/sale-items/{item_id}/soft")
    suspend fun deleteSaleItemSoft(
        @Path("item_id") itemId: Int
    ): Response<DefaultResponse>


    // PRODUCTS


    @POST("/reservas/products")
    suspend fun createProduct(
        @Body request: ProductCreateRequest
    ): Response<MiniProductResponse>

    @POST("/reservas/products/safe")
    suspend fun createProductSafe(
        @Body request: ProductCreateRequest
    ): Response<ProductResponse>

    @GET("/reservas/products")
    suspend fun getProductsByBusiness(
        @Query("business_id") businessId: Int,
        @Query("name") name: String
    ): Response<List<MiniProductResponse>>

    @GET("/reservas/products/{product_id}")
    suspend fun getProductById(
        @Path("product_id") productId: Int
    ): Response<ProductResponse>

    @PUT("/reservas/products/{product_id}")
    suspend fun updateProduct(
        @Path("product_id") productId: Int,
        @Body request: ProductUpdateRequest
    ): Response<ProductResponse>

    @DELETE("/reservas/products/{product_id}/hard")
    suspend fun deleteProductHard(
        @Path("product_id") productId: Int
    ): Response<DefaultResponse>


    @DELETE("/reservas/products/{product_id}/soft")
    suspend fun deleteProductSoft(
        @Path("product_id") productId: Int
    ): Response<DefaultResponse>





    // reservas

    @POST("/reservas/reservation/create")
    suspend fun createReservation(
        @Body request: ReservationCreateRequest
    ): Response<ReservationResponse>


    @PUT("/reservas/reservation/update/{reservation_id}")
    suspend fun updateReservation(
        @Path("reservation_id") reservationId: Int,
        @Body request: ReservationUpdateRequest
    ): Response<ReservationResponse>

    @GET("/reservas/reservation/id/{reservation_id}")
    suspend fun getReservationById(
        @Path("reservation_id") reservationId: Int,
    ): Response<ReservationResponse>

    @GET("/reservation/list")
    suspend fun getReservations(
    ): Response<List<ReservationResponse>>



    @GET("/reservas/reservation/list/byprovider/{provider_id}")
    suspend fun getReservationsByProvider(
        @Path("provider_id") providerId: Int,
    ): Response<List<ReservationResponseComplete>>




    //clientes

    @GET("/reservas/clients/bybusiness")
    suspend fun getClientsByBusiness(
    @Query("business_id") businessId: Int,
    @Query("full_name") fullName: String,
    @Query("email") email: String,
    ): Response<List<ClientResponse>>

    @POST("/reservas/client/create")
    suspend fun createClient(
        @Body request: ClientCreateRequest
    ): Response<ClientResponse>



    @PUT("/reservas/client/update/{client_id}")
    suspend fun updateClient(
        @Path("client_id") clientId: Int,
        @Body request: ClientUpdateRequest
    ): Response<ClientResponse>

    @GET("/reservas/client/id/{client_id}")
    suspend fun getClientById(
        @Path("client_id") clientId: Int
    ): Response<ClientResponse>


    @DELETE("/reservas/client/delete/{client_id}")
    suspend fun deleteClient(
        @Path("client_id") clientId: Int
    ): Response<DefaultResponse>





    //servicios

    @POST("/reservas/service/create")
    suspend fun createService(
        @Body request: ServiceCreateRequest
    ): Response<ServiceResponse>

    @GET("/reservas/service/id/{service_id}")
    suspend fun getServiceById(
        @Path("service_id") serviceId: Int
    ): Response<ServiceResponse>


    @PUT("/reservas/service/update/{service_id}")
    suspend fun updateService(
        @Path("service_id") serviceId: Int,
        @Body request: ServiceUpdateRequest
    ): Response<ServiceResponse>

    @GET("/reservas/service/list/byprovider")
    suspend fun getServicesByProvider(
        @Query("provider_id") providerId: Int,
        @Query("name") name: String
    ): Response<List<ServiceResponse>>

    /* staff */
    @GET("/reservas/staff/list")
    suspend fun getStaffsTotales(
    ): Response<List<StaffResponse>>

    /*
    @GET("football/teams/{team_id}")
    suspend fun getTeamById(
        @Path("team_id") teamId: Int
    ): Response<TeamResponse>

    // equipos sugeridos

    @GET("football/teams/suggested/{limit}")
    suspend fun getSuggestedTeams(
        @Path("limit") limit: Int
    ): Response<List<Team>>

    // players

    @GET("football/get/player/{player_id}")
    suspend fun getPlayerPorId(
        @Path("player_id") playerId: Int,
    ): Response<PlayerComplete>

    @GET("football/players/name/{name}/page/{page}/size/{size}")
    suspend fun getPlayers(
        @Path("name") name: String?,
        @Path("page") page: Int,
        @Path("size") size: Int
    ): Response<List<Player>>

    @GET("football/players/all")
    suspend fun getallPlayers(
    ): Response<List<Player>>

    /*  Recupera la informacion de un Player junto con todas sus estatisticas
    * ligas, equipos, goles etc..   */
    @GET("football/players/stats/{player_id}")
    suspend fun getPlayerStats(
        @Path("player_id") playerId: Int
    ): Response<PlayerWithStats>

     // Trayectoria de un jugador last team
    @GET("football/players/team/{player_id}")
    suspend fun getPlayerLastTeam(
        @Path("player_id") playerId: Int
    ): Response<PlayerLastTeamResponse>

    // Trayectoria de un jugador,
    @GET("football/players/teams/{player_id}")
    suspend fun getPlayerTeams(
        @Path("player_id") playerId: Int
    ): Response<PlayerTeamsResponse>


   // get ligas
   @GET("football/leagues")
   suspend fun getLeagues(
       @Query("name") name: String,
       @Query("type_") type: String,
       @Query("country_name") countryName: String
   ): Response<List<League>>

    // get ligas
    @GET("football/leagues/participate/user/{user_id}")
    suspend fun getProdeParticipateLeagues(
        @Path("user_id") userId: Int
    ): Response<List<League>>


    @GET("football/leagues/top")
    suspend fun getTopLeagues(
    ): Response<List<League>>

    @GET("football/leagues/{league_id}")
    suspend fun getLeagueById(
        @Path("league_id") leagueId: Int
    ): Response<LeagueCompleteResponse>



// SEGUIDORES
    @POST("football/createFollowedPlayer")
    suspend fun createFollowedPlayer(
    @Body request: FollowedPlayerRequest
    ): Response<FollowedPlayerResponse>

    @GET("football/getFollowedPlayers")
    suspend fun getFollowedPlayers(
    ): Response<List<Player>>

    @DELETE("football/deleteFollowed/{player_id}")
    suspend fun deleteFollowedPlayer(
        @Path("player_id") playerId: Int
    ): Response<DefaultResponse>


    // TEAMS SEGUIDOS

    @POST("football/createFollowedTeam")
    suspend fun createFollowedTeam(
        @Body request: FollowedTeamRequest
    ): Response<FollowedTeamResponse>

    @GET("football/getFollowedTeams")
    suspend fun getFollowedTeams(
    ): Response<List<Team>>

    @DELETE("football/deleteFollowedTeam/{team_id}")
    suspend fun deleteFollowedTeam(
        @Path("team_id") teamId: Int
    ): Response<DefaultResponse>

    // VERSUS FIXTURE

    @GET("football/versusTeamFixture")
    suspend fun getFixtureVersus(
        @Query("team_one_id") teamOneId: Int,
        @Query("team_two_id") teamTwoId: Int,
        @Query("league_id") leagueId: Int,
        @Query("season") season: Int
    ): Response<List<FixtureResponse>>

    // FIXTURE POR PAIS ( MEDIANTE LA IP DE DONDE SE CONECTAN)
    @GET("football/fixtures/country")
    suspend fun getContryFixtures(
        @Query("season") season: Int,
        @Query("date") date: String
    ): Response<List<FixtureResponse>>

    // FIXTURE , RECUPERAR POR ID
    @GET("football/fixtures/{id}")
    suspend fun getFixtureById(
        @Path("id") id: Int
    ): Response<FixtureResponse>

    // FIXTURE LINEUPS POR ID
    @GET("football/fixtures/{id}/lineups")
    suspend fun getFixtureLineups(
        @Path("id") id: Int
    ): Response<FixtureLineupsResponse>

    // FIXTURE stats POR ID
    @GET("football/fixtures/{id}/stats")
    suspend fun getFixtureStats(
        @Path("id") id: Int
    ): Response<FixtureStatsResponse>

    // MATCHES ( FIXTURES ) POR TEAMS SEGUUIDOS

    @GET("football/getFixtureFollowedTeams")
    suspend fun getFixtureFollowedTeams(
        @Query("season") season: Int,
        @Query("date") date: String
    ): Response<List<FixtureResponse>>


    // MATCHES ( FIXTURES ) de equipos no seguidos


    @GET("football/fixtures/nofollow")
    suspend fun getNoFollowFixtures(
        @Query("season") season: Int,
        @Query("date") date: String
    ): Response<List<FixtureResponse>>
    //Fixture de un equipo en general
    @GET("football/fixtures/team/{team_id}")
    suspend fun getFixtureTeam(
        @Path("team_id") teamId: Int
    ): Response<List<FixtureResponse>>

    //Fixture de proximo partido de un EQuipo
    @GET("football/fixtures/team/{team_id}/next")
    suspend fun getNextFixtureTeam(
        @Path("team_id") teamId: Int
    ): Response<FixtureResponse>

    //Fixture top 5 ultimos partidos finalizados
    @GET("football/fixtures/team/{team_id}/top5")
    suspend fun getTopFiveFixtureTeam(
        @Path("team_id") teamId: Int
    ): Response<List<FixtureResponse>>

    //Fixture de una Liga
    @GET("football/fixtures/league/{league_id}/season/{season}/team/{team_id}")
    suspend fun getLeagueFixture(
        @Path("league_id") leagueId: Int,
        @Path("season") season: Int,
        @Path("team_id") teamId: Int
    ): Response<List<FixtureResponse>>

    // fixture por fecha
    @GET("football/fixtures/date/{date}/limit/{limit}")
    suspend fun getFixturesByDate(
        @Path("date") date: String,
        @Path("limit") limit: Int
    ): Response<List<FixtureResponse>>


    // fixture por rango de fechas
    @GET("football/fixtures/start/{date_start}/end/{date_end}")
    suspend fun getFixturesByRange(
        @Path("date_start") dateStart: String,
        @Path("date_end") dateEnd: String,
    ): Response<List<FixtureResponse>>

    // fixrure por round de liga
    @GET("football/fixtures/round")
    suspend fun getFixturesByRound(
        @Query("league_id") leagueId: Int,
        @Query("season") season: Int,
        @Query("round") round: String
    ): Response<List<FixtureResponse>>




    // PARA SEGUIR LIGAS

    @POST("football/createFollowedLeague")
    suspend fun createFollowedLeague(
        @Body request: FollowedLeagueRequest
    ): Response<FollowedLeagueResponse>

    @GET("football/getFollowedLeagues")
    suspend fun getFollowedLeagues(
    ): Response<List<League>>

    @DELETE("football/deleteFollowedLeague/{league_id}")
    suspend fun deleteFollowedLeague(
        @Path("league_id") leagueId: Int
    ): Response<DefaultResponse>


    // STANDINGS ( CLASIFICACIONES) POR LEAGUE Y SEASON
    @GET("football/leagues/{leagueId}/standings/{season}")
    suspend fun getLeagueStandings(
        @Path("leagueId") leagueId: Int,
        @Path("season") season: Int
    ): Response<List<StandingResponse>>


    //teams stats

    @GET("football/team/stats")
    suspend fun getTeamStats(
        @Query("season") season: Int,
        @Query("team_id") teamId: Int,
        @Query("date") date: String? =null
    ): Response<TeamStatsResponse>

    // prodes
    @POST("football/prode/create")
    suspend fun createFixturePrediction(
        @Body request: FixturePredictionRequest
    ): Response<FixturePredictionResponse>


    @GET("football/prode/fixturepredict/list")
    suspend fun getUserFixturePredictions(
        @Query("league_id") leagueId: Int,
        @Query("season") season: Int
    ): Response<List<FixturePredictionResponse>>


    // ranking de predicciones
    @GET("football/prode/ranking/{top}")
    suspend fun getPredictionRanking(
        @Path("top") top: Int
    ): Response<List<UserPredictionRanking>>

    // user predictions summary
    @GET("football/prode/summary/user/{user_id}/season/{season}")
    suspend fun getUserPredictionSummary(
        @Path("user_id") userId: Int,
        @Path("season") season: Int
    ): Response<UserPredictionSummaryResponse>

*/

}
