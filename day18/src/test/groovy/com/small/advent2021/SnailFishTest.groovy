package com.small.advent2021

import spock.lang.Specification


class SnailFishTest extends Specification {
    private SnailFish snailFish

    def setup() {
        snailFish = new SnailFish()
    }

    def "should add list example 3"() {
        given:
        var theList = List.of(
                new SnailFish.Number("[1,1]"),
                new SnailFish.Number("[2,2]"),
                new SnailFish.Number("[3,3]"),
                new SnailFish.Number("[4,4]"),
                new SnailFish.Number("[5,5]"),
                new SnailFish.Number("[6,6]")
        )
        SnailFish.Number n1 = new SnailFish.Number("").addList(theList)
        SnailFish.Number n2 = new SnailFish.Number("[[[[5,0],[7,4]],[5,5]],[6,6]]")

        n1 = n1.reduce()
        n1 = n1.reduce()
        expect:
        n2 == n1

    }
    def "should add list example 2"() {
        given:
        var theList = List.of(
                new SnailFish.Number("[1,1]"),
                new SnailFish.Number("[2,2]"),
                new SnailFish.Number("[3,3]"),
                new SnailFish.Number("[4,4]"),
                new SnailFish.Number("[5,5]")
        )
        SnailFish.Number n1 = new SnailFish.Number("").addList(theList)
        SnailFish.Number n2 = new SnailFish.Number("[[[[3,0],[5,3]],[4,4]],[5,5]]")

        n1 = n1.reduce().reduce()
        expect:
        n2 == n1

    }
    def "should add list"() {
        given:
        var theList = List.of(
                new SnailFish.Number("[1,1]"),
                new SnailFish.Number("[2,2]"),
                new SnailFish.Number("[3,3]"),
                new SnailFish.Number("[4,4]"))
        SnailFish.Number n1 = new SnailFish.Number("").addList(theList)
        SnailFish.Number n2 = new SnailFish.Number("[[[[1,1],[2,2]],[3,3]],[4,4]]")

        expect:
        n2 == n1

    }

    def "should split first example"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[[[[0,7],4],[15,[0,13]]],[1,1]]")
        SnailFish.Number n2 = new SnailFish.Number("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]")
        SnailFish.Number n3 = new SnailFish.Number("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]")
        SnailFish.Number n4 = new SnailFish.Number("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")

        expect:
        n2 == n1.splitReduce()
        n3 == n2.splitReduce()
        n4 == n3.reduce()
    }

    def "should reduce fifth example number"() {

        given:
        SnailFish.Number n1 = new SnailFish.Number("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
        SnailFish.Number n2 = new SnailFish.Number("[[3,[2,[8,0]]],[9,[5,[7,0]]]]")


        expect:
        n2 == n1.reduce()
    }

    def "should reduce a fourth number"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")
        SnailFish.Number n2 = new SnailFish.Number("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")


        expect:
        n2 == n1.reduce()
    }

    def "should reduce a third number"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[[6,[5,[4,[3,2]]]],1]")
        SnailFish.Number n2 = new SnailFish.Number("[[6,[5,[7,0]]],3]")


        expect:
        n2 == n1.reduce()
    }

    def "should reduce a second number"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[7,[6,[5,[4,[3,2]]]]]")
        SnailFish.Number n2 = new SnailFish.Number("[7,[6,[5,[7,0]]]]")


        expect:
        n2 == n1.reduce()
    }

    def "should reduce a number"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[[[[[9,8],1],2],3],4]")
        SnailFish.Number n2 = new SnailFish.Number("[[[[0,9],2],3],4]")


        expect:
        n2 == n1.reduce()
    }

    def "should add 2 numbers"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[1,2]")
        SnailFish.Number n2 = new SnailFish.Number("[[3,4],5]")

        SnailFish.Number n3 = new SnailFish.Number("[[1,2],[[3,4],5]]")


        expect:
        n3 == n1.add(n2)
    }
}
