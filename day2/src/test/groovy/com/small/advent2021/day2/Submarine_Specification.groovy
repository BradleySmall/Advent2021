package com.small.advent2021.day2

import spock.lang.Specification

class Submarine_Specification extends Specification {
    Submarine sub

    def "should forward"() {
        given:
        def oldHorizontalPosition = sub.getHorizontalPosition()
        when:
        sub.forward(5)

        then:
        oldHorizontalPosition + 5 == sub.getHorizontalPosition()

    }

    def "should down doesn't change depth"() {
        given:
        def oldVert = sub.getDepth()
        when:
        sub.down(5)

        then:
        oldVert == sub.getDepth()

    }

    def "should up doesn't change depth"() {
        given:
        def oldVert = sub.getDepth()
        when:
        sub.up(5)

        then:
        oldVert == sub.getDepth()

    }

    def "should show some 60, 15, 900"() {
        when:
        sub.forward(5)
        sub.down(5)
        sub.forward(8)
        sub.up(3)
        sub.down(8)
        sub.forward(2)

        then:
        sub.getDepth() == 60
        sub.getHorizontalPosition() == 15
        sub.getPos() == 900

    }

    void setup() {
        sub = new Submarine()

    }

    def "should get list of commands"() {
        when:
        List<Submarine.Command> list = sub.getListFromFile("input.txt")
        then:
        list.size() != 0
    }

    def "should get position 21"() {
        when:
        sub.horizontalPosition = 3
        sub.depth = 7
        then:
        sub.getPos() == 21
    }

    def "should get value for first 4 items in list"() {
        given:
        when:
        List<Submarine.Command> list = sub.getListFromFile("input.txt")
        sub.processList(list, 4)
        then:
        sub.getDepth() == 54
        sub.getHorizontalPosition() == 10
        sub.getPos() == 540
    }
}
