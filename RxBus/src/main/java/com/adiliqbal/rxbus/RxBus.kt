package com.adiliqbal.rxbus

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

/**
 * RxBus similar to <p> <a src="https://github.com/greenrobot/EventBus" > </p>
 * using <p> <a src="https://github.com/JakeWharton/RxRelay" > </p>
 *
 * Objects need to call {@see register()} to start listening for events and {@see unregister()}
 * to unregister from RxBus.
 * Objects should unregister on their lifecycle events as it might result in leaking contexts.
 *
 * @see PublishRelay
 */
object RxBus {
    private var events: ConcurrentHashMap<Class<Any>, Any> = ConcurrentHashMap()
    private var SUBSCRIBERS_CACHE: ConcurrentHashMap<Class<Any>, Subscriber> = ConcurrentHashMap()

    private val bus = PublishRelay.create<Any>()


    /**
     * Sends an event that will only be consumed by real-time subscribers
     *
     * @param update
     * @param <E>
    </E> */
    fun <E : Any> post(update: E) {
        synchronized(bus) {
            bus.accept(update)
        }
    }

    /**
     * Sends an event that will be stored until consumed. For example, if a class
     * subscribes to this event long after it is fired, it can still consume this event
     * by calling asObservable().
     *
     * @param event
     * @param <E>
    </E> */
    fun postSticky(event: Any) {
        synchronized(events) {
            events[event.javaClass] = event
        }
        bus.accept(event)
    }

    fun removeSticky(event: Any) {
        synchronized(events) {
            events.remove(event.javaClass)
        }
    }

    fun <E> removeSticky(type: Class<E>) {
        synchronized(events) {
            events.remove(type as Class<Any>)
        }
    }

    fun hasConsumers(): Boolean {
        return bus.hasObservers()
    }

    fun <E> observe(type: Class<E>): Observable<E> {
        return Observable.merge(observable(type), stickyObservable(type))
    }

    /**
     * Observe non-sticky events
     *
     * @param type
     * @return
     */
    private fun <E> observable(type: Class<E>): Observable<E> {
        return bus.ofType(type)
    }

    /**
     * Observer sticky events. If the event exists, it will be forwarded to observers
     * otherwise Observable will not emit
     *
     * @param type
     * @return
     */
    private fun <E> stickyObservable(type: Class<E>): Observable<E> {
        return Observable.just(Optional.of(events[type as Class<Any>]))
                .flatMap { z ->
                    if (z.isPresent) Observable.just(z.get()) as Observable<E>
                    else Observable.empty()
                }
    }

    /**
     * Finds all methods annotated with {@see Consumer.class} and then creates new {@see Observer}
     * from Bus which invoke methods on events
     *
     * @param listener
     *          Class object
     */
    fun register(listener: Any) {
        val consumers: List<Method> = AnnotationProcessor.getConsumerMethods(listener)
        val disposables = CompositeDisposable()

        val subscriber = Subscriber(listener)
        subscriber.registered = true

        synchronized(this) {
            for (method: Method in consumers) {
                disposables.add(observe(method.parameterTypes[0])
                        .subscribe { result ->
                            if (subscriber.registered)
                                method.invoke(subscriber.subscriber, result)
                        })
            }
        }

        subscriber.compositeDisposable = disposables
        SUBSCRIBERS_CACHE[listener.javaClass] = subscriber
    }

    fun unregister(listener: Any) {
        val clazz = listener.javaClass
        val subscriber: Subscriber? = SUBSCRIBERS_CACHE[clazz]
        if (subscriber != null) {
            subscriber.subscriber = null
            subscriber.compositeDisposable.dispose()
            subscriber.registered = false
        }
        SUBSCRIBERS_CACHE.remove(clazz)
    }
}