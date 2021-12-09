package com.small.advent2001

import spock.lang.Specification

class SmokeBasinTest extends Specification {
    SmokeBasin smokeBasin

    void setup() {
        smokeBasin = new SmokeBasin("input_test.txt")
    }

    def "should show pretty matrix"() {
        expect:
        "\n" +
                " [2, 1, 9, 9, 9, 4, 3, 2, 1, 0]\n" +
                " [3, 9, 8, 7, 8, 9, 4, 9, 2, 1]\n" +
                " [9, 8, 5, 6, 7, 8, 9, 8, 9, 2]\n" +
                " [8, 7, 6, 7, 8, 9, 6, 7, 8, 9]\n" +
                " [9, 8, 9, 9, 9, 6, 5, 6, 7, 8]\n"
         == smokeBasin.prettyPrintMatrix(smokeBasin.matrix)

    }
    def "should count low spots" () {
        expect:
        4 == smokeBasin.countLowPoints()
        15 == smokeBasin.getRiskLevel()
        1134L == smokeBasin.getBasinListAssessment()
    }
}
