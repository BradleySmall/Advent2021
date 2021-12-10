package com.small.advent2021

import spock.lang.Specification

class SyntaxScoringTest extends Specification {
    SyntaxScoring syntaxScoring
    void setup() {
        syntaxScoring = new SyntaxScoring("input_test.txt")
    }

    def "should test all"() {
        expect:
        26397 == syntaxScoring.getScore()
    }
    def "should test for me"() {
        expect:
        1 ==  syntaxScoring.scoreLine("{}}")
    }
    def "should test line"() {
        when:

        String line = syntaxScoring.showGetLine(2)
        int score = syntaxScoring.scoreLine(line)

        then:
        0 == score
    }
    def "should have good line"() {
        expect:
        "[({(<(())[]>[[{[]{<()<>>" == syntaxScoring.showGetLine(0)

    }

    def "should create object"() {

        expect:
        syntaxScoring.class.toString() == "class com.small.advent2021.SyntaxScoring"
    }
}
