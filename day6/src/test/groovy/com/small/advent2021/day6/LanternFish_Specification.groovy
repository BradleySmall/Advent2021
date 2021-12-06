package com.small.advent2021.day6

import spock.lang.Specification

class LanternFish_Specification extends Specification {
    LanternFish lanternFish

    void setup() {
        lanternFish = new LanternFish("input_test.txt")
    }

    def "should read list"() {
        expect:
        lanternFish.countFish() == 5
    }

    def "advance day should show correct test values" () {
        when:
        lanternFish.dayTick(1)

        then:
        lanternFish.countFish() == 5
    }
    def "advance 2 days should show correct test values" () {
        when:
        lanternFish.dayTick(2)

        then:
        lanternFish.countFish() == 6
    }

    def "advance 18 days should show correct test values" () {
        when:
        lanternFish.dayTick(18)

        then:
        lanternFish.countFish() == 26
    }


}
