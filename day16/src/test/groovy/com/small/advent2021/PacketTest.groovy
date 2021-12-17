package com.small.advent2021

import spock.lang.Specification

class PacketTest extends Specification {

    def "solve the puzzle" () {
        given:
        Packet packet = new Packet("input.txt")
        when:
        var versions = packet.processPackets()
        then:
        versions.size() == 268

    }
    def "should decode multiple packets"() {
        given:
        Packet packet = new Packet("input.txt")
        var String packetTxt = "11101110000000001101010000001100100000100011000001100000"
        var versions = new ArrayList<String>()
        packet.decodePacket(packetTxt, versions)
        expect:
        versions.size() == 4
        versions.toString() == "[111, 010, 100, 001]"

    }
    def "should decode binary packet" () {
        given:
        Packet packet = new Packet("input.txt")
        var String packetTxt = "110100101111111000101000"
        var versions = new ArrayList<String>()
        packet.decodePacket(packetTxt, versions)
        expect:
        versions.size() == 1
        versions.toString() == "110"

    }
    def "should decode to binary" () {

        given:
        Packet packet = new Packet("input.txt")
        var encoded = "D2FE28"
        expect:
        packet.decodeToBinary(encoded) == "110100101111111000101000"
    }
}
