# RxArrow [![](https://jitpack.io/v/lenguyenthanh/RxArrow.svg)](https://jitpack.io/#lenguyenthanh/RxArrow)

RxArrow is a collection of RxJava extensions and typealiases [Arrow's Data Types](https://arrow-kt.io).

## Table of Contents

+ [Why Z](#z)
+ [Usage](#usage)
+ [Setup](#setup)
+ [Compatibility](#compatibility)
+ [Contributing](#contributing)
+ [License](/LICENSE)


## Why `Z` <a name = "z"></a>

I use `Z` as a postfix for all typealiases because I was influenced by [ZIO](https://zio.dev).

## Usage <a name = "usage"></a>

### [Observable<Either<E, A>> aka ObservableZ<E, A>](https://github.com/lenguyenthanh/RxArrow/blob/master/src/main/kotlin/com/lenguyenthanh/rxarrow/ObservableZ.kt)

`ObservableZ<E, A>` is a type alias for `Observable<Either<E, A>>`. It has a convenient extension functions to make life easier when working with `Observable<Either<E, A>>`.

```Kotlin

Observable.just("error".left(), 23.right()) // Left(error), Right(24): ObservableZ<String, Int>
    .mapZ { it + 2 } // Left("error"), Right(25): ObservableZ<String, Int>
    .mapLeft { it.reversed() } // Left("rorre"), Right(25): ObservableZ<String, Int>
    .mapLeft { SomethingWentWrong } // Left(SomethingWentWrong), Right(25): ObservableZ<SomethingWentWrong, Int>
    .flatMapZ { Observable.just(it * it) } // Left(SomethingWentWrong), Right(625): ObservableZ<SomethingWentWrong, Int>
    .flatMapSingleEither { divide(it, 0) } // Left(SomethingWentWrong), Left(DevidedByZero): ObservableZ<Error, Int>
    .subscribe { either -> println("$either") }
```

### [Single<Either<E, A>> aka SingleZ<E, A>](https://github.com/lenguyenthanh/RxArrow/blob/master/src/main/kotlin/com/lenguyenthanh/rxarrow/SingleZ.kt)

Similar to `ObservableZ`

### [Maybe<Either<E, A>> aka MaybeZ<E, A>](https://github.com/lenguyenthanh/RxArrow/blob/master/src/main/kotlin/com/lenguyenthanh/rxarrow/MaybeZ.kt)

Similar to `ObservableZ`

## Setup <a name = "setup"></a>

You can get `RxArrow` by using [Jitpack](https://jitpack.io/#lenguyenthanh/RxArrow/).

```Gradle
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }

    implementation "io.arrow-kt:arrow-core:$arrow_version"
    implementation "com.github.lenguyenthanh:RxArrow:$rxarrow"
```

## Compatibility <a name = "compatibility"></a>

Supports RxJava2 and Arrow version `0.10.1`.

## Contributing <a name = "contributing"></a>

Any bug reports, feature requests, questions and pull requests are very welcome.
