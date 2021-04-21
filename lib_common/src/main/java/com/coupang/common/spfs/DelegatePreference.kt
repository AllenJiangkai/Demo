package com.coupang.common.spfs

import kotlin.reflect.KProperty

class DelegatePreference<T : Any>(
    private val sp: DelegateSharedPreferences,
    private val key: String,
    private val default: T
) {

    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        return try {
            val value = sp.getValue(key, default)
            value
        } catch (ignore: Exception) {
            default
        }
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        try {
            sp.setValue(key, value)
        } catch (ignore: Exception) {

        }
    }
}