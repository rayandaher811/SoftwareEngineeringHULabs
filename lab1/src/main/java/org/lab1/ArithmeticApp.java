package org.lab1;

import org.lab1.utilities.IoFilesHandler;

import static org.lab1.utilities.Constants.BASE_PATH_FOR_INPUT_FILES;

public class ArithmeticApp {
    public static void main(String[] args){
        MathematicExpression expression =
                IoFilesHandler.readExpressionInputFromFile(BASE_PATH_FOR_INPUT_FILES + "in5.txt");
        System.out.println(expression.getRawExpression());

        // Performing ouput file creation after calculation
        expression.solveAndWriteResultToOutputFile();
    }
}
