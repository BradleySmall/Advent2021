package com.small.advent2021

import spock.lang.Specification

class ChitonTest extends Specification {
    Chiton chiton

    void setup() {
        chiton = new Chiton("input_test.txt")
    }
    def "should "() {
        given:
        when:
        then:
        expect:
        //chiton.score2StepPaths() == 4854L
        chiton.tryWalk() == 0L
    }
}
