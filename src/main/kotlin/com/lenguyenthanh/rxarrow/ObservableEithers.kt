package com.lenguyenthanh.rxarrow

import arrow.core.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.functions.BiFunction
import io.reactivex.internal.operators.observable.ObservableEmpty

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.mapE(crossinline mapper: (T) -> R): Observable<Either<E, R>> {
    return mapEither { mapper(it).right() }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Observable<Either<E1, T>>.mapEither(crossinline mapper: (T) -> Either<E2, R>): Observable<Either<E, R>>
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
inline fun <E1, E2, T> Observable<Either<E1, T>>.mapLeft(crossinline mapper: (E1) -> E2): Observable<Either<E2, T>> {
    return map { either -> either.mapLeft { mapper(it) } }
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
inline fun <E1, E2, E, T, R> Observable<Either<E1, T>>.flatMapEither(crossinline mapper: (T) -> Observable<Either<E2, R>>): Observable<Either<E, R>>
        where E1 : E, E2 : E {
    return flatMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return concatMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Observable<Either<E1, T>>.concatMapEither(crossinline mapper: (T) -> Observable<Either<E2, R>>): Observable<Either<E, R>>
        where E1 : E, E2 : E {
    return concatMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapE(crossinline mapper: (T) -> Observable<R>): Observable<Either<E, R>> {
    return switchMap { it.flatMapObservable(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Observable<Either<E1, T>>.switchMapEither(crossinline mapper: (T) -> Observable<Either<E2, R>>): Observable<Either<E, R>>
        where E1 : E, E2 : E {
    return switchMap { it.flatMapObservableEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.flatMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return flatMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Observable<Either<E1, T>>.flatMapSingleEither(crossinline mapper: (T) -> Single<Either<E2, R>>): Observable<Either<E, R>>
        where E1 : E, E2 : E {
    return flatMapSingle { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.switchMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return switchMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Observable<Either<E1, T>>.switchMapSingleEither(crossinline mapper: (T) -> Single<Either<E2, R>>): Observable<Either<E, R>>
        where E1 : E, E2 : E {
    return switchMapSingle { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E, T, R> Observable<Either<E, T>>.concatMapSingleE(crossinline mapper: (T) -> Single<R>): Observable<Either<E, R>> {
    return concatMapSingle { it.flatMapSingle(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <E1, E2, E, T, R> Observable<Either<E1, T>>.concatMapSingleEither(crossinline mapper: (T) -> Single<Either<E2, R>>): Observable<Either<E, R>>
        where E1 : E, E2 : E {
    return concatMapSingle { it.flatMapSingleEither(mapper) }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T, R> Observable<Either<E, T>>.scanEither(initialValue: R, accumulator: BiFunction<R,T,R>): Observable<Either<E, R>> {
    return scan(initialValue.right() as Either<E, R>, {t1, t2 ->
        when(t2) {
            is Either.Left -> t2.a.left()
            is Either.Right -> {
                when(t1) {
                    is Either.Left -> accumulator.apply(initialValue, t2.b).right()
                    is Either.Right -> accumulator.apply(t1.b, t2.b).right()
                }
            }
        }
    })
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <E, T, R> Observable<Either<E, T>>.scanWithEither(seed: (T) -> R, accumulator: BiFunction<R,T,R>): Observable<Either<E, R>> {
    return Observable.empty()
}
