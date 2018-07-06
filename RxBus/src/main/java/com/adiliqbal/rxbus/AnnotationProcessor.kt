package com.adiliqbal.rxbus

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*
import javax.annotation.processing.SupportedAnnotationTypes

@SupportedAnnotationTypes("com.adiliqbal.rxbus.Consumer")
object AnnotationProcessor {

    /**
     * Returns all methods of class annotated with @Consumer
     *
     * @see Consumer
     * @param listener
     *
     * @throws NoSuchMethodException if no annotations found in the class
     * @throws IllegalArgumentException if method does not have ay parameter or more than one parameter
     */
    fun getConsumerMethods(listener: Any): List<Method> {
        val methods = ArrayList<Method>()
        var clazz = listener.javaClass
        while (clazz != Any::class.java) {
            val allMethods = ArrayList(Arrays.asList(*clazz.declaredMethods))

            if (allMethods.isEmpty()) {
                throw NoSuchMethodException("No methods found with @Consumer annotation")
            }

            for (method in allMethods) {
                if (method.isAnnotationPresent(Consumer::class.java)) {
                    checkConsumerType(method)
                    methods.add(method)
                }
            }
            // move to the inherited class in the hierarchy in search for more methods
            clazz = clazz.superclass as Class<Any>
        }
        return methods
    }

    /**
     * Checks whether consumer has valid event type
     * @param method
     * @return parameter type
     */
    private fun checkConsumerType(method: Method) {
        if (!Modifier.isPublic(method.modifiers)) {
            throw IllegalArgumentException(method.name + " : Methods with @Consumer annotations must be public")
        }

        val parameters: Array<Class<*>> = method.parameterTypes

        if (parameters.isEmpty()) {
            throw IllegalArgumentException(method.name + " : Consumers must not be contain event parameter type")
        }
        if (parameters.size > 1) {
            throw IllegalArgumentException(method.name + " : Consumer method has multiple parameters")
        }
    }
}
