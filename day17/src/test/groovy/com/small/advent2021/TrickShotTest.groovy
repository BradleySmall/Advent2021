package com.small.advent2021

import spock.lang.Specification

class TrickShotTest extends Specification {

    def "should return apogee for 79"() {
        given:
        TrickShot trickShot = new TrickShot("input.txt")

        expect:
        3160 == trickShot.vert(79, -45, -80)

    }
    def "should"() {
        given:
        TrickShot trickShot = new TrickShot("input.txt")

        expect:
        7 == trickShot.findH(282, 314)
        1928 == trickShot.countValidStarts(282, 314, -45, -80)
    }
}
