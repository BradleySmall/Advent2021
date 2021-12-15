package com.small.advent2021

import spock.lang.Specification

class PolymerTest extends Specification {
    Polymer polymer
    void setup() {
    }

    def "should show new mapping"() {
        given:
        polymer = new Polymer("input_test.txt");

        when:
        polymer.performPassWithMap(15);

        then:
        false
    }
    def "should show puzzle solution"() {
        given:
        polymer = new Polymer("input.txt")

        when:
        polymer.performPass(10)
        then:
        polymer.showMostMinusLeast() == 2194L

    }

    def "should show results of passes"() {
        given:
        polymer = new Polymer("input_test.txt");

        when:
        polymer.performPass(1)
        then:
        polymer.showPolymer() == "NCNBCHB"

        when:
        polymer.performPass(2)
        then:
        polymer.showPolymer() == "NBCCNBBBCBHCB"

        when:
        polymer.performPass(3)
        then:
        polymer.showPolymer() == "NBBBCNCCNBBNBNBBCHBHHBCHB"

        when:
        polymer.performPass(4)
        then:
        polymer.showPolymer() == "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB"

        when:
        polymer.performPass(10)
        then:
        polymer.showMostMinusLeast() == 1588L
    }

    def "should show template"() {
        given:
        polymer = new Polymer("input_test.txt");

        expect:
        polymer.showPolymer() == "NNCB"
        polymer.showMostMinusLeast() == 1L
    }
}
