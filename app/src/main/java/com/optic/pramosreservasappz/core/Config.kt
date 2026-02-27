package com.optic.pramosreservasappz.core

object  Config {

    const val URL_BASE = "http://136.113.44.38/"
    //const val URL_BASE = "http://10.0.2.2/"
/*
    const val BASE_URL = "http://10.0.2.2:8006/"  //reservas auth
    const val BASE_URL_TEAMS = "http://10.0.2.2:8008/"  //reservas
    const val BASE_URL_EXTERNAL_SERVICES = "http://10.0.2.2:8007/" //reservas external
    const val AUTH_PREFERENCE = "AUTH_PREF"
    const val AUTH_KEY = "AUTH_KEY"
*/



    // IP DE GCP --REMOTO -PRODUCCION : 34.136.16.162  ( SIN PUERTO ya que internamente usa nginx puerto 80)
    const val BASE_URL = URL_BASE
    const val BASE_URL_TEAMS = URL_BASE
    const val BASE_URL_EXTERNAL_SERVICES = URL_BASE
    const val AUTH_PREFERENCE = "AUTH_PREF"
    const val AUTH_KEY = "AUTH_KEY"


    /*google auht */
    const val CLIENT_ID_GOOGLE ="433710092520-m7644hv1215dgfqr11ks4dseusl8rcgl.apps.googleusercontent.com"

    // API KEY APLICATION
    const val APP_API_KEY = "pramos1151_key_reservas_zk" //
}