package com.small.advent2021.day4

import spock.lang.Specification

class Bingo_Specification extends Specification{
    def "should satisfy example"() {
        given:
        String fileName = "input_test.txt"
        Bingo bingo = new Bingo(fileName)

        when:
        bingo.run()

        then:
        bingo.getStatus() == Bingo.Status.SUCCESS
    }
}
