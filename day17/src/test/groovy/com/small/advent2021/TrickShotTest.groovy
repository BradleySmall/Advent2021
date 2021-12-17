package com.small.advent2021

import spock.lang.Specification

class TrickShotTest extends Specification {
    def "should"() {
        given:
        TrickShot trickShot = new TrickShot("input.txt")

        expect:
        false
    }
}
