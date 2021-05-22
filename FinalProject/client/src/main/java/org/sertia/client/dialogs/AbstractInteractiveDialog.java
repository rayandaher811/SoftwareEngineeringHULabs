package org.sertia.client.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;

import java.util.Collection;
import java.util.HashMap;

public abstract class AbstractInteractiveDialog<InputType, OutputType> {
    private String dialogTitle;
    private String dialogHeaderText;
    private Collection<ButtonType> buttons;
    private GridPane grid;
    private HashMap<TextInputControl, String> inputControlComponentToDisplayValue;
}
