package com.small.advent2021

import spock.lang.Specification


class SnailFishTest extends Specification {
    def "should show magnitude" () {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[9,1]")
        SnailFish.Number n2 = new SnailFish.Number("[1,9]")

        expect:
        n1.getMagnitude() == 29
        n2.getMagnitude() == 21
    }

    def "should add list example 4"() {
        given:
        var theList = List.of(
        new SnailFish.Number("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]"),
        new SnailFish.Number("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"),
        new SnailFish.Number("[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]"),
        new SnailFish.Number("[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]"),
        new SnailFish.Number("[7,[5,[[3,8],[1,4]]]]"),
        new SnailFish.Number("[[2,[2,2]],[8,[8,1]]]"),
        new SnailFish.Number("[2,9]"),
        new SnailFish.Number("[1,[[[9,3],9],[[9,0],[0,7]]]]"),
        new SnailFish.Number("[[[5,[7,4]],7],1]"),
        new SnailFish.Number("[[[[4,2],2],6],[8,7]]")
        )

        SnailFish.Number n1 = new SnailFish.Number().addList(theList)
        SnailFish.Number n2 = new SnailFish.Number("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
        expect:
        n2 == n1
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
        SnailFish.Number n1 = new SnailFish.Number().addList(theList)
        SnailFish.Number n2 = new SnailFish.Number("[[[[5,0],[7,4]],[5,5]],[6,6]]")

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
        SnailFish.Number n1 = new SnailFish.Number().addList(theList)
        SnailFish.Number n2 = new SnailFish.Number("[[[[3,0],[5,3]],[4,4]],[5,5]]")

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
        SnailFish.Number n1 = new SnailFish.Number().addList(theList)
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
        n4 == n3.explodeReduce()
    }

    def "should reduce fifth example number"() {

        given:
        SnailFish.Number n1 = new SnailFish.Number("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
        SnailFish.Number n2 = new SnailFish.Number("[[3,[2,[8,0]]],[9,[5,[7,0]]]]")


        expect:
        n2 == n1.explodeReduce()
    }

    def "should reduce a fourth number"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")
        SnailFish.Number n2 = new SnailFish.Number("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")


        expect:
        n2 == n1.explodeReduce()
    }

    def "should reduce a third number"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[[6,[5,[4,[3,2]]]],1]")
        SnailFish.Number n2 = new SnailFish.Number("[[6,[5,[7,0]]],3]")


        expect:
        n2 == n1.explodeReduce()
    }

    def "should reduce a second number"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[7,[6,[5,[4,[3,2]]]]]")
        SnailFish.Number n2 = new SnailFish.Number("[7,[6,[5,[7,0]]]]")


        expect:
        n2 == n1.explodeReduce()
    }

    def "should reduce a number"() {
        given:
        SnailFish.Number n1 = new SnailFish.Number("[[[[[9,8],1],2],3],4]")
        SnailFish.Number n2 = new SnailFish.Number("[[[[0,9],2],3],4]")


        expect:
        n2 == n1.explodeReduce()
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
