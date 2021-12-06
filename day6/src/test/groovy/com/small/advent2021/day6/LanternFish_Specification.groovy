package com.small.advent2021.day6

import spock.lang.Specification

class LanternFish_Specification extends Specification {
    LanternFish lanternFish

    void setup() {
        lanternFish = new LanternFish("input_test.txt")
    }

    def "should read list"() {
        expect:
        lanternFish.fishList == [3, 4, 3, 1, 2]
    }

    def "advance day should show correct test values" () {
        when:
        lanternFish.dayTick(1)

        then:
        lanternFish.fishList == [2, 3, 2, 0, 1]
        lanternFish.countFish() == 5
    }
    def "advance 2 days should show correct test values" () {
        when:
        lanternFish.dayTick(2)

        then:
        lanternFish.fishList == [1, 2, 1, 6, 0, 8]
        lanternFish.countFish() == 6
    }

    def "advance 18 days should show correct test values" () {
        when:
        lanternFish.dayTick(18)

        then:
        lanternFish.fishList == [6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8]
        lanternFish.countFish() == 26
    }


}
