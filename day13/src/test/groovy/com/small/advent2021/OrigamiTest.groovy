package com.small.advent2021

import spock.lang.Specification

class OrigamiTest extends Specification {
    Origami origami

    def "should get results"() {
        given:
        origami = new Origami("input.txt")

        when:
        origami.performXFold(655)
        origami.performYFold(447)
        origami.performXFold(327)
        origami.performYFold(223)
        origami.performXFold(163)
        origami.performYFold(111)
        origami.performXFold(81)
        origami.performYFold(55)
        origami.performXFold(40)
        origami.performYFold(27)
        origami.performYFold(13)
        origami.performYFold(6)
        then:
        origami.getPairsCount() == 98

    }
    def "should show results of first puzzle fold"() {
        given:
        origami = new Origami("input.txt")

        when:
        origami.performXFold(655)
        then:
        origami.getPairsCount() == 785
    }

    def "should show results of two folds"() {
        given:
        origami = new Origami("input_test.txt")

        when:
        origami.performYFold(7)
        then:
        origami.getPairsCount() == 17

        when:
        origami.performXFold(5)
        then:
        origami.getPairsCount() == 16

    }

    def "should show foldline translation"() {
        when:
        origami = new Origami("input_test.txt")

        then:
        origami.yFoldTranslation(5, new Origami.Pair(3,6)).toString() == "3,4"
        origami.xFoldTranslation(5, new Origami.Pair(9,6)).toString() == "1,6"
        origami.yFoldTranslation(5, new Origami.Pair(3,4)).toString() == "3,4"
        origami.xFoldTranslation(5, new Origami.Pair(1,6)).toString() == "1,6"
    }

    def "should read file"() {
        when:
        origami = new Origami("input_test.txt")

        then:
        origami.getDotsSize() == 18
        origami.getCmdsSize() == 2

        when:
        origami = new Origami("input.txt")

        then:
        origami.getDotsSize() == 950
        origami.getCmdsSize() == 12
    }

    def "should show paper dimensions "() {
        when:
        origami = new Origami("input_test.txt")

        then:
        origami.getMinXY().toString() == "0,0"
        origami.getMaxXY().toString() == "10,14"
    }
}
