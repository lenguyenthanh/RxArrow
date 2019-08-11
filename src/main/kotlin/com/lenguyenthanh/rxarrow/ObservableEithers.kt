package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.right
import io.reactivex.Observable
import io.reactivex.Single

inline fun <E, T, R> Observable<Either<E, T>>.mapE(crossinline mapper: (T) -> R): Observable<Either<E, R>> {
    return this.map { either ->
        when (either) {
            is Either.Right -> mapper(either.b).right()
            is Either.Left -> either
        }
    }
}

inline fun <E, T> Observable<Either<E, T>>.filterE(crossinline filter: (T) -> Boolean): Observable<Either<E, T>> {
    return filter { either ->
        when (either) {
            is Either.Right -> filter(either.b)
            is Either.Left -> true
        }
    }
}

inline fun <E, T, R> Observable<Either<E, T>>.flatMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return flatMap { it.flatMapObservable(mapper) }
}

inline fun <E, T, R> Observable<Either<E, T>>.concatMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return concatMap { it.flatMapObservable(mapper) }
}

inline fun <E, T, R> Observable<Either<E, T>>.switchMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return switchMap { it.flatMapObservable(mapper) }
}

inline fun <E, T, R> Observable<Either<E, T>>.flatMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return flatMapSingle { it.flatMapSingle(mapper) }
}

inline fun <E, T, R> Observable<Either<E, T>>.switchMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return switchMapSingle { it.flatMapSingle(mapper) }
}

inline fun <E, T, R> Observable<Either<E, T>>.concatMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return concatMapSingle { it.flatMapSingle(mapper) }
}
