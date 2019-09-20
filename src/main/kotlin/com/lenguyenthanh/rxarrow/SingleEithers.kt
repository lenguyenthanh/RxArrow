package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Single<Either<E1, T>>.mapEither(crossinline mapper: (T) -> Either<E2, R>): Single<Either<E, R>>
        where E1 : E, E2 : E {
    return map { either ->
        when (either) {
            is Either.Right -> mapper(either.b)
            is Either.Left -> either
        }
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Single<Either<E, T>>.mapE(crossinline mapper: (T) -> R): Single<Either<E, R>> {
    return mapEither { mapper(it).right() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, T> Single<Either<E1, T>>.mapLeft(crossinline mapper: (E1) -> E2): Single<Either<E2, T>> {
    return map { either -> either.mapLeft { mapper(it) } }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Single<Either<E, T>>.flatMapE(crossinline mapper: (T) -> Single<R>): Single<Either<E, R>> {
    return flatMap { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Single<Either<E1, T>>.flatMapEither(crossinline mapper: (T) -> Single<Either<E2, R>>): Single<Either<E, R>>
        where E1 : E, E2 : E {
    return flatMap { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Single<Either<E, T>>.flatMapObservableE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return flatMapObservable { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Single<Either<E1, T>>.flatMapObservableEither(crossinline mapper: (T) -> Observable<Either<E2, R>>): Observable<Either<E, R>>
        where E1 : E, E2 : E {
    return flatMapObservable { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> Single<Either<E, T>>.fix(toThrowable: (E) -> Throwable): Single<T> {
    return flatMap {
        when (it) {
            is Either.Right -> Single.just(it.b)
            is Either.Left -> Single.error(toThrowable(it.a))
        }
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T> Single<T>.either(toError: (Throwable) -> E): Single<Either<E, T>> {
    return this.map { it.right() as Either<E, T> }
        .onErrorReturn { toError(it).left() }
}