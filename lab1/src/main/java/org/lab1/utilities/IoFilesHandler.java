package org.lab1.utilities;

import org.lab1.MathematicExpression;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

public class IoFilesHandler {
    /**
     * Reading expression from input file
     * @param pathToFile path to input file
     * @return object represents read mathematical expression
     */
    public static MathematicExpression readExpressionInputFromFile(String pathToFile) {
        File inputFile = new File(pathToFile);
        String filePath = inputFile.getParentFile().toPath().toString();
        String fileName = inputFile.getName();

        return new MathematicExpression(readExpectedResultFromOutputFile(pathToFile), filePath, fileName);
    }

    /**
     * Reading expected output result
     * @param pathToFile path to given output file
     * @return expected expression value
     */
    public static String readExpectedResultFromOutputFile(String pathToFile) {
        String outputFileAsRawData = getRawDataFromFile(pathToFile);

        if (outputFileAsRawData != null) {
            String[] lines = outputFileAsRawData.split("\n");
            if (lines.length > 1)
                return extractExpectedResultFromLine(lines[1]);
            else
                return lines[0];

        }

        return "ERROR";
    }

    public static boolean writeToOutputFile(String path, String fileName, Collection<String> data) throws IOException {
            String outputFileName = path + "bg" + fileName;
            FileWriter fileWriter = new FileWriter(outputFileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            data.forEach(printWriter::println);
            printWriter.close();
        return true;
    }

    private static String getRawDataFromFile(String pathToFile){
        String inputFileAsString = "";
        try {
            File myFile = new File(pathToFile);
            Scanner fileScanner = new Scanner(myFile);

            while (fileScanner.hasNextLine()) {
                inputFileAsString += fileScanner.nextLine() + "\n";
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputFileAsString;
    }

    private static String extractExpectedResultFromLine(String line){
        // Getting second line
        String result = line.substring(line.indexOf(':') + 1);

        // Cutting result's value
        return result.substring(result.indexOf(' ') + 1);
    }
}
