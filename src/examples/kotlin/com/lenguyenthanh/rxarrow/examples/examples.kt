package com.lenguyenthanh.rxarrow.examples

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.lenguyenthanh.rxarrow.*
import com.lenguyenthanh.rxarrow.examples.util.disposedBy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


fun main(args: Array<String>) {

    val bag = CompositeDisposable()

    observableZExample()
        .disposedBy(bag)

    println()
    println()

    singleZExample()
        .disposedBy(bag)
}

fun observableZExample(): Disposable {
    println("======ObservableZ example======")
    val z: ObservableZ<String, Int> = Observable.just("error".left(), 23.right()) // Left(error), Right(24)
    val bag = z.doOnNext { println("$it") }
        .mapZ { it + 2 } // Left("error"), Right(25): ObservableZ<String, Int>
        .doOnNext { println("$it") }
        .mapLeft { it.reversed() } // Left("rorre"), Right(25): ObservableZ<String, Int>
        .doOnNext { println("$it") }
        .mapLeft { SomethingWentWrong } // Left(SomethingWentWrong), Right(25): ObservableZ<SomethingWentWrong, Int>
        .doOnNext { println("$it") }
        .flatMapZ { Observable.just(it * it) } // Left(SomethingWentWrong), Right(625: ObservableZ<SomethingWentWrong, Int>)
        .doOnNext { println("$it") }
        .flatMapSingleEither { divide(it, 25) } // Left(SomethingWentWrong), Left(25: ObservableZ<SomethingWentWrong, Int>)
        .doOnNext { println("$it") }
        .flatMapSingleEither { divide(it, 0) } // Left(SomethingWentWrong), Left(DevidedByZero: ObservableZ<Error, Int>)
        .subscribe { either -> println("$either") }

    return bag
}

fun singleZExample(): Disposable {
    println("======singleZ example======")
    val start: Either<SomethingWentWrong, Int> = 0.right()
    return Single.just(start).flatMapEither { divide(30, it) }
        .subscribe { either -> println("$either") }
}

sealed class Error {
    override fun toString(): String {
        return when(this) {
            is SomethingWentWrong -> "SomethingWentWrong"
            is DividedByZero -> "DividedByZero"
        }
    }
}

object SomethingWentWrong : Error()
object DividedByZero : Error()

fun divide(a: Int, b: Int): SingleZ<DividedByZero, Int> {
    return if (b == 0) Single.just(DividedByZero.left())
    else Single.just((a / b).right())
}
