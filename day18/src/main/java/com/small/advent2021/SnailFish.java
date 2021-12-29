package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

@Slf4j
public class SnailFish {


    public static class Number {
        private final String str;

        public Number(String str) {
            this.str = str;
        }
        public Number add(Number n) {
            return new Number(String.format("[%s,%s]", str, n.toString()));
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

        public Number reduce() {
            StringBuilder sb = new StringBuilder();
            Deque<String> stack = new ArrayDeque<>();

            int depth = 0;
            boolean addNext = false;

            for(var a : str.split(",")) {
                String brackets;
                String num;
                String addNum = null;
                if(a.charAt(0) == '[') {
                    int idx = a.lastIndexOf('[') + 1;
                    String theBkts = a.substring(0,idx);
                    String theNum = a.substring(idx);

                    if(addNext == true) {
                        String comma = stack.pop();
                        String bkts = stack.pop();
                        addNum = stack.pop();
                        stack.push("0");
                        stack.push(bkts.substring(1));
                        stack.push(comma);
                        addNext = false;
                    }


                    depth += idx;
                    if (depth > 4) {
                        brackets = a.substring(1, idx);
                        if (stack.peek() == null || stack.peek().contains("[")) {
                            num = "0";
                        } else {
                            stack.pop();
                            var tmp = stack.pop();
                            num = String.format("%d", Integer.parseInt(tmp) + Integer.parseInt(a.substring(idx)));
                        }
                        addNext = true;
                    } else {
                        brackets = a.substring(0, idx);
                        if (addNum != null)
                            num = String.format("%d", Integer.parseInt(addNum) + Integer.parseInt(a.substring(idx)));
                        num = a.substring(idx);
                    }
                    stack.push(brackets);
                    stack.push(num);
                } else {
                    int idx = a.indexOf(']');

                    if (depth > 4) {
                        brackets = a.substring(idx);
                        num = a.substring(0,idx);
                        stack.push(num);
                        stack.push(brackets);
                        addNext = true;
                    } else if (addNext){
                        String comma = stack.pop();
                        String bkts = stack.pop();
                        var tmp = stack.pop();
                        stack.push("0");
                        stack.push(bkts.substring(1));
                        stack.push(comma);

                        num = String.format("%d", Integer.parseInt(tmp) + Integer.parseInt(a.substring(0,idx)));
                        brackets = a.substring(idx);
                        stack.push(num);
                        stack.push(brackets);
                        addNext = false;
                    } else {
                        num = a.substring(0,idx);
                        brackets = a.substring(idx);
                        stack.push(num);
                        stack.push(brackets);
                    }
                    depth -= a.length() - idx;
                }
                stack.push(",");
            }
            if(addNext == true) {
                String s1 = stack.pop();
                String s2 = stack.pop();
                String s3 = stack.pop();
                stack.push("0");
                stack.push(s2.substring(1));
                stack.push(s3);
                addNext = false;
            }

            stack.removeFirst();
            do {
                var t = stack.removeLast();
                sb.append(t);
            } while (!stack.isEmpty());
            return new Number(sb.toString());
        }
    }
}
