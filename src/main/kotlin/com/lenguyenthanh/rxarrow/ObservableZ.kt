package com.lenguyenthanh.rxarrow

import arrow.core.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.functions.BiFunction

typealias ObservableZ<E, A> = Observable<Either<E, A>>

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> ObservableZ<E, T>.mapZ(crossinline mapper: (T) -> R): ObservableZ<E, R> {
    return mapEither { mapper(it).right() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> ObservableZ<E1, T>.mapEither(crossinline mapper: (T) -> Either<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return map { it.flatMap { t -> mapper(t) } }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, T> ObservableZ<E1, T>.mapLeft(crossinline mapper: (E1) -> E2): ObservableZ<E2, T> {
    return map { either -> either.mapLeft { mapper(it) } }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T> ObservableZ<E, T>.filterZ(crossinline filter: (T) -> Boolean): ObservableZ<E, T> {
    return filter { either ->
        either.fold({ true }, { filter(it) })
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> ObservableZ<E, T>.flatMapZ(crossinline mapper: (T) -> Observable<R>): ObservableZ<E, R> {
    return flatMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> ObservableZ<E1, T>.flatMapEither(crossinline mapper: (T) -> ObservableZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return flatMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> ObservableZ<E, T>.concatMapZ(crossinline mapper: (T) -> Observable<R>): ObservableZ<E, R> {
    return concatMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> ObservableZ<E1, T>.concatMapEither(crossinline mapper: (T) -> ObservableZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return concatMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> ObservableZ<E, T>.switchMapZ(crossinline mapper: (T) -> Observable<R>): ObservableZ<E, R> {
    return switchMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> ObservableZ<E1, T>.switchMapEither(crossinline mapper: (T) -> ObservableZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return switchMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> ObservableZ<E, T>.flatMapSingleZ(crossinline mapper: (T) -> Single<R>): ObservableZ<E, R> {
    return flatMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> ObservableZ<E1, T>.flatMapSingleEither(crossinline mapper: (T) -> SingleZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return flatMapSingle { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> ObservableZ<E, T>.switchMapSingleZ(crossinline mapper: (T) -> Single<R>): ObservableZ<E, R> {
    return switchMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> ObservableZ<E1, T>.switchMapSingleEither(crossinline mapper: (T) -> SingleZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return switchMapSingle { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> ObservableZ<E, T>.concatMapSingleZ(crossinline mapper: (T) -> Single<R>): ObservableZ<E, R> {
    return concatMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> ObservableZ<E1, T>.concatMapSingleEither(crossinline mapper: (T) -> SingleZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return concatMapSingle { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T, R> ObservableZ<E, T>.scanZ(
    initialValue: R,
    accumulator: BiFunction<R, T, R>
): ObservableZ<E, R> {
    return scan(initialValue.right() as Either<E, R>, { t1, t2 ->
        t2.map { v2 -> accumulator.apply(t1.getOrElse { initialValue }, v2) }
    })
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> ObservableZ<E, T>.fix(toThrowable: (E) -> Throwable): Observable<T> {
    return flatMap {
        it.fold({ Observable.error<T> { toThrowable(it) } }, { Observable.just(it) })
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> ObservableZ<Throwable, T>.fix(): Observable<T> {
    return fix(::identity)
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> Observable<T>.z(toError: (Throwable) -> E): ObservableZ<E, T> {
    return map { it.right() as Either<E, T> }
        .onErrorReturn { toError(it).left() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Observable<T>.z(): ObservableZ<Throwable, T> {
    return z(::identity)
}
