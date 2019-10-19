package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.identity
import arrow.core.left
import arrow.core.right
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport

typealias MaybeZ<E, A> = Maybe<Either<E, A>>

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> MaybeZ<E1, T>.mapEither(crossinline mapper: (T) -> Either<E2, R>): MaybeZ<E, R>
        where E1 : E, E2 : E {
    return map { it.fold({ e -> e.left() }) { t -> mapper(t) } }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> MaybeZ<E, T>.mapZ(crossinline mapper: (T) -> R): MaybeZ<E, R> {
    return mapEither { mapper(it).right() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, T> MaybeZ<E1, T>.mapLeft(crossinline mapper: (E1) -> E2): MaybeZ<E2, T> {
    return map { either -> either.mapLeft { mapper(it) } }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> MaybeZ<E, T>.flatMapZ(crossinline mapper: (T) -> Maybe<R>): MaybeZ<E, R> {
    return flatMap { it.flatMapMaybe(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> MaybeZ<E1, T>.flatMapEither(crossinline mapper: (T) -> MaybeZ<E2, R>): MaybeZ<E, R>
        where E1 : E, E2 : E {
    return flatMap { it.flatMapMaybeEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> MaybeZ<E, T>.flatMapObservableZ(crossinline mapper: (T) -> Observable<R>): ObservableZ<E, R> {
    return flatMapObservable { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> MaybeZ<E1, T>.flatMapObservableEither(crossinline mapper: (T) -> ObservableZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return flatMapObservable { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> MaybeZ<E, T>.fix(toThrowable: (E) -> Throwable): Maybe<T> {
    return flatMap {
        it.fold({ Maybe.error<T> { toThrowable(it) } }, { Maybe.just(it) })
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> MaybeZ<Throwable, T>.fix(): Maybe<T> {
    return fix(::identity)
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> Maybe<T>.z(toError: (Throwable) -> E): MaybeZ<E, T> {
    return map { it.right() as Either<E, T> }
        .onErrorReturn { toError(it).left() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Maybe<T>.z(): MaybeZ<Throwable, T> {
    return z(::identity)
}