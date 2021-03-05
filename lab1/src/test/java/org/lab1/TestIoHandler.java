package org.lab1;

import org.lab1.utilities.IoFilesHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.lab1.utilities.Constants.BASE_PATH_FOR_INPUT_FILES;
import static org.lab1.utilities.Constants.BASE_PATH_FOR_OUTPUT_FILES;

public class TestIoHandler {
    @Test
    @DisplayName("Read input file 1 expression as expected")
    public void readingInputFileOneExpressionAsExpected() {
        assertEquals("15 + 22/0.5 *  0.25 - 4",
            IoFilesHandler.readExpressionInputFromFile(BASE_PATH_FOR_INPUT_FILES + "in1.txt").getRawExpression());
    }

    @Test
    @DisplayName("Read input file 2 expression as expected")
    public void readingInputFileTwoExpressionAsExpected() {
        assertEquals("22 + -3 - 21 + 7",
            IoFilesHandler.readExpressionInputFromFile(BASE_PATH_FOR_INPUT_FILES + "in2.txt").getRawExpression());
    }

    @Test
    @DisplayName("Read input file 3 expression as expected")
    public void readingInputFileThreeExpressionAsExpected() {
        assertEquals("22.75*15.2 -  3 + 14*9/5 - 1200",
            IoFilesHandler.readExpressionInputFromFile(BASE_PATH_FOR_INPUT_FILES + "in3.txt").getRawExpression());
    }

    @Test
    @DisplayName("Read input file 4 expression as expected")
    public void readingInputFileFourExpressionAsExpected() {
        assertEquals("-5 * -60 + 300 - 360",
            IoFilesHandler.readExpressionInputFromFile(BASE_PATH_FOR_INPUT_FILES + "in4.txt").getRawExpression());
    }

    @Test
    @DisplayName("Read output file number 1 result's as expected")
    public void readingOutputFileOneExpectedResult() {
        assertEquals("22.00000",
                IoFilesHandler.readExpectedResultFromOutputFile(BASE_PATH_FOR_OUTPUT_FILES + "out1.txt"));
    }

    @Test
    @DisplayName("Read output file number 2 result's as expected")
    public void readingOutputFileTwoExpectedResult() {
        assertEquals("5.00000",
                IoFilesHandler.readExpectedResultFromOutputFile(BASE_PATH_FOR_OUTPUT_FILES + "out2.txt"));
    }

    @Test
    @DisplayName("Read output file number 3 result's as expected")
    public void readingOutputFileThreeExpectedResult() {
        assertEquals("-832.00000",
                IoFilesHandler.readExpectedResultFromOutputFile(BASE_PATH_FOR_OUTPUT_FILES + "out3.txt"));
    }

    @Test
    @DisplayName("Read output file number 4 result's as expected")
    public void readingOutputFileFourExpectedResult() {
        assertEquals("240.00000",
                IoFilesHandler.readExpectedResultFromOutputFile(BASE_PATH_FOR_OUTPUT_FILES + "out4.txt"));
    }
}
