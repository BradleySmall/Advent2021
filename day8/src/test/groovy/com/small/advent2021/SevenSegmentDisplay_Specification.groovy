package com.small.advent2021

import spock.lang.Specification

class SevenSegmentDisplay_Specification extends Specification {
    def "should show correct total according to test data" () {
        when:
        SevenSegmentDisplay sevenSegmentDisplay = new SevenSegmentDisplay("input_test.txt")

        then:
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        sevenSegmentDisplay.decode("be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb",
                "fdgacbe cefdb cefbgd gcbe") == "8394"
        sevenSegmentDisplay.total() == 61229
    }

    def "should prove decode values according to example"() {
        when:
        SevenSegmentDisplay sevenSegmentDisplay = new SevenSegmentDisplay("input_test.txt")

        then:
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        //noinspection SpellCheckingInspection
        sevenSegmentDisplay.decode("be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb",
                "fdgacbe cefdb cefbgd gcbe") == "8394"
    }
}
