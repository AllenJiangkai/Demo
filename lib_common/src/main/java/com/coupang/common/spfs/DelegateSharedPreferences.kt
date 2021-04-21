package com.coupang.common.spfs

import android.content.Context
import android.content.SharedPreferences
import com.coupang.common.utils.ContextUtils

open class DelegateSharedPreferences(
    val fileName: String,
    private val mode: Int = Context.MODE_PRIVATE
) {

    companion object {
        private var context: Context? = ContextUtils.getApplication()
        fun init(application: Context) {
            context = application
        }
    }

    internal val sp by lazy { context!!.getSharedPreferences(fileName, mode) }

    fun <T : Any> getValue(key: String, default: T): T {
        val dealKey = normalizeKey(key)
        return when (default) {
            is String -> sp.getString(dealKey, default) as T
            is Boolean -> sp.getBoolean(dealKey, default) as T
            is Int -> sp.getInt(dealKey, default) as T
            is Long -> sp.getLong(dealKey, default) as T
            is Float -> sp.getFloat(dealKey, default) as T
            is Set<*> -> sp.getStringSet(dealKey, default as MutableSet<String>) as T
            else -> throw RuntimeException("sp(${fileName}) get unknown type of ${default.javaClass} with key $key")
        }
    }

    fun <T : Any> setValue(key: String, value: T) {
        val dealKey = normalizeKey(key)
        when (value) {
            is String -> sp.edit().putString(dealKey, value).commit()
            is Boolean -> sp.edit().putBoolean(dealKey, value).commit()
            is Int -> sp.edit().putInt(dealKey, value).commit()
            is Long -> sp.edit().putLong(dealKey, value).commit()
            is Float -> sp.edit().putFloat(dealKey, value).commit()
            is Set<*> -> sp.edit().putStringSet(dealKey, value as MutableSet<String>).commit()
            else -> throw RuntimeException("sp(${fileName}) set unknown type of ${value.javaClass} with key $key")
        }
    }

    fun contain(key: String): Boolean {
        val dealKey = normalizeKey(key)
        return sp.contains(dealKey)
    }

    fun remove(key: String) {
        val dealKey = normalizeKey(key)
        sp.edit().remove(dealKey).commit()
    }

    fun clear() {
        sp.edit().clear().commit()
    }

    open fun getAll(): MutableMap<String, *> {
        return sp.all
    }

    fun registerSharedPreferencesChanged(l: SharedPreferences.OnSharedPreferenceChangeListener) {
        sp.registerOnSharedPreferenceChangeListener(l)
    }

    fun unregisterSharedPreferencesChanged(l: SharedPreferences.OnSharedPreferenceChangeListener) {
        sp.unregisterOnSharedPreferenceChangeListener(l)
    }

    open protected fun normalizeKey(key: String): String {
        return key
    }

}