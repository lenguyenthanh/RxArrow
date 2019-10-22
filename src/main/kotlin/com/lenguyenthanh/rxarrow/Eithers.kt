package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

inline fun <E1, E2, E, T, R> Either<E1, T>.flatMapObservableEither(crossinline mapper: (T) -> ObservableZ<E2, R>): ObservableZ<E, R>
        where E1 : E, E2 : E {
    return fold({ Observable.just(it.left()) }, { mapper(it).map { e -> e } })
}

inline fun <E, T, R> Either<E, T>.flatMapObservable(crossinline mapper: (T) -> Observable<R>): ObservableZ<E, R> {
    return flatMapObservableEither { mapper(it).map { r -> r.right() } }
}

inline fun <E1, E2, E, T, R> Either<E1, T>.flatMapSingleEither(crossinline mapper: (T) -> SingleZ<E2, R>): SingleZ<E, R>
        where E1 : E, E2 : E {
    return fold({ Single.just(it.left()) }, { mapper(it).map { either -> either.mapLeft { e2 -> e2 as E } } })
}

inline fun <E, T, R> Either<E, T>.flatMapSingle(crossinline mapper: (T) -> Single<R>): SingleZ<E, R> {
    return flatMapSingleEither { mapper(it).map { r -> r.right() } }
}

inline fun <E1, E2, E, T, R> Either<E1, T>.flatMapMaybeEither(crossinline mapper: (T) -> MaybeZ<E2, R>): MaybeZ<E, R>
        where E1 : E, E2 : E {
    return fold({ Maybe.empty() }, { mapper(it).map { either -> either.mapLeft { e2 -> e2 as E } } })
}

inline fun <E, T, R> Either<E, T>.flatMapMaybe(crossinline mapper: (T) -> Maybe<R>): MaybeZ<E, R> {
    return flatMapMaybeEither { mapper(it).map { r -> r.right() } }
}
