package com.divyanshu.workshopapp.utils

import android.content.Context
import com.divyanshu.workshopapp.R
import okio.IOException
import okio.buffer
import okio.source
import java.nio.charset.Charset

class Utils {
    companion object {
        fun readJsonFromAssets(context: Context, filePath: String): String? {
            try {
                val source = context.assets.open(filePath).source().buffer()
                return source.readByteString().string(Charset.forName("utf-8"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        fun String.getMappedPosters() = when (this) {
            "poster1.jpg" -> R.drawable.poster1
            "poster2.jpg" -> R.drawable.poster2
            "poster3.jpg" -> R.drawable.poster3
            "poster4.jpg" -> R.drawable.poster4
            "poster5.jpg" -> R.drawable.poster5
            "poster6.jpg" -> R.drawable.poster6
            "poster7.jpg" -> R.drawable.poster7
            "poster8.jpg" -> R.drawable.poster8
            "poster9.jpg" -> R.drawable.poster9
            else -> R.drawable.placeholder_for_missing_posters
        }
    }
}