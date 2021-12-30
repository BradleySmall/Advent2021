package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

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

        public Number(String str) {
            this.str = str;
        }

        Number explodeReduce() {
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
                String before = populateBeforeValue(nd);
                stringBuilderBeginning(nd, lNum, before, out);

                out.append("0");

                int rNum = Integer.parseInt(numbs[1]);
                String after = populateAfterValue(nd);
                stringBuilderEnding(nd, out, rNum, after);

                this.str = out.toString();
            }
            return this;
        }

        private void stringBuilderEnding(NumberData nd, StringBuilder out, int rNum, String after) {
            String sub = str.substring(nd.end + 1);
            if (!after.isEmpty()) {
                rNum += Integer.parseInt(after);
                after = String.valueOf(rNum);
                out.append(sub
                        .replaceFirst("\\d+", after));
            } else {
                out.append(sub);
            }
        }

        private void stringBuilderBeginning(NumberData nd, int lNum, String before, StringBuilder out) {
            String sub = str.substring(0, nd.begin);
            if (!before.isEmpty()) {
                lNum += Integer.parseInt(before);
                before = new StringBuilder(String.valueOf(lNum)).reverse().toString();

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

        private String populateBeforeValue(NumberData nd) {
            String before = "";
            Matcher matcher = Pattern.compile("(?:.*?(\\d+))+").matcher(str);
            matcher.region(0, nd.begin - 1);
            if (matcher.find()) {
                before = matcher.group(1);
            }
            return before;
        }

        private String populateAfterValue(NumberData nd) {
            String after = "";

            Matcher matcher = Pattern.compile("\\d+").matcher(str);
            matcher.region(nd.end + 1, str.length() - 1);
            if (matcher.find()) {
                after = matcher.group(0);
            }
            return after;
        }

        private void populateNumberData(NumberData nd) {
            String[] split = str.split("");
            for (int idx = 0; idx < split.length; ++idx) {
                String a = split[idx];
                if (a.equals("[")) {
                    ++nd.level;
                    if (nd.level > 4 && split[idx + 1].matches("(.*)\\d(.*)")) {
                        nd.begin = idx;
                        nd.sb.setLength(0);
                        nd.sb.append(a);
                    }
                } else if (a.equals("]")) {
                    --nd.level;
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

        public Number add(Number n) {
            if (this.str.equals("")) this.str = n.str;
            else this.str = format("[%s,%s]", str, n.toString());

            while (needsExploded() || needsSplit()) {
                if (needsExploded()) {
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

        static class NumberData {
            final StringBuilder  sb = new StringBuilder();
            int begin = -1;
            int end = -1;
            int level = 0;
        }
    }

}
