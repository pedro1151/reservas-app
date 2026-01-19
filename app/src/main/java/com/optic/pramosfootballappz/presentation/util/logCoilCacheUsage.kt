package com.optic.pramosfootballappz.presentation.util

import android.util.Log
import coil.ImageLoader
import java.io.File

fun logCoilCacheUsage(loader: ImageLoader) {
// -------------------------------
// MemoryCache
// -------------------------------
    loader.memoryCache?.let { memoryCache ->
        val usedMB = memoryCache.size.toDouble() / (1024 * 1024)
        val maxMB = memoryCache.maxSize.toDouble() / (1024 * 1024)
        Log.d("CoilCacheUsage", "MemoryCache: %.2f MB usados de %.2f MB permitidos".format(usedMB, maxMB))
    }


// -------------------------------
// DiskCache
// -------------------------------
    loader.diskCache?.let { diskCache ->
        val directoryFile: File = diskCache.directory.toFile()
        if (directoryFile.exists() && directoryFile.isDirectory) {
            val totalMB = (directoryFile.listFiles()?.sumOf { it.length() }?.toDouble() ?: 0.0) / (1024 * 1024)
            val maxMB = diskCache.maxSize.toDouble() / (1024 * 1024)
            Log.d("CoilCacheUsage", "DiskCache: %.2f MB usados de %.2f MB permitidos".format(totalMB, maxMB))
        } else {
            Log.d("CoilCacheUsage", "DiskCache: directorio no existe")
        }
    }


}
