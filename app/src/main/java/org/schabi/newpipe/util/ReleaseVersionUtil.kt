package org.schabi.newpipe.util

import android.content.pm.PackageManager
import androidx.core.content.pm.PackageInfoCompat
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.schabi.newpipe.App
import org.schabi.newpipe.error.ErrorInfo
import org.schabi.newpipe.error.ErrorUtil.Companion.createNotification
import org.schabi.newpipe.error.UserAction

object ReleaseVersionUtil {
    // Public key of the certificate that is used in NewPipe release versions
    private const val RELEASE_CERT_PUBLIC_KEY_SHA256 =
        "cb84069bd68116bafae5ee4ee5b08a567aa6d898404e7cb12f9e756df5cf5cab"

    // Nexos Tube: huella SHA-256 del certificado de firma propio (ver /root/nexos-keystore).
    private const val NEXOS_RELEASE_CERT_PUBLIC_KEY_SHA256 =
        "e04aa29000b0fa02981c486b8ee7ec6266b574a8dfa6e82676f53fafc5c5055f"

    @OptIn(ExperimentalStdlibApi::class)
    val isReleaseApk by lazy {
        @Suppress("NewApi")
        val certificates = mapOf(
            RELEASE_CERT_PUBLIC_KEY_SHA256.hexToByteArray() to PackageManager.CERT_INPUT_SHA256,
            NEXOS_RELEASE_CERT_PUBLIC_KEY_SHA256.hexToByteArray()
                to PackageManager.CERT_INPUT_SHA256
        )
        val app = App.instance
        try {
            PackageInfoCompat.hasSignatures(app.packageManager, app.packageName, certificates, false)
        } catch (e: PackageManager.NameNotFoundException) {
            createNotification(
                app,
                ErrorInfo(e, UserAction.CHECK_FOR_NEW_APP_VERSION, "Could not find package info")
            )
            false
        }
    }

    fun isLastUpdateCheckExpired(expiry: Long): Boolean {
        return Instant.ofEpochSecond(expiry) < Instant.now()
    }

    /**
     * Compare two dot-separated version names numerically, part by part.
     * Non-numeric parts count as zero.
     *
     * @return Negative if [latest] is older, zero if equal, positive if [latest] is newer
     */
    @JvmStatic
    fun compareVersionNames(latest: String, current: String): Int {
        val latestParts = latest.split(".")
        val currentParts = current.split(".")
        for (i in 0 until maxOf(latestParts.size, currentParts.size)) {
            val latestPart = latestParts.getOrNull(i)?.toIntOrNull() ?: 0
            val currentPart = currentParts.getOrNull(i)?.toIntOrNull() ?: 0
            if (latestPart != currentPart) {
                return latestPart.compareTo(currentPart)
            }
        }
        return 0
    }

    /**
     * Coerce expiry date time in between 6 hours and 72 hours from now
     *
     * @return Epoch second of expiry date time
     */
    fun coerceUpdateCheckExpiry(expiryString: String?): Long {
        val nowPlus6Hours = ZonedDateTime.now().plusHours(6)
        val expiry = expiryString?.let {
            ZonedDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(it))
                .coerceIn(nowPlus6Hours, nowPlus6Hours.plusHours(66))
        } ?: nowPlus6Hours
        return expiry.toEpochSecond()
    }
}
