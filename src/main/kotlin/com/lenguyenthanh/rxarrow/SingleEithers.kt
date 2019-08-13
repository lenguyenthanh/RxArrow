package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.right
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport

inline fun <E, T, R> Single<Either<E, T>>.mapEither(crossinline mapper: (T) -> Either<E, R>): Single<Either<E, R>> {
    return map { either ->
        when (either) {
            is Either.Right -> mapper(either.b)
            is Either.Left -> either
        }
    }
}

inline fun <E, T, R> Single<Either<E, T>>.map(crossinline mapper: (T) -> R): Single<Either<E, R>> {
    return mapEither { mapper(it).right() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Single<Either<E, T>>.flatMapE(crossinline mapper: (T) -> Single<R>): Single<Either<E, R>> {
    return flatMap { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Single<Either<E, T>>.flatMapEither(crossinline mapper: (T) -> Single<Either<E, R>>): Single<Either<E, R>> {
    return flatMap { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Single<Either<E, T>>.flatMapObservableE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return flatMapObservable{ it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Single<Either<E, T>>.flatMapObservableEither(crossinline mapper: (T) -> Observable<Either<E,R>>): Observable<Either<E, R>> {
    return flatMapObservable{ it.flatMapObservableEither(mapper) }
}