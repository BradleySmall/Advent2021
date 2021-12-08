package com.small.advent2021;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Log4j
public class PowerConsumption {
    PowerConsumption(@SuppressWarnings("SameParameterValue") String fileName) {
        bitArrayList = getListFromFile(fileName);
        processData();
    }

    private final List<int[]> bitArrayList;

    private int width;

    private Integer gamaRate;
    private Integer epsilonRate;
    private Integer consumption;
    private Integer o2GeneratorRating;
    private Integer co2ScrubberRating;
    private Integer lifeSupportRating;

    public static void main(String[] args) {

        PowerConsumption powerConsumption = new PowerConsumption("day3/input.txt");

        log.info("\n*******************************************" +
                 "\n***************Program Output**************" +
                 "\n*      gamaRate=" + powerConsumption.gamaRate +
                 "\n*      epsilonRate=" + powerConsumption.epsilonRate +
                 "\n*      consumption=" + powerConsumption.consumption +
                 "\n*      o2 Generator Rating=" + powerConsumption.o2GeneratorRating +
                 "\n*      co2 Scrubber Rating=" + powerConsumption.co2ScrubberRating +
                 "\n*      Life Support=" + powerConsumption.lifeSupportRating
        );
    }

    public Integer getO2GeneratorRating() {
        return o2GeneratorRating;
    }

    public Integer getCo2ScrubberRating() {
        return co2ScrubberRating;
    }

    public Integer getLifeSupportRating() {
        return lifeSupportRating;
    }

    public Integer getGamaRate() {
        return gamaRate;
    }

    public Integer getEpsilonRate() {
        return epsilonRate;
    }

    public Integer getConsumption() {
        return consumption;
    }

    private List<int[]> getListFromFile(String fileName) {
        Path path = Path.of(fileName);
        try {
            width = Files.readString(path)
                    .indexOf('\n');
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }

        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .map(e -> e
                            .chars()
                            .map(Character::getNumericValue)
                            .toArray())
                    .toList();
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    private void processData() {
        int count = bitArrayList.size();
        int[] gamaBits = new int[width];


        gamaRateFromData(count, gamaBits, bitArrayList);
        epsilonRateFromData(gamaBits);
        consumption = gamaRate * epsilonRate;

        o2GeneratorRatingFromData(bitArrayList);
        co2ScrubberRatingFromData(bitArrayList);
        lifeSupportRating = o2GeneratorRating * co2ScrubberRating;

    }

    private void co2ScrubberRatingFromData(List<int[]> bitArrayList) {
        List<int[]> theList = new ArrayList<>(bitArrayList);
        for (int i = 0; i < width && theList.size() > 1; ++i) {

            int count = 0;
            for (int[] bit : theList) {
                count += bit[i];
            }

            int finalIndex = i;
            int finalCount = count;
            int half = (theList.size() + 1) / 2;
            theList.removeIf(e -> e[finalIndex] == ((finalCount < half) ? 0 : 1));
        }

        co2ScrubberRating = intFromBitArray(theList.get(0));
    }

    private void o2GeneratorRatingFromData(List<int[]> bitArrayList) {
        List<int[]> theList = new ArrayList<>(bitArrayList);

        for (int i = 0; i < width && theList.size() > 1; ++i) {

            int count = 0;
            for (int[] bit : theList) {
                count += bit[i];
            }

            int finalIndex = i;
            int finalCount = count;
            int half = (theList.size() + 1) / 2;
            theList.removeIf(e -> e[finalIndex] == ((finalCount >= half) ? 0 : 1));
        }

        o2GeneratorRating = intFromBitArray(theList.get(0));
    }

    private Integer intFromBitArray(int[] bitArray) {
        String bitArrayString = Arrays.toString(bitArray)
                .replace("[", "")
                .replace(",", "")
                .replace("]", "")
                .replace(" ", "");
        return Integer.parseInt(bitArrayString, 2);
    }

    private void epsilonRateFromData(int[] gamaBits) {
        int[] epsilonBits = new int[width];
        for (int index = 0; index < width; ++index) {
            epsilonBits[index] = gamaBits[index] == 1 ? 0 : 1;
        }

        epsilonRate = intFromBitArray(epsilonBits);
    }

    private void gamaRateFromData(int count, int[] gamaBits, List<int[]> bitArrayList) {
        for (int[] bitArray : bitArrayList) {
            gamaBitsFromBitArray(gamaBits, bitArray);
        }

        for (int index = 0; index < width; ++index) {
            gamaBits[index] = gamaBits[index] >= (count / 2) ? 1 : 0;
        }

        gamaRate = intFromBitArray(gamaBits);
    }

    private void gamaBitsFromBitArray(int[] gamaBits, int[] bitArray) {
        for (int x = 0; x < width; ++x) {
            gamaBits[x] += bitArray[x];
        }
    }
}
