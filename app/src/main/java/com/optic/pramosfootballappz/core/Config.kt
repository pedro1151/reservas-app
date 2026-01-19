package com.optic.pramosfootballappz.core

object Config {

    const val URL_BASE = "http://136.113.44.38/"

    // IP LOCAL -DESARROLLO: 10.0.2.2

    /*
    const val BASE_URL = "http://10.0.2.2:8000/"
    const val BASE_URL_TEAMS = "http://10.0.2.2:8004/"
    const val BASE_URL_EXTERNAL_SERVICES = "http://10.0.2.2:8002/"
    const val BASE_URL_TRIVIAS = "http://10.0.2.2:8003/"
    const val AUTH_PREFERENCE = "AUTH_PREF"
    const val AUTH_KEY = "AUTH_KEY"

     */


    // IP DE GCP --REMOTO -PRODUCCION : 34.136.16.162  ( SIN PUERTO ya que internamente usa nginx puerto 80)
    const val BASE_URL = URL_BASE
    const val BASE_URL_TEAMS = URL_BASE
    const val BASE_URL_EXTERNAL_SERVICES = URL_BASE
    const val BASE_URL_TRIVIAS = URL_BASE
    const val AUTH_PREFERENCE = "AUTH_PREF"
    const val AUTH_KEY = "AUTH_KEY"


    /*google auht */
    const val CLIENT_ID_GOOGLE ="507074065531-g7f3sp1kf0dlnf7fj7bssfcn8i1138ff.apps.googleusercontent.com"

}