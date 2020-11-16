package uk.co.coop.app.engine.settings

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal


/**
 * Created by dant on 27/02/2018, stolen by samdc 12/07/2019
 */
class EncryptedSharedPreferences(val context: Context, val sharedPreferences: SharedPreferences) : SharedPreferences {

    companion object {
        private const val TAG = "Encryption"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val SYMMETRIC_KEY_KEY = "SYMMETRIC_KEY"
        private const val IV_KEY = "IV"
        private const val ALIAS = "CoopApp"

        /* Algorithms used for encryption or decryption
        * Format is Algorithm/Mode/Padding */
        private const val RSA_MODE = "RSA/ECB/PKCS1Padding"
        private const val AES_MODE = "AES/CBC/PKCS5Padding"
    }

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
    
    private val setJsonAdapter: JsonAdapter<MutableSet<String>> = {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(MutableSet::class.java, String::class.java)
        moshi.adapter(type)
    }()

    init {
        keyStore.load(null)

        if (!keyStore.containsAlias(ALIAS) ||
            !sharedPreferences.contains(SYMMETRIC_KEY_KEY) ||
            !sharedPreferences.contains(IV_KEY)) {
            
            generateAsymmetricKey()
            generateSymmetricKey()
        }
    }

    /* RSA encryption was not designed to work with large amount of data.
    * Solution is to encrypt and decrypt data with a symmetric key and encrypt and decrypt this key with an
    * asymmetric key. */

    /* Create asymmetric private-public key pair.
    * Android Key Store provide will automatically save the asymmetric key in KeyStore. */
    private fun generateAsymmetricKey() {
        try {
            val generator = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE)

            initGeneratorWithKeyGenParameterSpec(generator)

            generator.generateKeyPair()
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
        }
    }

    /* Create symmetric key and store in shared preferences */
    private fun generateSymmetricKey() {
        val randomSecureRandom = SecureRandom.getInstance("SHA1PRNG")
        val iv = ByteArray(16)
        randomSecureRandom.nextBytes(iv)

        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256)

        val key = keyGen.generateKey()
        setSymmetricKey(key.encoded, iv)
    }

    /* Initialise KeyPairGenerator using KeyPairGeneratorSpec
    * Asymmetric keys must be signed with a certificate, asymmetric keys cannot be signed without one
    * As both the public and private key are used a our single application we can create a self signed certificate */
    private fun initGeneratorWithKeyPairGeneratorSpec(generator: KeyPairGenerator) {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 25)

        @Suppress("DEPRECATION")
        val spec = KeyPairGeneratorSpec.Builder(context)
            .setAlias(ALIAS)
            .setSubject(X500Principal("O=$ALIAS"))
            .setSerialNumber(BigInteger.ONE)
            .setStartDate(start.time)
            .setEndDate(end.time)
            .build()

        generator.initialize(spec)
    }

    /* Android M introduced KeyGenParameterSpec used to initialise asymmetric and symmetric keys */
    @TargetApi(Build.VERSION_CODES.M)
    private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator) {
        val builder = KeyGenParameterSpec.Builder(ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)

        generator.initialize(builder.build())
    }

    private fun setSymmetricKey(key: ByteArray, iv: ByteArray) {
        try {
            val certificate = keyStore.getCertificate(ALIAS)
            val cipher = Cipher.getInstance(RSA_MODE)
            cipher.init(Cipher.ENCRYPT_MODE, certificate.publicKey)


            val encryptedKey = cipher.doFinal(key)
            val encryptedIv = cipher.doFinal(iv)
            sharedPreferences.edit()
                .putString(SYMMETRIC_KEY_KEY, Base64.encodeToString(encryptedKey, Base64.DEFAULT))
                .putString(IV_KEY, Base64.encodeToString(encryptedIv, Base64.DEFAULT))
                .apply()
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
        }
    }

    private fun getSymmetricKeyAndIv(): Pair<ByteArray, ByteArray> {
        try {
            val encryptedSymmetricKey = sharedPreferences.getString(SYMMETRIC_KEY_KEY, "")
            val encryptedIv = sharedPreferences.getString(IV_KEY, "")

            val privateKey = keyStore.getKey(ALIAS, null)

            val output = Cipher.getInstance(RSA_MODE)
            output.init(Cipher.DECRYPT_MODE, privateKey)


            val decryptedKey = output.doFinal(Base64.decode(encryptedSymmetricKey, Base64.DEFAULT))
            val decryptedIv = output.doFinal(Base64.decode(encryptedIv, Base64.DEFAULT))

            return Pair(decryptedKey, decryptedIv)
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
        }

        return Pair(ByteArray(0), ByteArray(0))
    }

    /* Encryption */

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, IllegalBlockSizeException::class, InvalidAlgorithmParameterException::class)
    private fun encryptString(plainText: String): String {
        val (symmetricKey, iv) = getSymmetricKeyAndIv()

        val symmetricKeySpec = SecretKeySpec(symmetricKey, "AES")
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher: Cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKeySpec, ivParameterSpec)

        var encrypted = ByteArray(0)
        try {
            encrypted = cipher.doFinal(plainText.toByteArray(charset("UTF-8")))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    /* Decryption */

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, IllegalBlockSizeException::class, InvalidAlgorithmParameterException::class)
    private fun decryptString(cipherText: String): String? {

        val (symmetricKey, iv) = getSymmetricKeyAndIv()

        val symmetricKeySpec = SecretKeySpec(symmetricKey, "AES")
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.DECRYPT_MODE, symmetricKeySpec, ivParameterSpec)

        try {
            cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT))?.let { decrypted ->
                return String(decrypted, 0, decrypted.size, charset("UTF-8"))
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return null
    }

    /* SharedPreferences */

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun contains(key: String?): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        val encryptedData = sharedPreferences.getString(key, "")
        encryptedData?.let {
            decryptString(it)?.let { decryptedString ->
                return decryptedString.toBoolean()
            }
        }
        return defValue
    }

    override fun getInt(key: String?, defValue: Int): Int {
        val encryptedData = sharedPreferences.getString(key, "")
        encryptedData?.let {
            decryptString(it)?.let { decryptedString ->
                return decryptedString.toInt()
            }
        }
        return defValue
    }

    override fun getLong(key: String, defValue: Long): Long {
        val encryptedData = sharedPreferences.getString(key, "")
        encryptedData?.let {
            decryptString(it)?.let { decryptedString ->
                return decryptedString.toLong()
            }
        }
        return defValue
    }

    override fun getFloat(key: String, defValue: Float): Float {
        val encryptedData = sharedPreferences.getString(key, "")
        encryptedData?.let {
            decryptString(it)?.let { decryptedString ->
                return decryptedString.toFloat()
            }
        }
        return defValue
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        val encryptedData = sharedPreferences.getString(key, "")
        encryptedData?.let {
            decryptString(it)?.let { decryptedString ->
                setJsonAdapter.fromJson(decryptedString)
            }
        }
        return defValues ?: mutableSetOf()
    }

    override fun getString(key: String?, defValue: String?): String {
        if (key == null) return defValue ?: ""

        val encryptedData = sharedPreferences.getString(key, "")

        if (!encryptedData.isNullOrBlank()) {
            decryptString(encryptedData)?.let { decryptedString ->
                return decryptedString
            }
        }

        return defValue ?: ""
    }

    override fun getAll(): MutableMap<String, *> {
        val allDecrypted = HashMap<String, Any>()
        for (entry in sharedPreferences.all) {
            sharedPreferences.getString(entry.key, "")?.let {
                decryptString(it)?.let { decryptedString ->
                    allDecrypted.put(entry.key, decryptedString)
                }
            }
        }
        return allDecrypted
    }

    override fun edit(): SharedPreferences.Editor {
        return EncryptedSharedPreferencesEditor()
    }

    private inner class EncryptedSharedPreferencesEditor : SharedPreferences.Editor {

        private val defaultSharedPreferencesEditor = sharedPreferences.edit()

        override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
            return defaultSharedPreferencesEditor.putString(key, encryptString(value.toString()))
        }

        override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
            return defaultSharedPreferencesEditor.putString(key, encryptString(value.toString()))
        }

        override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
            return defaultSharedPreferencesEditor.putString(key, encryptString(value.toString()))
        }

        override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor {
            return defaultSharedPreferencesEditor.putString(key, encryptString(setJsonAdapter.toJson(values)))
        }

        override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
            return defaultSharedPreferencesEditor.putString(key, encryptString(value.toString()))
        }

        override fun putString(key: String?, value: String?): SharedPreferences.Editor {
            return defaultSharedPreferencesEditor.putString(key ?: "", encryptString(value ?: ""))
        }

        override fun remove(key: String): SharedPreferences.Editor {
            return defaultSharedPreferencesEditor.remove(key)
        }

        override fun clear(): SharedPreferences.Editor {
            return defaultSharedPreferencesEditor.clear()
        }

        override fun apply() {
            defaultSharedPreferencesEditor.apply()
        }

        override fun commit(): Boolean {
            return defaultSharedPreferencesEditor.commit()
        }
    }
}