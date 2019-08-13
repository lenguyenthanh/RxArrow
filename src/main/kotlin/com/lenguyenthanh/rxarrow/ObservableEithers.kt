package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.right
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.mapE(crossinline mapper: (T) -> R): Observable<Either<E, R>> {
    return mapEither { mapper(it).right() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.mapEither(crossinline mapper: (T) -> Either<E, R>): Observable<Either<E, R>> {
    return map { either ->
        when (either) {
            is Either.Right -> mapper(either.b)
            is Either.Left -> either
        }
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T> Observable<Either<E, T>>.filterE(crossinline filter: (T) -> Boolean): Observable<Either<E, T>> {
    return filter { either ->
        when (either) {
            is Either.Right -> filter(either.b)
            is Either.Left -> true
        }
    }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.flatMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return flatMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.flatMapEither(crossinline mapper: (T) -> Observable<Either<E, R>>): Observable<Either<E, R>> {
    return flatMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return concatMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapEither(crossinline mapper: (T) -> Observable<Either<E, R>>): Observable<Either<E, R>> {
    return concatMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return switchMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapEither(crossinline mapper: (T) -> Observable<Either<E, R>>): Observable<Either<E, R>> {
    return switchMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.flatMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return flatMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.flatMapSingleEither(crossinline mapper: (T) -> Single<Either<E, R>>): Observable<Either<E, R>> {
    return flatMapSingle { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return switchMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapSingleEither(crossinline mapper: (T) -> Single<Either<E, R>>): Observable<Either<E, R>> {
    return switchMapSingle { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return concatMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapSingleEither(crossinline mapper: (T) -> Single<Either<E, R>>): Observable<Either<E, R>> {
    return concatMapSingle { it.flatMapSingleEither(mapper) }
}
