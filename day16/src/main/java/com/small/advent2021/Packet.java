package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Map.*;

/*
0 = 0000
1 = 0001
2 = 0010
3 = 0011
4 = 0100
5 = 0101
6 = 0110
7 = 0111
8 = 1000
9 = 1001
A = 1010
B = 1011
C = 1100
D = 1101
E = 1110
F = 1111
 */


@Slf4j
public class Packet {
    String xlate = "0 = 0000, 1 = 0001, 2 = 0010, 3 = 0011, 4 = 0100, 5 = 0101, 6 = 0110, 7 = 0111, 8 = 1000, 9 = 1001, A = 1010, B = 1011, C = 1100, D = 1101, E = 1110, F = 1111";
    private final Map<String, String> translateMap;
    private final Map<String, String> encodeMap;
    private String packetString;

    Packet(String fileName) {
        translateMap =
                stream(xlate.split(", "))
                        .map(entry -> entry.split(" = "))
                        .collect(Collectors.toMap(rule -> rule[0], rule -> rule[1]));
        log.debug(String.valueOf(translateMap));


        encodeMap = translateMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getValue, Entry::getKey));
        log.debug(String.valueOf(encodeMap));

        processFile(fileName);
    }

    public List<String> processPackets() {
        List<String> versions = new ArrayList<>();
        String packet = decodeToBinary(packetString);
        while (packet.length() > 8) {
            packet = decodePacket(packet, versions);
        }
        var total = versions
                .stream()
                .mapToInt(v -> Integer.parseInt(v,2))
                .sum();
        log.info(String.valueOf(total));
        return versions;
    }

    public static void main(String[] args) {
        log.info("Done!");
    }

    private void processFile(String fileName) {
        Path path = Path.of(fileName);
        try {
            packetString = Files.readString(path);
            log.debug(packetString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String decodeToBinary(String encodedHex) {
        return stream(encodedHex.split(""))
                .map(translateMap::get)
                .toList()
                .toString()
                .replace(", ", "")
                .replace("[", "")
                .replace("]", "");
    }

    public String decodePacket(String packet, List<String> versions) {
        String version = packet.substring(0, 3);
        versions.add(version);

        String type = packet.substring(3, 6);
        packet = packet.substring(6);
        log.info(String.format("V:%s T:%s", version, type));

        if (type.equals("100")) { // literal number
            List<String> pieces = new ArrayList<>();
            packet = readLiteral(packet, pieces);
            String decoded = String.join("", pieces);
            log.debug(String.format("   V:%s T:%s %s", version, type, decoded));
        } else {
            String lengthId = packet.substring(0, 1);
            log.info("LengthId=" + lengthId);
            if (lengthId.equals("0")) {
                packet = getSubpacketByLength(packet, versions);
                log.debug(String.format("   V:%s T:%s", version, type));
            } else if (lengthId.equals("1")) {
                packet = getSubpacketByCount(packet, versions);
                log.debug(String.format("   V:%s T:%s", version, type));
            }
        }

        return packet;
    }

    private String getSubpacketByCount(String packet, List<String> versions) {
        var subPacketLength = packet.substring(1, 1 + 11);
        int count = Integer.parseInt(subPacketLength, 2);
        packet = packet.substring(1 + 11);
        for (int x = 0; x < count; ++x) {
            packet = decodePacket(packet, versions);
        }
        return packet;
    }

    private String getSubpacketByLength(String packet, List<String> versions) {
        var subPacketLength = packet.substring(1, 1 + 15);
        int thisLength = Integer.parseInt(subPacketLength, 2);
        var subPacket = packet.substring(1 + 15, 1 + 15 + thisLength);
        while(subPacket.length() != 0) {
            subPacket = decodePacket(subPacket, versions);
        }
        return packet.substring(1 + 15 + thisLength);
    }

    private String readLiteral(String packet, List<String> pieces) {
        // walk every 5 bits looking for 0 and the next 4 bits end the packet
        int length = 0;
        for (int x = 0; x < packet.length() - 5; x += 5) {
            boolean isLast = packet.charAt(x) == '0';
            pieces.add(encodeMap.get(packet.substring(x + 1, x + 5)));

            if (isLast) {
                length = x + 5;
                break;
            }
        }
        if (length == 0) {
            // no packets left
            return "";
        } else {
            // next packet starts at substring(length)
            return packet.substring(length);
        }
    }
}
