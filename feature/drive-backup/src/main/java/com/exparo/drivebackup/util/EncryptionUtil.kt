package com.exparo.drivebackup.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptionUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "EncryptionUtil"
        private const val ALGORITHM = "AES"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256"
        private const val KEY_SIZE = 256
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 16
        private const val ITERATION_COUNT = 65536
        private const val BUFFER_SIZE = 1024
        private val SALT = "ExparoWalletSalt".toByteArray()
        private const val PREFS_NAME = "EncryptionPrefs"
        private const val PREFS_KEY = "SecretKey"
    }

    private fun getSecretKeyForUser(userId: String): SecretKey {
        val factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM)
        val spec: KeySpec = PBEKeySpec(userId.toCharArray(), SALT, ITERATION_COUNT, KEY_SIZE)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, ALGORITHM)
    }

    // --- THIS IS THE MISSING FUNCTION ---
    fun getEncryptionCipher(userId: String): Cipher {
        val secretKey = getSecretKeyForUser(userId)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)
        return cipher
    }

    fun encryptFile(inputFile: File, outputFile: File, userId: String) {
        try {
            val cipher = getEncryptionCipher(userId)

            FileOutputStream(outputFile).use { fos ->
                fos.write(cipher.iv)
                FileInputStream(inputFile).use { fis ->
                    javax.crypto.CipherOutputStream(fos, cipher).use { cos ->
                        fis.copyTo(cos, BUFFER_SIZE)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error encrypting file", e)
            throw e
        }
    }

    fun decryptFile(inputFile: File, outputFile: File, userId: String) {
        try {
            val secretKey = getSecretKeyForUser(userId)
            FileInputStream(inputFile).use { fis ->
                val iv = ByteArray(GCM_IV_LENGTH)
                fis.read(iv)
                val cipher = Cipher.getInstance(TRANSFORMATION)
                val spec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

                FileOutputStream(outputFile).use { fos ->
                    javax.crypto.CipherInputStream(fis, cipher).use { cis ->
                        cis.copyTo(fos, BUFFER_SIZE)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error decrypting file", e)
            throw e
        }
    }
}