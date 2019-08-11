package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.right
import io.reactivex.Observable
import io.reactivex.Single


inline fun <E, T, R> Either<E, T>.flatMapObservable(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return when (this) {
        is Either.Right -> mapper(b).map { it.right() }
        is Either.Left -> Observable.just(this)
    }
}

inline fun <E, T, R> Either<E, T>.flatMapSingle(crossinline mapper: (T) -> Single<R>): Single<Either<E, R>> {
    return when (this) {
        is Either.Right -> mapper(b).map { it.right() }
        is Either.Left -> Single.just(this)
    }
}
