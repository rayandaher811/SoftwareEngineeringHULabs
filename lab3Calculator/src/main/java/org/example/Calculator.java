package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Calculator implements ICalculator {
    @Override
    public String calculate(String rawExpression) {
        // Fixing few chars in our expression:
        // spaces to none
        // +- to -
        // : to /
        var fixedExpression = rawExpression.replace(" ", "")
                                                  .replace("+-", "-")
                                                  .replace(":", "/");

        return "" + scaleExpressionResult(calculateExpression(fixedExpression));
    }

    private BigDecimal scaleExpressionResult(String result){
        return BigDecimal.valueOf(Double.parseDouble(result)).setScale(5, RoundingMode.DOWN);
    }

    private static String calculateExpression(String expression) {
        return calculatePlusAndMinus(calculateMultiplyAndDivide(calculateParenthesis(expression)));
    }

    /**
     * The method replaces the expression's parenthesis with it's actual result
     *
     * @param expression the expression to calculate
     * @return new expression with no parenthesis (all of them has been calculated)
     */
    private static String calculateParenthesis(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                var parenthesisStartIndex = i;
                var parenthesisClosureIndex = i;
                int recursiveParenthesis = 0;

                // Detect the relevant ) for our detected (
                for (int j = i + 1; j < expression.length(); j++) {
                    if (expression.charAt(j) == ')' && recursiveParenthesis == 0) {
                        parenthesisClosureIndex = j;
                        break;
                    } else if (expression.charAt(j) == '(') {
                        recursiveParenthesis++;
                    } else if (expression.charAt(j) == ')') {
                        recursiveParenthesis--;
                    }
                }

                // Calculating the detected parenthesis
                var calculatedParenthesis = calculateExpression(expression.substring(parenthesisStartIndex + 1, parenthesisClosureIndex));

                // Replacing the detected parenthesis by it's calculated value
                expression = expression.substring(0, parenthesisStartIndex) + calculatedParenthesis + expression.substring(parenthesisClosureIndex + 1);

                // Indenting the index properly due the expression change
                i = parenthesisStartIndex + calculatedParenthesis.length() - 1;
            }
        }

        return expression;
    }

    /**
     * The method replaces the expression's * and  / expression with it's actual result
     * Note: The expression must have no parenthesis
     *
     * @param expression the expression to calculate
     * @return new expression with no * or / (all of them has been calculated)
     */
    private static String calculateMultiplyAndDivide(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                var leftNumberString = getLastDetectedNumber(expression.substring(0, i));
                var rightNumberString = getFirstDetectedNumber(expression.substring(i + 1));

                var leftNumber = Double.parseDouble(leftNumberString);
                var rightNumber = Double.parseDouble(rightNumberString);
                double result = 0.0;

                if (expression.charAt(i) == '*')
                    result = leftNumber * rightNumber;
                else
                    result = leftNumber / rightNumber;

                // Parsing the result to string
                String resultStr = ""+ result;

                // Replacing the detected  * or / expression with it's calculated value
                expression = expression.substring(0, i - leftNumberString.length()) + resultStr + expression.substring(i + rightNumberString.length() + 1);

                // Indenting the index properly due the expression change
                i = i - leftNumberString.length() + resultStr.length() - 1;
            }
        }

        return expression;
    }

    /**
     * The method replaces the expression's + and  - expression with it's actual result
     * Note: The expression must have no parenthesis
     *
     * @param expression the expression to calculate
     * @return new expression with no + or - (all of them has been calculated)
     */
    private static String calculatePlusAndMinus(String expression) {
        // Detecting + and - from index 1 in order to avoid negative numbers confusion
        for (int i = 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                var leftNumberString = getLastDetectedNumber(expression.substring(0, i));
                var rightNumberString = getFirstDetectedNumber(expression.substring(i + 1));

                var leftNumber = Double.parseDouble(leftNumberString);
                var rightNumber = Double.parseDouble(rightNumberString);
                double result = 0.0;

                if (expression.charAt(i) == '+')
                    result = leftNumber + rightNumber;
                else
                    result = leftNumber - rightNumber;

                // Parsing the result to string
                String resultStr = "" + result;

                // Replacing the detected  + or - expression with it's calculated value
                expression = expression.substring(0, i - leftNumberString.length()) + resultStr + expression.substring(i + rightNumberString.length() + 1);

                // Indenting the index properly due the expression change
                i = i - leftNumberString.length() + resultStr.length() - 1;
            }
        }

        return expression;
    }

    private static String getLastDetectedNumber(String expression) {
        for (int i = expression.length() - 1; i >= 0; i--) {
            var currentChar = expression.charAt(i);

            if (currentChar == '+' || currentChar == '*' || currentChar == '/')
                return expression.substring(i + 1);
            if (currentChar == '-') {
                // In case our number is negative
                if(i - 1 < 0)
                    return expression.substring(i);
                else
                    return expression.substring(i + 1);
            }
        }

        return expression;
    }

    private static String getFirstDetectedNumber(String expression) {
        int i = 0;

        // In case our number is negative
        if (expression.charAt(0) == '-')
            i++;

        for (; i < expression.length(); i++) {
            var currentChar = expression.charAt(i);
            if (currentChar == '+' || currentChar == '*' || currentChar == '/' || currentChar == '-')
                return expression.substring(0, i);
        }

        return expression;
    }
}
