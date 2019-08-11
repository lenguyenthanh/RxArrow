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
    return map { either ->
        when (either) {
            is Either.Right -> mapper(either.b).right()
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
inline fun <E, T, R> Observable<Either<E, T>>.flatMapEE(crossinline mapper: (T) -> Observable<Either<E, R>>): Observable<Either<E, R>> {
    return flatMap { it.flatMapObservableE(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return concatMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapEE(crossinline mapper: (T) -> Observable<Either<E, R>>): Observable<Either<E, R>> {
    return concatMap { it.flatMapObservableE(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return switchMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapEE(crossinline mapper: (T) -> Observable<Either<E, R>>): Observable<Either<E, R>> {
    return switchMap { it.flatMapObservableE(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.flatMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return flatMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.flatMapSingleEE(crossinline mapper: (T) -> Single<Either<E, R>>): Observable<Either<E, R>> {
    return flatMapSingle { it.flatMapSingleE(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return switchMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapSingleEE(crossinline mapper: (T) -> Single<Either<E, R>>): Observable<Either<E, R>> {
    return switchMapSingle { it.flatMapSingleE(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return concatMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapSingleEE(crossinline mapper: (T) -> Single<Either<E, R>>): Observable<Either<E, R>> {
    return concatMapSingle { it.flatMapSingleE(mapper) }
}
