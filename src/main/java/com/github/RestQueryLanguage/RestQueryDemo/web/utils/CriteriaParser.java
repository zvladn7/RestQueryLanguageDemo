package com.github.RestQueryLanguage.RestQueryDemo.web.utils;

import com.google.common.base.Joiner;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriteriaParser {

    private static Map<String, Operator> ops;

    private static final String OPERATION_SET = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);

    private static final String REGEX = "(\\w+?)(" + OPERATION_SET + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?)$";

    private static final Pattern SpecificationSearchCriteriaRegex = Pattern.compile(REGEX);

    private enum Operator {
        OR(1),
        AND(2);

        final int precedence;

        Operator(int precedence) {
            this.precedence = precedence;
        }
    }

    static {
        //returns unmodifiable map
        ops = Map.of(
                "AND", Operator.AND,
                "and", Operator.AND,
                "OR", Operator.OR,
                "or", Operator.OR
        );
    }

    private static boolean isHigherPrecedenceOperator(final String currOp, final String prevOp) {
        return ops.containsKey(prevOp) && ops.get(prevOp).precedence >= ops.get(currOp).precedence;
    }

    public Deque<?> parse(final String searchParam) {

        Deque<Object> output = new LinkedList<>();
        Deque<String> stack = new LinkedList<>();

        Arrays.stream(searchParam.split("\\s+")).forEach(token -> {
            if (ops.containsKey(token)) { // if OR/AND
                while (!stack.isEmpty() && isHigherPrecedenceOperator(token, stack.peek())) {
                    output.push(stack.pop()
                            .equalsIgnoreCase(SearchOperation.OR_OPERATOR)
                            ? SearchOperation.OR_OPERATOR
                            : SearchOperation.AND_OPERATOR);
                }
                stack.push(
                        token.equalsIgnoreCase(SearchOperation.OR_OPERATOR)
                        ? SearchOperation.OR_OPERATOR
                        : SearchOperation.AND_OPERATOR
                );
            } else if (token.equals(SearchOperation.LEFT_PARANTHESIS)) {
                stack.push(SearchOperation.LEFT_PARANTHESIS);
            } else if (token.equals(SearchOperation.RIGHT_PARANTHESIS)) {
                while (!stack.peek().equals(SearchOperation.LEFT_PARANTHESIS)) {
                    output.push(stack.pop());
                }
                stack.pop(); // delete left paranthesis from the stack
            } else {
                Matcher matcher = SpecificationSearchCriteriaRegex.matcher(token);
                boolean cond =  matcher.find();
                while (cond) {
                    output.push(new SpecificationSearchCriteria(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5)
                    ));
                    cond = matcher.find();
                }
            }
        });

        while (!stack.isEmpty()) {
            output.push(stack.pop());
        }

        return output;
    }

}
