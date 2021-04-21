package com.coupang.common.network

import java.io.Serializable
import java.lang.reflect.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

/**
 * @author rexy
 */
object TypeUtils {

    inline fun <reified T : Any> type(normalized: Boolean = false): Type {
        val type = object : TypeReference<T>() {}.type
        return if (normalized) {
            normalizeType(type)
        } else {
            type
        }
    }

    fun normalizeType(type: Type): Type {
        if (type is ParameterizedType && type !is ParameterizedTypeImpl) {
            return ParameterizedTypeImpl(type.ownerType, type.rawType, *type.actualTypeArguments)
        }
        return type
    }

    fun getRawType(type: Type): Class<*> {
        if (type is Class<*>) {
            return type
        } else if (type is ParameterizedType) {
            val rawType = type.rawType
            if (rawType is Class<*>) {
                return rawType
            }
        } else if (type is GenericArrayType) {
            val componentType = type.genericComponentType
            return java.lang.reflect.Array.newInstance(getRawType(componentType), 0).javaClass
        } else if (type is TypeVariable<*>) {
            return Any::class.java
        } else if (type is WildcardType) {
            return getRawType(type.upperBounds[0])
        }
        throw IllegalArgumentException("Expected a Class,ParameterizedType or GenericArrayType,but <$type> is of type ${type.javaClass.name}")
    }

    fun equalType(a: Type, b: Type): Boolean {
        return when {
            a === b -> true
            a is Class<*> -> a == b
            a is ParameterizedType -> {
                if (b !is ParameterizedType) {
                    return false
                }
                val pa = a
                val pb = b
                pa.ownerType == pb.ownerType && pa.rawType == pb.rawType && Arrays.equals(
                    pa.actualTypeArguments, pb.actualTypeArguments
                )
            }
            a is GenericArrayType -> {
                if (b !is GenericArrayType) {
                    return false
                }
                equalType(
                    a.genericComponentType, b.genericComponentType
                )
            }
            a is WildcardType -> {
                if (b !is WildcardType) {
                    return false
                }
                val wa = a
                val wb = b
                Arrays.equals(
                    wa.upperBounds, wb.upperBounds
                ) && Arrays.equals(wa.lowerBounds, wb.lowerBounds)
            }
            a is TypeVariable<*> -> {
                if (b !is TypeVariable<*>) {
                    return false
                }
                val ta = a
                val tb = b
                ta.genericDeclaration === tb.genericDeclaration && ta.name == tb.name
            }
            else -> false
        }
    }

    fun isCollectType(type: Type?): Boolean {
        val rawClz = (if (null == type) null else getRawType(type)) ?: return false
        if (Collection::class.java.isAssignableFrom(rawClz)) {
            return true
        }
        return Map::class.java.isAssignableFrom(rawClz)
    }

    @Throws(Exception::class)
    fun noNullOf(data: Any?, dataType: Type, deeply: Boolean = false): Any {
        val finalData = data ?: createDefaultInstanceFor(dataType)
        if (deeply && null != finalData) {
            ensureFieldNotNull(finalData)
        }
        return finalData ?: throw IllegalStateException("could not create instance for type $dataType")
    }

    private fun ensureFieldNotNull(data: Any) {
        if (data is List<*>) {
            data.forEach { item ->
                item?.also { noNullItem ->
                    ensureFieldNotNull(noNullItem)
                }
            }
        }
        if (data is Map<*, *>) {
            data.values.forEach { item ->
                item?.also { noNullItem ->
                    ensureFieldNotNull(noNullItem)
                }
            }
        }
        data.javaClass.declaredFields.forEach {
            if (!it.isAccessible) {
                it.isAccessible = true
            }
            if (null == it.get(data)) {
                createDefaultInstanceFor(it.type)?.also { obj ->
                    it.set(data, obj)
                    if (!it.type.isPrimitive) {
                        ensureFieldNotNull(obj)
                    }
                }
            }
        }
    }

    fun createDefaultInstanceFor(type: Type): Any? {
        return when (type) {
            is Class<*> -> {
                createDefaultValueByClass(type)
            }
            is ParameterizedType -> {
                createDefaultInstanceFor(type.rawType)
            }
            is GenericArrayType -> {
                java.lang.reflect.Array.newInstance(
                    getRawType(type.genericComponentType), 0
                )
            }
            else -> null
        }
    }

    private fun createDefaultValueByClass(type: Class<*>): Any? {
        if (type.isPrimitive) {
            return createDefaultValueForPrimitive(type)
        }
        if (CharSequence::class.java.isAssignableFrom(type)) {
            return ""
        }
        if (Number::class.java.isAssignableFrom(type)) {
            return 0
        }
        if (null != type.componentType) {
            return java.lang.reflect.Array.newInstance(
                getRawType(type.componentType!!), 0
            )
        }
        return createDefaultValueCollection(type) ?: createDefaultValueForReflect(type)
    }

    private fun createDefaultValueCollection(type: Class<*>): Any? {
        val rawType = getRawType(type)
        if ((rawType.modifiers and Modifier.ABSTRACT) == Modifier.ABSTRACT) {
            if (List::class.java.isAssignableFrom(rawType)) {
                return ArrayList<Any>()
            }
            if (Map::class.java.isAssignableFrom(rawType)) {
                return HashMap<Any, Any>()
            }
            if (Set::class.java.isAssignableFrom(rawType)) {
                return HashSet<Any>()
            }
        }
        return null
    }

    private fun createDefaultValueForPrimitive(type: Class<*>): Any? {
        if (Boolean::class.java === type) {
            return false
        }
        if (Int::class.java == type || Short::class.java == type || Byte::class.java == type) {
            return 0
        }
        if (Long::class.java == type) {
            return 0L
        }
        if (Float::class.java == type) {
            return 0f
        }
        if (Double::class.java == type) {
            return 0.0
        }
        return null
    }

    private fun createDefaultValueForReflect(type: Class<*>): Any? {
        try {
            return type.newInstance()
        } catch (ignore: Exception) {
            val constructors = type.constructors
            for (c in constructors) {
                val parameterTypes = c.genericParameterTypes
                val parameter = arrayOfNulls<Any>(parameterTypes.size)
                for (i in parameter.indices) {
                    parameter[i] = createDefaultInstanceFor(parameterTypes[i])
                }
                return c.newInstance(*parameter)
            }
        }
        return null
    }

    private class ParameterizedTypeImpl(
        ownerType: Type?, rawType: Type, vararg typeArguments: Type
    ) : ParameterizedType, Serializable {

        private val raw: Type = normalizeType(rawType)
        private val owner: Type? = if (null == ownerType) null else normalizeType(ownerType)
        private val arguments: Array<Type> = typeArguments.clone() as Array<Type>

        init {
            for (i in typeArguments.indices) {
                arguments[i] = normalizeType(
                    typeArguments[i]
                )
            }
        }

        override fun getActualTypeArguments(): Array<Type> {
            return arguments.clone()
        }

        override fun getRawType(): Type {
            return raw
        }

        override fun getOwnerType(): Type? {
            return owner
        }

        override fun equals(o: Any?): Boolean {
            return o is ParameterizedType && equalType(this, o)
        }

        override fun hashCode(): Int {
            return arguments.contentHashCode() xor raw.hashCode() xor Objects.hashCode(owner)
        }

        override fun toString(): String {
            val sb = StringBuilder(30 * (arguments.size + 1))
            sb.append(raw)
            if (arguments.isEmpty()) {
                return sb.toString()
            }
            sb.append('<').append(arguments[0])
            for (i in 1 until arguments.size) {
                sb.append(", ").append(arguments[i])
            }
            return sb.append('>').toString()
        }
    }

    abstract class TypeReference<T> : Comparable<TypeReference<T>> {
        val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        override fun compareTo(other: TypeReference<T>) = 0
    }

}