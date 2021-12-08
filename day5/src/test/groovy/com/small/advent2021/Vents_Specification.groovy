package com.small.advent2021
import spock.lang.Specification

class Vents_Specification extends Specification {
    private Vents vents

    def "should load line segments"() {
        expect:
        Arrays.deepToString(vents.lineList.toArray()) == "[[0, 9, 5, 9], [8, 0, 0, 8], [9, 4, 3, 4], [2, 2, 2, 1], [7, 0, 7, 4], [6, 4, 2, 0], [0, 9, 2, 9], [3, 4, 1, 4], [0, 0, 8, 8], [5, 5, 8, 2]]"
    }
    def "should show counts of vertical and horizontal"() {
        expect:
        vents.countHorizontal() == 2
        vents.countVertical() == 4
        vents.countOverlappingPoints() == 12
    }
    void setup() {
        vents = new Vents("input_test.txt")
    }
}
