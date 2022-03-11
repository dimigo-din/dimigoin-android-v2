package `in`.dimigo.dimigoin.data.datasource

import `in`.dimigo.dimigoin.data.mapper.toAttendanceLog
import `in`.dimigo.dimigoin.data.mapper.toJsonString
import `in`.dimigo.dimigoin.data.mapper.toPlace
import `in`.dimigo.dimigoin.data.mapper.toSchedule
import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.schedule.Schedule
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class LocalSharedPreferenceManager(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val encryptedSharedPreference = EncryptedSharedPreferences.create(
        ENCRYPTED_FILE_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val sharedPreference = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)

    var accessToken: String?
        get() = encryptedSharedPreference.getString(PREF_ACCESS_TOKEN, null)
        set(value) = encryptedSharedPreference.edit {
            this.putString(PREF_ACCESS_TOKEN, value)
        }

    var refreshToken: String?
        get() = encryptedSharedPreference.getString(PREF_REFRESH_TOKEN, null)
        set(value) = encryptedSharedPreference.edit {
            this.putString(PREF_REFRESH_TOKEN, value)
        }

    var favoriteAttendanceLogs: List<AttendanceLog>
        get() = sharedPreference.getStringSet(PREF_FAVORITES, setOf())?.map(String::toAttendanceLog)
            ?: emptyList()
        set(value) = sharedPreference.edit {
            putStringSet(PREF_FAVORITES, value.map(AttendanceLog::toJsonString).toSet())
        }

    var places: List<Place>?
        get() = sharedPreference.getStringSet(PREF_FAVORITES, setOf())?.map(String::toPlace)
            ?: emptyList()
        set(value) = sharedPreference.edit {
            putStringSet(PREF_FAVORITES, value?.map(Place::toJsonString)?.toSet())
        }

    var schedules: List<Schedule>?
        get() = sharedPreference.getStringSet(PREF_FAVORITES, setOf())?.map(String::toSchedule)
            ?: emptyList()
        set(value) = sharedPreference.edit {
            putStringSet(PREF_FAVORITES, value?.map(Schedule::toJsonString)?.toSet())
        }

    companion object {
        private const val TAG = "LocalSharedPreferenceMa"
        private const val ENCRYPTED_FILE_NAME = "dimigoin_enc_shared_pref"
        private const val FILE_NAME = "dimigoin_shared_pref"
        private const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
        private const val PREF_REFRESH_TOKEN = "PREF_REFRESH_TOKEN"
        private const val PREF_FAVORITES = "PREF_FAVORITES"
    }
}
