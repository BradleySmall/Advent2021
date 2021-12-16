package com.small.advent2021

import spock.lang.Specification

class PolymerTest extends Specification {
    Polymer polymer
    void setup() {
    }

    def "should show results using map"() {
        given:
        polymer = new Polymer("input_test.txt")

        when:
        polymer.performPassWithMap(1)
        then:
        polymer.getLength() == 7

        when:
        polymer.performPassWithMap(2)
        then:
        polymer.getLength() == 13

        when:
        polymer.performPassWithMap(3)
        then:
        polymer.getLength() == 25

        when:
        polymer.performPassWithMap(4)
        then:
        polymer.getLength() == 49

        when:
        polymer.performPassWithMap(5)
        then:
        polymer.getLength() == 97

        when:
        polymer.performPassWithMap(10)
        then:
        polymer.getLength() == 3073
        polymer.getCount("B") == 1749
        polymer.getCount("C") == 298
        polymer.getCount("H") == 161
        polymer.getCount("N") == 865
        polymer.getMaxCount() == 1749
        polymer.getMinCount() == 161
        polymer.getMaxMinusMin() == 1588

        when:
        polymer.performPassWithMap(40)
        then:
        polymer.getMaxCount() == 2192039569602
        polymer.getMinCount() == 3849876073
        polymer.getMaxMinusMin() == 2188189693529

    }
    def "should show puzzle solution"() {
        given:
        polymer = new Polymer("input.txt")

        when:
        polymer.performPassWithPolymerString(10)
        then:
        polymer.showMostMinusLeastFromPolymerString() == 2194L

        when:
        polymer.performPassWithMap(40)
        then:
        polymer.log.info(polymer.cntMap.toString())
        polymer.getMaxCount() == 3403627694467
        polymer.getMinCount() == 1043328798690
        polymer.getMaxMinusMin() == 2360298895777

    }

    def "should show results of passes"() {
        given:
        polymer = new Polymer("input_test.txt")

        when:
        polymer.performPassWithPolymerString(1)
        then:
        polymer.showPolymer() == "NCNBCHB"

        when:
        polymer.performPassWithPolymerString(2)
        then:
        polymer.showPolymer() == "NBCCNBBBCBHCB"

        when:
        polymer.performPassWithPolymerString(3)
        then:
        polymer.showPolymer() == "NBBBCNCCNBBNBNBBCHBHHBCHB"

        when:
        polymer.performPassWithPolymerString(4)
        then:
        polymer.showPolymer() == "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB"

        when:
        polymer.performPassWithPolymerString(10)
        then:
        polymer.showMostMinusLeastFromPolymerString() == 1588L
    }

    def "should show template"() {
        given:
        polymer = new Polymer("input_test.txt")

        expect:
        polymer.showTemplate() == "NNCB"
        polymer.showMostMinusLeastFromPolymerString() == 1L
    }
}
