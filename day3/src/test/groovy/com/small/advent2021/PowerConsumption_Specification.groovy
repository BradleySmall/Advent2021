package com.small.advent2021

import spock.lang.Specification

class PowerConsumption_Specification extends Specification {
    PowerConsumption powerConsumption

    def setup() {
        powerConsumption = new PowerConsumption("input2.txt")
    }

    def "should calculate gama rate"() {
        expect:
        powerConsumption.getGamaRate() == 22
    }

    def "should calculate epsilon rate" () {
        expect:
        powerConsumption.getEpsilonRate() == 9
    }

    def "should calculate consumption" () {
        expect:
        powerConsumption.getConsumption() == 198
    }

    def "GetO2GeneratorRating"() {
        expect:
        powerConsumption.getO2GeneratorRating() == 23
    }

    def "GetCo2ScrubberRating"() {
        expect:
        powerConsumption.getCo2ScrubberRating() == 10
    }

    def "GetLifeSupportRating"() {
        expect:
        powerConsumption.getLifeSupportRating() == 230
    }
}
