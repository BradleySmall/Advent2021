package com.small.advent2021

import spock.lang.Specification

class SyntaxScoringTest extends Specification {
    SyntaxScoring syntaxScoring
    void setup() {
        syntaxScoring = new SyntaxScoring("input_test.txt")
    }

    def "should show middle score"() {
        expect:
        5566L == syntaxScoring.getTotalScore()
    }
    def "should get incomplete score"() {
        expect:
        288957L == syntaxScoring.getIncompleteScore()
    }
    def "should get corrupt score"() {
        expect:
        26397L == syntaxScoring.getCorruptScore()
    }
    def "should test incomplete line"() {
        when:

        String line = syntaxScoring.showGetLine(0)
        long score = syntaxScoring.scoreIncompleteLine(line)

        then:
        288957L == score
    }
    def "should test corrupt line"() {
        when:

        String line = syntaxScoring.showGetLine(2)
        long score = syntaxScoring.scoreCorruptLine(line)

        then:
        1197L == score
    }
}
