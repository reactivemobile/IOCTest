package test

import annotations.Inject
import processor.inject
import test.vehicles.Bicycle
import test.vehicles.Car

class MyVehicles {

    init {
        inject(this)
    }

    @Inject
    lateinit var myBicycle: Bicycle

    @Inject
    lateinit var myOtherBicycle: Bicycle

    @Inject
    lateinit var myCar: Car

    @Inject
    lateinit var myOtherCar: Car

    fun test() {
        myBicycle.testRide()

        myOtherBicycle.testRide()

        myCar.testDrive()

        myOtherCar.testDrive()
    }
}

