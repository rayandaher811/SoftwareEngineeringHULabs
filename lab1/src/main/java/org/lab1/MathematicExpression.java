package org.lab1;

import org.lab1.utilities.IoFilesHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class MathematicExpression {
    private String rawExpression;
    private String calculatedResult;
    private String outputPath;
    private String fileName;

    public MathematicExpression(String rawExpression, String outputPath, String fileName) {
        this.rawExpression = rawExpression;
        this.calculatedResult = "";
        this.outputPath = outputPath;
        this.fileName = fileName;
    }

    public String getRawExpression() {
        return rawExpression;
    }

    private Collection<String> getResult() {
        ArrayList<String> result = new ArrayList<>();
        result.add("Please enter expression:");
        // TODO: calculate the expression
        result.add("The value of expression " + rawExpression.replaceAll(" ", "") + " is: " + calculatedResult);

        return result;
    }

    public void solveAndWriteResultToOutputFile(){
        Collection<String> result = getResult();
        try {
            if (!IoFilesHandler.writeToOutputFile(outputPath, fileName, result))
                System.out.println("ERROR!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
