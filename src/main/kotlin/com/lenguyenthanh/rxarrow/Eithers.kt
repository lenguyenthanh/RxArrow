package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.right
import io.reactivex.Observable
import io.reactivex.Single

inline fun <E, T, R> Either<E, T>.flatMapObservableEither(crossinline mapper: (T) -> Observable<Either<E, R>>): Observable<Either<E, R>> {
    return when (this) {
        is Either.Right -> mapper(b)
        is Either.Left -> Observable.just(this)
    }
}

inline fun <E, T, R> Either<E, T>.flatMapObservable(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return flatMapObservableEither { mapper(it).map { it.right() as Either<E, R>} } // don't remove the stupid cast otherwise compiler won't compile.
}

inline fun <E, T, R> Either<E, T>.flatMapSingleEither(crossinline mapper: (T) -> Single<Either<E, R>>): Single<Either<E, R>> {
    return when (this) {
        is Either.Right -> mapper(b)
        is Either.Left -> Single.just(this)
    }
}

inline fun <E, T, R> Either<E, T>.flatMapSingle(crossinline mapper: (T) -> Single<R>): Single<Either<E, R>> {
    return flatMapSingleEither { mapper(it).map { it.right() as Either<E, R>} } // don't remove the stupid cast otherwise compiler won't compile.
}

