package test

import annotations.Inject
import processor.inject
import test.vehicles.Bicycle

class MyOtherVehicles {

    init {
        inject(this)
    }

    @Inject
    lateinit var myBicycle: Bicycle

    @Inject
    lateinit var myOtherBicycle: Bicycle

    fun test() {
        myBicycle.testRide()

        myOtherBicycle.testRide()
    }
}

