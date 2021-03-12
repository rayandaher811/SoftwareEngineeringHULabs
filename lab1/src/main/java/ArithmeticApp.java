
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class ArithmeticApp {
    public static final String BASE_PATH_FOR_TEST_FILES
            = "C:\\Users\\barak\\IdeaProjects\\SoftwareEngineeringHULabs\\lab1\\src\\";
    public static final String BASE_PATH_FOR_INPUT_FILES = BASE_PATH_FOR_TEST_FILES + "inputFiles\\";

    public static boolean writeToOutputFile(String path, String fileName, Collection<String> data) throws IOException {
        String outputFileName = path + "\\" + fileName;
        removeIfFileExists(outputFileName);
        FileWriter fileWriter = new FileWriter(outputFileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        data.forEach(printWriter::println);
        printWriter.close();
        return true;
    }

    private static void removeIfFileExists(String fileName){
        try {
            File f = new File(fileName);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception e) {

        }
    }

    public static String getRawDataFromFile(String pathToFile) {
        String inputFileAsString = "";
        try {
            File myFile = new File(System.getProperty("user.dir") + "\\" + pathToFile);
            Scanner fileScanner = new Scanner(myFile);

            while (fileScanner.hasNextLine()) {
                inputFileAsString += fileScanner.nextLine() + "\n";
            }
            inputFileAsString = inputFileAsString.substring(0, inputFileAsString.lastIndexOf('\n'));
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputFileAsString;
    }

    public static void solveAndWriteResultToOutputFile(String fileName, String rawExpression) {
        String outputPath = System.getProperty("user.dir");
        Collection<String> result = getResult(rawExpression);
        try {
            if (!writeToOutputFile(outputPath, fileName, result))
                System.out.println("ERROR!!!");
            result.stream().forEach(s -> System.out.println(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Collection<String> getResult(String rawExpression) {
        ArrayList<String> result = new ArrayList<>();
        result.add("Please enter expression:");

        // Fixing spaces to none and +- to - in the expression
        var fixedExpression = rawExpression.replace(" ", "").replace("+-", "-");

        var expressionResult = calculateExpression(fixedExpression);

        result.add("The value of expression " + rawExpression.replaceAll(" ", "") + " is: " + expressionResult);

        return result;
    }

    private static String calculateExpression(String expression) {
        return calculatePlusAndMinus(calculateMultiplyAndDivide(calculateParenthesis(expression)));
    }

    /**
     * The method replaces the expression's parenthesis with it's actual result
     *
     * @param expression
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
     * @param expression
     * @return new expression with no * or / (all of them has been calculated)
     */
    private static String calculateMultiplyAndDivide(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                var leftNumberString = getLastDetectedNumber(expression.substring(0, i));
                var rightNumberString = getFirstDetectedNumber(expression.substring(i + 1));

                var leftNumber = Double.parseDouble(leftNumberString);
                var rightNumber = Double.parseDouble(rightNumberString);
                Double result = 0.0;

                if (expression.charAt(i) == '*')
                    result = leftNumber * rightNumber;
                else
                    result = leftNumber / rightNumber;

                // Parsing the result to string
                String resultStr = String.format("%.5f", result);

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
     * @param expression
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
                Double result = 0.0;

                if (expression.charAt(i) == '+')
                    result = leftNumber + rightNumber;
                else
                    result = leftNumber - rightNumber;

                // Parsing the result to string
                String resultStr = String.format("%.5f", result);

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
            // In case our number is negative
            if (currentChar == '-' && i - 1 < 0) {
                return expression.substring(i);
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

    public static void main(String[] args) {
        String expression = "";
        String outputFileName = "out.txt";
        System.out.println("Enter Expression:");
        if (args.length == 0) {
            // User didn't specify input file, thus need to get an input
            Scanner in = new Scanner(System.in);
            expression = in.nextLine();
        } else {
            expression = ArithmeticApp.getRawDataFromFile(args[0]);
            if (args.length > 2)
                outputFileName = args[1];
        }
        ArithmeticApp.solveAndWriteResultToOutputFile(outputFileName, expression);
    }
}
