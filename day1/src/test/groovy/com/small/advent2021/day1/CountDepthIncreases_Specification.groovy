package com.small.advent2021.day1

import spock.lang.Specification

class CountDepthIncreases_Specification extends Specification {
    def "should count increases and show 1"() {
        given:
        CountIncreases ci = new CountIncreases()
        List list = List.of(1, 2, 2)

        when:
        def count = ci.count(list)

        then:
        count == 1
    }

    def "should count increases and show 0"() {
        given:
        CountIncreases ci = new CountIncreases()
        List list = List.of(2, 2, 2)

        when:
        def count = ci.count(list)

        then:
        count == 0
    }

    def "should open a file"() {
        given:
        File file = new File("input.txt")

        when:
        def canRead = file.canRead()

        then:
        canRead
    }

    def "should create a list from a file"() {
        given:
        CountIncreases ci = new CountIncreases()

        when:
        List<Integer> list = ci.getListFromFile("input.txt")

        then:
        list.size() > 0
    }

    def "should create a sliding sum list from a file"() {
        given:
        CountIncreases ci = new CountIncreases()

        when:
        List<Integer> l = ci.getListFromFile("input.txt")
        List<Integer> list = ci.slideCountList(l, 3)

        then:
        list.size() > 0
    }

    def "should count the increases in the file"() {
        given:
        CountIncreases ci = new CountIncreases()

        when:
        List<Integer> list = ci.getListFromFile("input.txt")
        def count = ci.count(list)

        then:
        count == 1451
    }
}
