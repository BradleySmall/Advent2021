package com.small.advent2021

import spock.lang.Specification

class OctopusTest extends Specification {
    Octopus octopus;

    def "should begin"() {
        expect:
        octopus.show() == "\n[5, 4, 8, 3, 1, 4, 3, 2, 2, 3]\n" +
                "[2, 7, 4, 5, 8, 5, 4, 7, 1, 1]\n" +
                "5, 2, 6, 4, 5, 5, 6, 1, 7, 3]\n" +
                "6, 1, 4, 1, 3, 3, 6, 1, 4, 6]\n" +
                "6, 3, 5, 7, 3, 8, 5, 4, 7, 8]\n" +
                "4, 1, 6, 7, 5, 2, 4, 6, 4, 5]\n" +
                "2, 1, 7, 6, 8, 4, 1, 7, 2, 1]\n" +
                "6, 8, 8, 2, 8, 8, 1, 1, 3, 4]\n" +
                "4, 8, 4, 6, 8, 4, 8, 5, 5, 4]\n" +
                "5, 2, 8, 3, 7, 5, 1, 5, 2, 6]\n"
    }
def "should pass1"() {
    when:
    octopus.passStep();

    then:
    octopus.show() == "\n[5, 4, 8, 3, 1, 4, 3, 2, 2, 3]\n" +
            "[2, 7, 4, 5, 8, 5, 4, 7, 1, 1]\n" +
            "5, 2, 6, 4, 5, 5, 6, 1, 7, 3]\n" +
            "6, 1, 4, 1, 3, 3, 6, 1, 4, 6]\n" +
            "6, 3, 5, 7, 3, 8, 5, 4, 7, 8]\n" +
            "4, 1, 6, 7, 5, 2, 4, 6, 4, 5]\n" +
            "2, 1, 7, 6, 8, 4, 1, 7, 2, 1]\n" +
            "6, 8, 8, 2, 8, 8, 1, 1, 3, 4]\n" +
            "4, 8, 4, 6, 8, 4, 8, 5, 5, 4]\n" +
            "5, 2, 8, 3, 7, 5, 1, 5, 2, 6]\n"
}
    void setup() {
        octopus = new Octopus("input_test.txt");
    }
}
