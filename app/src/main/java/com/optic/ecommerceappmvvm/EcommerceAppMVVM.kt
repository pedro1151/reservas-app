package com.optic.ecommerceappmvvm

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.optic.ecommerceappmvvm.core.locale.LocaleManager
import com.optic.ecommerceappmvvm.data.dataSource.local.datastore.AuthDatastore
import com.optic.ecommerceappmvvm.data.worker.UpdateFixturesWorker
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.LocaleEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class EcommerceAppMVVM: Application()  {

    private val FIXTURES_WORK_TAG = "fixtures_worker"

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.attachBaseContext(base))
    }

    override fun onCreate() {
        super.onCreate()
        scheduleFixtureUpdates()
    }

    private fun scheduleFixtureUpdates() {

        val request =
            PeriodicWorkRequestBuilder<UpdateFixturesWorker>(
                15, TimeUnit.MINUTES // mínimo permitido por Android
            )
                .addTag(FIXTURES_WORK_TAG) // 👈 clave
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "update_fixtures_today",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
        observeWorkerStatus()

    }

    private fun observeWorkerStatus() {
        WorkManager.getInstance(this)
            .getWorkInfosByTagLiveData(FIXTURES_WORK_TAG)
            .observeForever { workInfos ->
                workInfos.forEach { workInfo ->
                    android.util.Log.d(
                        "FixturesWorker",
                        """
                    🔎 Worker status:
                    id=${workInfo.id}
                    state=${workInfo.state}
                    runAttemptCount=${workInfo.runAttemptCount}
                    """.trimIndent()
                    )
                }
            }
    }
}