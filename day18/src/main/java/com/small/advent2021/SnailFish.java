package com.small.advent2021;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

@Slf4j
public class SnailFish {
    private SnailFish() {

    }

    public static class Number {
        private final String str;

        public Number(String str) {
            this.str = str;
        }

        private static Number numberFromStack(StringBuilder sb, Deque<String> stack) {
            if (stack.peek() != null && stack.peek().equals(",")) stack.pop();
            do {
                var t = stack.removeLast();
                sb.append(t);
            } while (!stack.isEmpty());
            return new Number(sb.toString());
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
            boolean explode = false;

            String openBrackets = "";
            String closeBrackets = "";
            String number;
            String addNumber = "";

            for(var a : str.split(",")) {
                if(a.charAt(0) == '[') {
                    int idx = a.lastIndexOf('[') + 1;
                    openBrackets = a.substring(0,idx);
                    number = a.substring(idx);
                } else {
                    int idx = a.indexOf(']');
                    number = a.substring(0,idx);
                    closeBrackets = a.substring(idx);
                }

                depth += pushBrackets(openBrackets, stack);
                if (depth > 4) {

                    explode = true;
                    stack.pop();
                    --depth;

                    if (stack.peek() != null) {
                        if (stack.peek().equals("[")) {
                            number = "0";
                        } else {
                            stack.pop();
                            addNumber = stack.pop();
                            number = String.format("%d", Integer.parseInt(addNumber) + Integer.parseInt(number));
                            addNumber = "";
                        }
                    }
                } else {
                    if (explode) {
                        explode = false;
                        addNumber = number;
                        if (closeBrackets.length() > 1) {
                            closeBrackets = closeBrackets.substring(1);
                            number= "0";
                        } else {
                            closeBrackets = "";
                        }

                        --depth;
                    } else if (!addNumber.isEmpty()) {
                        String s1 = stack.pop();
                        String s2 = stack.pop();

                        if(s2.matches("[0-9]+")) {
                            number = String.format("%d", Integer.parseInt(s2) + Integer.parseInt(number));
                        } else {
                            stack.push(s2);
                            stack.push(s1);
                            number = String.format("%d", Integer.parseInt(addNumber) + Integer.parseInt(number));
                        }

                        addNumber = "";
                    }
                }
                stack.push(number);
                depth -= pushBrackets(closeBrackets, stack);
                stack.push(",");

                openBrackets = "";
                closeBrackets = "";
            }

            return numberFromStack(sb, stack);
        }

        private int pushBrackets(String brackets, Deque<String> stack) {
            int depth=0;
            if (brackets.length() == 0) return depth;
            for (var bracket : brackets.split("")) {
                stack.push(bracket);
                ++depth;
            }
            return depth;
        }
    }

}
