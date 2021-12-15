package com.small.advent2021

import spock.lang.Specification

class PassagePathingTest extends Specification {
    PassagePathing passagePathing

    def "files should show number of paths" () {
        when:
        passagePathing = new PassagePathing("input_test.txt")
        then:
        passagePathing.getCount() == 36

        when:
        passagePathing = new PassagePathing("input_test_2.txt")
        then:
        passagePathing.getCount() == 103

        when:
        passagePathing = new PassagePathing("input_test_3.txt")
        then:
        passagePathing.getCount() == 3509

        when:
        passagePathing = new PassagePathing("input.txt")
        then:
        passagePathing.getCount() == 114189


    }

    def "should show file"() {
        given:
        passagePathing = new PassagePathing("input_test.txt")
        expect:
        passagePathing.show() == "\n" +
                "[" +
                "(start,A)" +
                "(start,b)" +
                "(A,c)" +
                "(A,b)" +
                "(b,d)" +
                "(A,end)" +
                "(b,end)]"
    }
    def "should show distinct rooms" () {
        given:
        passagePathing = new PassagePathing("input_test.txt")
        expect:
        passagePathing.showDistinctRooms() == "[A, b, c, d, start, end]"
    }
    def "should show legs" () {
        given:
        passagePathing = new PassagePathing("input_test.txt")
        expect:
        passagePathing.legs
                .toString()
                .replace("\n"," ") ==
                "[" +
                "[(A,c), (A,b), (A,end)], " +
                "[(b,A), (b,d), (b,end)], " +
                "[(c,A)], " +
                "[(d,b)], " +
                "[(start,A), (start,b)]" +
                "]"
    }
    def "should show starts list"() {
        given:
        passagePathing = new PassagePathing("input_test.txt")
        expect:
        passagePathing.show(passagePathing.getLeg("start")) == "\n[(start,A)(start,b)]"
        passagePathing.show(passagePathing.getLeg("A")) == "\n[(A,c)(A,b)(A,end)]"
        passagePathing.show(passagePathing.getLeg("b")) == "\n[(b,A)(b,d)(b,end)]"
        passagePathing.show(passagePathing.getLeg("c")) == "\n[(c,A)]"
        passagePathing.show(passagePathing.getLeg("d")) == "\n[(d,b)]"
    }
}
