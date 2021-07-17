package io.uvera.template.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
    private lateinit var instance: Logger

    override operator fun getValue(thisRef: R, property: KProperty<*>): Logger =
        if (this::instance.isInitialized)
            instance
        else {
            instance = LoggerFactory.getLogger(thisRef::class.java)
            instance
        }
}

fun <T : Any> loggerDelegate(): ReadOnlyProperty<T, Logger> = LoggerDelegate()
