package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object EithersSpec : Spek({

    describe("flatMapObservableEither") {

        lateinit var testObservable: TestObserver<Either<Failure, String>>
        fun int2String(i: Int): Observable<Either<Failure.ABadFailure, String>> {
            return if (i == 0) Observable.just(Failure.ABadFailure.left())
            else Observable.just("Success".right())
        }

        describe("right to right") {
            val right: Either<Failure.FailureWithReason, Int> = 1.right()
            beforeEachTest {
                testObservable = right.flatMapObservableEither { int2String(it) }.test()
            }
            it("should return a right") {
                testObservable.assertComplete()
                    .assertValueCount(1)
                    .assertValue("Success".right())
            }
        }

        describe("right to left") {
            val right: Either<Failure.FailureWithReason, Int> = 0.right()

            beforeEachTest {
                testObservable = right.flatMapObservableEither { int2String(it) }.test()
            }
            it("should return a left") {
                testObservable.assertComplete()
                    .assertValueCount(1)
                    .assertValue(Failure.ABadFailure.left())
            }
        }

        describe("left to any thing") {
            val left: Either<Failure.FailureWithReason, Int> = Failure.FailureWithReason("blabla").left()

            beforeEachTest {
                testObservable = left.flatMapObservableEither { int2String(it) }.test()
            }

            it("should return a left") {
                testObservable.assertComplete()
                    .assertValueCount(1)
                    .assertValue(left as Either<Failure, String>)
            }
        }
    }

    describe("flatMapObservable") {
        lateinit var testObservable: TestObserver<Either<Failure, String>>
        fun int2String(i: Int): Observable<String> {
            return Observable.just("This is $i")
        }

        describe("right") {
            val right: Either<Failure, Int> = 0.right()

            beforeEachTest {
                testObservable = right.flatMapObservable { int2String(it) }.test()
            }

            it("should return a left") {
                testObservable.assertComplete()
                    .assertValueCount(1)
                    .assertValue("This is 0".right())
            }
        }

        describe("left") {
            val right: Either<Failure, Int> = Failure.ABadFailure.left()

            beforeEachTest {
                testObservable = right.flatMapObservable { int2String(it) }.test()
            }

            it("should return a left") {
                testObservable.assertComplete()
                    .assertValueCount(1)
                    .assertValue(Failure.ABadFailure.left())
            }
        }
    }
})
