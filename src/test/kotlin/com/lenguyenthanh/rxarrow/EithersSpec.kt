package com.lenguyenthanh.rxarrow

import arrow.core.Either
import arrow.core.right
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe

object EithersSpec : Spek({
   describe("An Either") {
       val right: Either<String, Int> by memoized { 1.right() }
       describe("fatMapObservableE") {

       }
   }
})
