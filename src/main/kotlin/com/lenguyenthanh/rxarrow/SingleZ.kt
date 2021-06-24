package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.identity
import arrow.core.left
import arrow.core.right
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport

typealias SingleZ<E, A> = Single<Either<E, A>>

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> SingleZ<E, T>.mapZ(crossinline mapper: (T) -> R): SingleZ<E, R> {
    return mapEither { mapper(it).right() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> SingleZ<E1, T>.mapEither(crossinline mapper: (T) -> Either<E2, R>): SingleZ<E, R>
        where E1 : E, E2 : E {
    return map { it.fold({ e -> e.left() }) { t -> mapper(t) } }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, T> SingleZ<E1, T>.mapLeft(crossinline mapper: (E1) -> E2): SingleZ<E2, T> {
    return map { either -> either.mapLeft { mapper(it) } }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> SingleZ<E, T>.flatMapZ(crossinline mapper: (T) -> Single<R>): SingleZ<E, R> {
    return flatMap { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> SingleZ<E1, T>.flatMapEither(crossinline mapper: (T) -> SingleZ<E2, R>): SingleZ<E, R>
        where E1 : E, E2 : E {
    return flatMap { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> SingleZ<E, T>.flatMapObservableZ(crossinline mapper: (T) -> Observable<R>): ObservableZ<E, R> {
    return flatMapObservable { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> SingleZ<E1, T>.flatMapObservableEither(crossinline mapper: (T) -> ObservableZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return flatMapObservable { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> SingleZ<E, T>.fix(toThrowable: (E) -> Throwable): Single<T> {
    return flatMap {
        it.fold({ Single.error<T> { toThrowable(it) } }, { Single.just(it) })
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> SingleZ<E, T>.fix(): Single<T> where E : Throwable {
    return fix(::identity)
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> Single<T>.z(toError: (Throwable) -> E): SingleZ<E, T> {
    return this.map { it.right() as Either<E, T> }
        .onErrorReturn { toError(it).left() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Single<T>.z(): SingleZ<Throwable, T> {
    return z(::identity)
}