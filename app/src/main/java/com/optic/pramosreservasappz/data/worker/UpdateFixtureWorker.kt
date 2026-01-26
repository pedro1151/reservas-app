package com.optic.pramosreservasappz.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate


@HiltWorker
class UpdateFixturesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val teamRepository: ReservasRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        return try {
            /*
            teamRepository.updateFixturesByDate(today, 1000)
            teamRepository.updateFixturesByDate(yesterday, 1000)

             */

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
