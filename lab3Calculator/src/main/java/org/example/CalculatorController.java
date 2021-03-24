package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class CalculatorController implements Initializable {

    private  ICalculator calculator;

    @FXML
    private TextArea resultTextArea;

    @FXML
    private Button clear;

    //region Numbers buttons

    @FXML
    private Button one;

    @FXML
    private Button two;

    @FXML
    private Button three;

    @FXML
    private Button four;

    @FXML
    private Button five;

    @FXML
    private Button six;

    @FXML
    private Button seven;

    @FXML
    private Button eight;

    @FXML
    private Button nine;

    @FXML
    private Button zero;

    //endregion

    //region Operators Buttons

    @FXML
    private Button plus;

    @FXML
    private Button minus;

    @FXML
    private Button multiply;

    @FXML
    private Button divide;

    @FXML
    private Button openParenthesis;

    @FXML
    private Button closeParenthesis;

    @FXML
    private Button equals;
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initializing our calculations service
        calculator = new Calculator();

        clear.setOnAction(e -> resultTextArea.setText(""));
        equals.setOnAction(e -> resultTextArea.setText(calculator.calculate(resultTextArea.getText())));

        initializeNumbersButtons();
        initializeOperatorsButtons();
    }

    private void initializeOperatorsButtons() {
        plus.setOnAction(e -> insertOperator('+'));
        minus.setOnAction(e -> insertOperator('-'));
        multiply.setOnAction(e -> insertOperator('*'));
        divide.setOnAction(e -> insertOperator('/'));
        openParenthesis.setOnAction(e -> insertOperator('('));
        closeParenthesis.setOnAction(e -> insertOperator(')'));
    }

    private void initializeNumbersButtons() {
        one.setOnAction(e -> insertNumber(1));
        two.setOnAction(e -> insertNumber(2));
        three.setOnAction(e -> insertNumber(3));
        four.setOnAction(e -> insertNumber(4));
        five.setOnAction(e -> insertNumber(5));
        six.setOnAction(e -> insertNumber(6));
        seven.setOnAction(e -> insertNumber(7));
        eight.setOnAction(e -> insertNumber(8));
        nine.setOnAction(e -> insertNumber(9));
        zero.setOnAction(e -> insertNumber(0));
    }

    private void insertNumber(int number) {
        resultTextArea.setText(resultTextArea.getText() + number);
    }

    private void insertOperator(char operator) {
        resultTextArea.setText(resultTextArea.getText() + operator);
    }

}
