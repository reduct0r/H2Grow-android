package com.h2grow.app.data.local

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.RegistryConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.security.GeneralSecurityException

@Singleton
class TokenEncryptor @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    companion object {
        private const val KEYSET_NAME = "tink_refresh_keyset"
        private const val PREF_FILE_NAME = "tink_keyset_prefs"
        private const val MASTER_KEY_URI = "android-keystore://tink_master_key_refresh"
        private val ASSOCIATED_DATA = "refresh_token_v1".toByteArray(Charsets.UTF_8)
    }

    private val aead: Aead by lazy {
        try {
            com.google.crypto.tink.aead.AeadConfig.register()

            AndroidKeysetManager.Builder()
                .withSharedPref(
                    context,
                    KEYSET_NAME,
                    PREF_FILE_NAME
                )
                .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle
                .getPrimitive(RegistryConfiguration.get(), Aead::class.java)

        } catch (e: Exception) {
            throw RuntimeException("Failed to initialize Tink AEAD", e)
        }
    }

    fun encrypt(plainText: String): String {
        return try {
            val plainBytes = plainText.toByteArray(Charsets.UTF_8)
            val encryptedBytes = aead.encrypt(plainBytes, ASSOCIATED_DATA)
            android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.NO_WRAP)
        } catch (e: GeneralSecurityException) {
            throw SecurityException("Refresh token encryption error", e)
        }
    }

    fun decrypt(encryptedBase64: String): String {
        return try {
            val encryptedBytes = android.util.Base64.decode(encryptedBase64, android.util.Base64.DEFAULT)
            val decryptedBytes = try {
                aead.decrypt(encryptedBytes, ASSOCIATED_DATA)
            } catch (_: GeneralSecurityException) {
                aead.decrypt(encryptedBytes, null)
            }
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: GeneralSecurityException) {
            throw SecurityException("Error decrypting refresh token. The token may be damaged or the key has been changed", e)
        }
    }
}