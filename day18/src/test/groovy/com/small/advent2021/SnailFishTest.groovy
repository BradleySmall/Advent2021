package com.small.advent2021

import spock.lang.Specification


class SnailFishTest extends Specification {
    private SnailFish snailFish

    def setup() {
        snailFish = new SnailFish()
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
