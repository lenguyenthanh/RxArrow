package com.lenguyenthanh.rxarrow.examples

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.lenguyenthanh.rxarrow.examples.util.disposedBy
import com.lenguyenthanh.rxarrow.flatMapEither
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


fun main(args: Array<String>) {

    val bag = CompositeDisposable()

    singleFlatMapEitherExample()
        .subscribe { either -> println("$either") }
        .disposedBy(bag)
}

sealed class Error
object SomethingWentWrong : Error()
object DevideByZero : Error()

fun singleFlatMapEitherExample(): Single<Either<Error, Int>> {
    val start: Either<SomethingWentWrong, Int> = 0.right()
    return Single.just(start).flatMapEither { divide(30, it) }
}

fun divide(a: Int, b: Int): Single<Either<DevideByZero, Int>> {
    return if (b == 0) Single.just(DevideByZero.left())
    else Single.just((a / b).right())
}
