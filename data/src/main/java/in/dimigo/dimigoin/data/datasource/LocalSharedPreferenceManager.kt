package `in`.dimigo.dimigoin.data.datasource

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class LocalSharedPreferenceManager(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    var accessToken: String?
        get() = sharedPreferences.getString(PREF_ACCESS_TOKEN, null)
        set(value) = sharedPreferences.edit {
            this.putString(PREF_ACCESS_TOKEN, value)
        }

    var refreshToken: String?
        get() = sharedPreferences.getString(PREF_REFRESH_TOKEN, null)
        set(value) = sharedPreferences.edit {
            this.putString(PREF_REFRESH_TOKEN, value)
        }

    private val sharedPreferences = EncryptedSharedPreferences.create(
        FILE_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val TAG = "LocalSharedPreferenceMa"
        private const val FILE_NAME = "dimigoin_enc_shared_pref"
        private const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
        private const val PREF_REFRESH_TOKEN = "PREF_REFRESH_TOKEN"
    }
}