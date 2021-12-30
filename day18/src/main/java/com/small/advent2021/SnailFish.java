package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Slf4j
public class SnailFish {
    private SnailFish() {

    }

    public static class Number {
        private String str;

        public Number() {
            this("");
        }

        public Number(String str) {
            this.str = str;
        }

        public int getMagnitude() {
            int [] numbs = Arrays.stream(str
                    .replace("[","")
                    .replace("]","")
                    .split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            return (numbs[0] * 3) + (numbs[1] * 2);
        }
        public Number explodeReduce() {
            NumberData nd = new NumberData();

            populateNumberData(nd);

            if (!nd.sb.isEmpty()) {
                String chunk = nd.sb.toString();
                String[] numbs = chunk
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");

                StringBuilder out = new StringBuilder();

                int lNum = Integer.parseInt(numbs[0]);
                String before = populateBeforeValue(nd, lNum);
                stringBuilderBeginning(nd, before, out);

                out.append("0");

                int rNum = Integer.parseInt(numbs[1]);
                String after = populateAfterValue(nd, rNum);
                stringBuilderEnding(nd, after, out);

                this.str = out.toString();
            }
            return this;
        }

        public Number add(Number n) {
            if (this.str.equals("")) this.str = n.str;
            else this.str = format("[%s,%s]", str, n.toString());

            while (needsExploded() || needsSplit()) {
                while (needsExploded()) {
                    explodeReduce();
                }
                if (needsSplit()) {
                    splitReduce();
                }
            }
            return this;
        }

        public Number addList(List<Number> list) {
            this.str = "";
            for (var a : list) {
                add(a);
            }

            return this;
        }

        public Number splitReduce() {
            StringBuilder sb = new StringBuilder();
            String openBrackets = "";
            String closeBrackets = "";
            String number;
            boolean done = false;

            for (var a : str.split(",")) {
                if (a.charAt(0) == '[') {
                    int idx = a.lastIndexOf('[') + 1;
                    openBrackets = a.substring(0, idx);
                    number = a.substring(idx);
                } else {
                    int idx = a.indexOf(']');
                    number = a.substring(0, idx);
                    closeBrackets = a.substring(idx);
                }

                if (!openBrackets.isEmpty()) sb.append(openBrackets);
                if (!done) {
                    var t = getNumberOrSplit(number);
                    if (!t.equals(number)) {
                        done = true;
                        number = t;
                    }
                }
                sb.append(number);
                if (!closeBrackets.isEmpty()) sb.append(closeBrackets);
                sb.append(",");
                openBrackets = "";
                closeBrackets = "";
            }
            sb.deleteCharAt(sb.length() - 1);
            this.str = sb.toString();
            return this;
        }

        private void stringBuilderEnding(NumberData nd, String after, StringBuilder out) {
            String sub = str.substring(nd.end + 1);
            if (!after.isEmpty()) {
                out.append(sub
                        .replaceFirst("\\d+", after));
            } else {
                out.append(sub);
            }
        }

        private void stringBuilderBeginning(NumberData nd, String before, StringBuilder out) {
            String sub = str.substring(0, nd.begin);
            if (!before.isEmpty()) {
                before = new StringBuilder(before).reverse().toString();

                out.append(new StringBuilder(sub)
                                .reverse()
                                .toString()
                                .replaceFirst("\\d+", before)
                        )
                        .reverse();
            } else {
                out.append(sub);
            }
        }

        private String populateBeforeValue(NumberData nd, int lNum) {
            String before = "";

            String sub = new StringBuilder(str.substring(0, nd.begin)).reverse().toString();

            Matcher matcher = Pattern.compile("\\d+").matcher(sub);
            if (matcher.find()) {
                before = new StringBuilder(matcher.group(0)).reverse().toString();
                before = String.valueOf(Integer.parseInt(before) + lNum);
            }
            return before;
        }

        private String populateAfterValue(NumberData nd, int rNum) {
            String after = "";

            String sub = str.substring(nd.end + 1);

            Matcher matcher = Pattern.compile("\\d+").matcher(sub);
            if (matcher.find()) {
                after = matcher.group(0);
                after = String.valueOf(Integer.parseInt(after) + rNum);
            }
            return after;
        }

        private void populateNumberData(NumberData nd) {
            String[] split = str.split("");
            int level = 0;
            for (int idx = 0; idx < split.length; ++idx) {
                String a = split[idx];
                if (a.equals("[")) {
                    ++level;
                    if (level > 4 && split[idx + 1].matches("(.*)\\d(.*)")) {
                        nd.begin = idx;
                        nd.sb.setLength(0);
                        nd.sb.append(a);
                    }
                } else if (a.equals("]")) {
                    --level;
                    if (!nd.sb.isEmpty()) {
                        nd.sb.append(a);
                        nd.end = idx;
                        break;
                    }
                } else if (!nd.sb.isEmpty()) {
                    nd.sb.append(a);
                }
            }
        }

        private boolean needsSplit() {
            return str.matches(".*\\d\\d.*");
        }

        private boolean needsExploded() {
            NumberData nd = new NumberData();
            populateNumberData(nd);
            return !nd.sb.isEmpty();
        }

        private String getNumberOrSplit(String number) {
            StringBuilder sb = new StringBuilder();
            int num = Integer.parseInt(number);
            if (num > 9) {
                int leftNum;
                int rightNum;
                if (num % 2 == 0) {
                    leftNum = num / 2;
                    rightNum = leftNum;
                } else {
                    leftNum = num / 2;
                    rightNum = leftNum + 1;
                }
                sb.append(format("[%d,%d]", leftNum, rightNum));
            } else {
                sb.append(number);
            }

            return sb.toString();
        }

        @Override
        public String toString() {
            return str;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Number number = (Number) o;
            return Objects.equals(str, number.str);
        }

        @Override
        public int hashCode() {
            return Objects.hash(str);
        }

        static class NumberData {
            final StringBuilder sb = new StringBuilder();
            int begin = -1;
            int end = -1;
        }
    }
}
