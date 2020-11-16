package uk.co.coop.app.engine.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty

abstract class BaseSettingsRepository ( val context: Context) {

    protected val defaultSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    protected val encryptedSharedPreferences = EncryptedSharedPreferences(context, defaultSharedPreferences)

    protected inner class EncryptedStringDelegate(key: String)
        : CachingStringDelegate(encryptedSharedPreferences, key)
    protected open class StringDelegate(private val sharedPreferences: SharedPreferences,
                                        private val key: String) {

        open operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
            return sharedPreferences.getString(key, null)?.let {
                if (it.isBlank()) null else it
            }
        }

        open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            val editor = sharedPreferences.edit()
            if (value != null) {
                editor.putString(key, value)
            } else {
                editor.remove(key)
            }
            editor.apply()
        }
    }

    protected open class CachingStringDelegate(sharedPreferences: SharedPreferences, key: String): StringDelegate(sharedPreferences, key) {

        private var cache: String? = null

        override operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
            return cache ?: (super.getValue(thisRef, property)?.also { cache = it })
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            cache = value
            super.setValue(thisRef, property, value)
        }
    }

    protected class BooleanDelegate(private val sharedPreferences: SharedPreferences,
                                    private val key: String) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean? {
            return if (sharedPreferences.contains(key)) sharedPreferences.getBoolean(key, false) else null
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean?) {
            val editor = sharedPreferences.edit()
            if (value != null) {
                editor.putBoolean(key, value)
            } else {
                editor.remove(key)
            }
            editor.apply()
        }
    }

    protected open class IntegerDelegate(private val sharedPreferences: SharedPreferences,
                                    private val key: String) {
        open fun getValue(thisRef: Any?, property: KProperty<*>): Int? {
            return if (sharedPreferences.contains(key)) sharedPreferences.getInt(key, 0) else null
        }

        open fun setValue(thisRef: Any?, property: KProperty<*>, value: Int?) {
            val editor = sharedPreferences.edit()
            if (value != null) {
                editor.putInt(key, value)
            } else {
                editor.remove(key)
            }
            editor.apply()
        }
    }


    protected open class CachingIntegerDelegate(sharedPreferences: SharedPreferences, key: String): IntegerDelegate(sharedPreferences, key) {

        private var cache: Int? = null

        override operator fun getValue(thisRef: Any?, property: KProperty<*>): Int? {
            return cache ?: (super.getValue(thisRef, property)?.also { cache = it })
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int?) {
            cache = value
            super.setValue(thisRef, property, value)
        }
    }

    class ZonedDateTimeDelegate(private val sharedPreferences: SharedPreferences,
                                private val key: String) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>): ZonedDateTime? {
            return sharedPreferences.getString(key, null)?.let {
                ZonedDateTime.parse(it, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            }
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ZonedDateTime?) {
            val editor = sharedPreferences.edit()
            if (value != null) {
                editor.putString(key, value.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            } else {
                editor.remove(key)

            }
            editor.apply()
        }
    }
}