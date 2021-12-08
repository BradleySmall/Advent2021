package com.small.advent2021

import spock.lang.Specification

class Crabs_Specification extends Specification {
    private Crabs crabs

    void setup() {
        crabs = new Crabs("input_test.txt")
    }

    def "should show min and max fuelUsage"() {
        when:
        crabs.calculateFuelUsage()

        then:
        crabs.getMinFuelUsage() == 168
        crabs.getMaxFuelUsage() == 817
    }
}
