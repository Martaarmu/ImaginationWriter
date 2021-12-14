package com.IW.utils;

import com.IW.interfaces.IBeans.IBook;
import com.IW.interfaces.IBeans.IChapter;
import com.IW.interfaces.IBeans.IPart;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.Objects;
import java.util.Optional;

/**
 * This class is used to show different Dialog popups to the user (GUI)
 */
public class Dialog {
    public static void showError(String title, String header, String description) {
        showDialog(Alert.AlertType.ERROR, title, header, description);
    }

    public static void showWarning(String title, String header, String description) {
        showDialog(Alert.AlertType.WARNING, title, header, description);
    }

    public static void showInformation(String title, String header, String description) {
        showDialog(Alert.AlertType.INFORMATION, title, header, description);
    }

    public static boolean showConfirmation(String title, String header, String description) {
        return showDialogBoolean(title, header, description);
    }

    private static void showDialog(Alert.AlertType type, String title, String header, String description) {
        Alert alert = new Alert(type);
        Tools.addCssAndIcon((Stage) alert.getDialogPane().getScene().getWindow());
        new JMetro(Style.DARK).setScene(alert.getDialogPane().getScene());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
    }

    private static boolean showDialogBoolean(String title, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Tools.addCssAndIcon((Stage) alert.getDialogPane().getScene().getWindow());
        new JMetro(Style.DARK).setScene(alert.getDialogPane().getScene());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
        return alert.getResult().getButtonData().isDefaultButton();
    }

    /**
     * This dialog shows a textfield and return the text that user puts in
     *
     * @param title       title of the dialog
     * @param header      the content of the dialog
     * @param description the description of the dialog
     * @return null if closed or press Cancel, otherwhise the string that user input
     */
    public static String showDialogString(String title, String header, String description) {
        TextInputDialog dialog = new TextInputDialog("");
        Tools.addCssAndIcon((Stage) dialog.getDialogPane().getScene().getWindow());
        new JMetro(Style.DARK).setScene(dialog.getDialogPane().getScene());
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(description);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * This dialog shows a textfield with only double values
     *
     * @param title       title of the dialog
     * @param header      the content of the dialog
     * @param description the description of the dialog
     * @return if null, returns -1, else the user input value of the textfield
     */
    public static int showDialogPart_Chapter(String title, String header, String description) {
        TextInputDialog dialog = new TextInputDialog();
        Tools.addCssAndIcon((Stage) dialog.getDialogPane().getScene().getWindow());
        new JMetro(Style.DARK).setScene(dialog.getDialogPane().getScene());
        Tools.onlyDoubleValue(dialog.getEditor());
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(description);
        Optional<String> result = dialog.showAndWait();
        return Integer.parseInt(result.orElse("-1"));
    }

    /**
     * This dialog shows a textfield with a button to examine and return the text that are in textfield
     *
     * @param title       title of the dialog
     * @param header      the content of the dialog
     * @param description the description of the dialog
     * @return null if closed or press Cancel, otherwhise the string the text on textfield
     */
    public static String showDialogExamine(String title, String header, String description) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        Tools.addCssAndIcon((Stage) dialog.getDialogPane().getScene().getWindow());
        new JMetro(Style.DARK).setScene(dialog.getDialogPane().getScene());
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(description);
        TextField tf_url = new TextField();
        tf_url.setPromptText("URL de la Imagen");
        Button examine = new Button("Examinar");
        examine.setOnAction(event -> {
            tf_url.setText(Tools.selectImageFile());
        });
        GridPane p = new GridPane();
        p.addColumn(0, examine);
        p.addColumn(1, tf_url);
        p.hgapProperty().setValue(5);
        dialog.getDialogPane().setContent(p);
        dialog.showAndWait();
        return tf_url.getText();
    }

    /**
     * This dialog (appears) to print a book with his pages TODO layout text inside PDF(?
     *
     * @param bookToPrint book to print to PDF
     */
    public static void printDialog(IBook bookToPrint) {
        ChoiceDialog<Printer> dialog = new ChoiceDialog<>(Printer.getDefaultPrinter(), Printer.getAllPrinters());
        Tools.addCssAndIcon((Stage) dialog.getDialogPane().getScene().getWindow());
        new JMetro(Style.DARK).setScene(dialog.getDialogPane().getScene());
        dialog.setTitle("Selección de impresora");
        dialog.setHeaderText("Elige una impresora");
        dialog.setContentText("¡Elige una impresora que imprima en PDF!");
        Optional<Printer> opt = dialog.showAndWait();
        if (opt.isPresent()) {
            Printer printer = opt.get();
            PrinterJob pj = PrinterJob.createPrinterJob(printer);
            bookToPrint.getParts().removeIf(Objects::isNull);
            for (IPart p : bookToPrint.getParts()) {
                p.getChapters().removeIf(Objects::isNull);
                for (IChapter c : p.getChapters()) {
                    //TODO maquetar un poco mejor el texto(?
                    GridPane g = new GridPane();
                    Label text = new Label();
                    text.setText(c.getText());
                    g.addColumn(0, text);
                    pj.printPage(g);
                }
            }
            pj.endJob();
            showInformation("¡Exportado con éxito!", "PDF Guardado", "Hemos guardado con éxito el libro");
        }
    }
}
