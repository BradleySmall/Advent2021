package com.small.advent2021

import spock.lang.Specification

class DiracTest extends Specification {
    def "should play real game" () {
        given:
        Dirac dirac = new Dirac()
        var ret = dirac.playDirac()

        expect:
        ret == 116741133558209
    }
    def "should show die values" () {
        given:
        Dirac dirac = new Dirac()
        expect:
        dirac.die.getNextValue() == 6
        dirac.die.getNextValue() == 15

    }
    def "should show game results" () {
        given:
        Dirac dirac = new Dirac()
        expect:
        dirac.play() == 6

    }
}
