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

        Number explodeIfNeeded() {
            int level = 0;
            StringBuilder sb = new StringBuilder();
            int begin = -1;
            int end = -1;
            int lNum;
            int rNum;

            int idx = 0;
            for (var a : str.split("")) {
                if (a.equals("[")) ++level;
                if (a.equals("]")) --level;
                if (level > 4) {
                    if (sb.isEmpty()) begin = idx;
                    sb.append(a);
                }
                if (level == 4 && !sb.isEmpty()) {
                    end = idx;
                    sb.append(a);
                    break;
                }
                ++idx;
            }
            if (!sb.isEmpty()) {
                String chunk = sb.toString();
                String[] nums = chunk.replace("[", "").replace("]", "").split(",");
                lNum = Integer.parseInt(nums[0]);
                rNum = Integer.parseInt(nums[1]);

                String before = "";
                String after = "";


                log.info(format("Needs %s exploded at (%d-%d) lNum=%d rNum=%d", sb, begin, end, lNum, rNum));
                Matcher matcher = Pattern.compile("\\d+").matcher(str);
                matcher.region(end+1,str.length()-1);
                if (matcher.find()) {
                    after = matcher.group(0);
                    log.info(matcher.group(0));
                }

                Matcher matcher2 = Pattern.compile("(?:.*?(\\d+))+").matcher(str);
                matcher2.region(0,begin-1);
                if (matcher2.find()) {
                    before = matcher2.group(1);
                    log.info("Last before " + matcher2.group(1));
                }

                StringBuilder out = new StringBuilder();
                if (!before.isEmpty()) {
                    out.append( new StringBuilder(str.substring(0,begin))
                            .reverse()
                            .toString()
                            .replaceFirst("\\d+",
                                    String.valueOf((Integer.parseInt(before)+lNum)))
                            )
                            .reverse();
                } else {
                    out.append(str, 0, begin);
                }
                out.append("0");
                if (!after.isEmpty()) {
                    out.append(str.substring(end+1)
                            .replaceFirst("\\d+", String.valueOf(Integer.parseInt(after)+rNum)));
                }else {
                    out.append(str.substring(end+1));
                }
                log.info("Changes - " + out);

                this.str = out.toString();
            }
            return this;
        }

        public Number add(Number n) {
            if (this.str.equals("")) return n;
            return new Number(format("[%s,%s]", str, n.toString()));
        }
        public Number addList(List<Number> list) {
            Number n = new Number("");
            for (var a : list) {
                n = n.add(a);
            }

            return n;
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

            for(var a : str.split(",")) {
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
            sb.deleteCharAt(sb.length()-1);
            return new Number(sb.toString());
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
                sb.append(format("[%d,%d]",leftNum, rightNum));
            } else {
                sb.append(number);
            }


            return sb.toString();
        }
    }

}
