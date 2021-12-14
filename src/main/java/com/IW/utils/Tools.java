package com.IW.utils;

import com.IW.App;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.styles.jmetro.MDL2IconFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class Tools {

    private static final Logger logger = LoggerFactory.getLogger(Tools.class);
    private static final String URL_IMG_EXPRESSION = "(http)?s?:?(\\/\\/[^\"']*\\.(?:bmp|gif|jpg|jpeg|png))";
    private static final String DOUBLE_EXPRESSION_TF = "-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?";
    public static final Image default_photo_cover = getImage("icon.png", true);

    /**
     * This method is used to set the icon and css
     *
     * @param window window to need apply styles
     */
    public static void addCssAndIcon(Stage window) {
        window.getScene().getStylesheets().add(String.valueOf(App.class.getResource("dark.css")));
        window.getIcons().add(getImage("icon.png", true));
    }

    /**
     * This method is used to validate an url from internet
     *
     * @param url url to check
     * @return true if ends with jpg, gif, or png, otherwhise, false
     */
    private static boolean Validate_img_URL(String url) {
        return Pattern.compile(URL_IMG_EXPRESSION).matcher(url).matches();
    }

    private static boolean Validate_Double_Value(String value) {
        return Pattern.compile(DOUBLE_EXPRESSION_TF).matcher(value).matches();
    }

    /**
     * This method is used to validate an url from internet
     *
     * @param url url to check
     * @return true if ends with jpg, gif, or png, otherwhise, false
     */
    private static boolean ValidateFile_img(String url) {
        boolean result = switch (url.toLowerCase().substring(url.length() - 4, url.length())) {
            case ".bmp", ".gif", ".jpg", ".png" -> true;
            default -> false;
        };
        if (!result) {
            if (url.endsWith(".jpeg")) {
                result = true;
            }
        }
        return result;
    }

    /**
     * This util is used to get an Image
     *
     * @param resPath   relative or absolute path folder, like "foo/bar.png" or "/home/user/foo.png" or "C:\Users\User\Desktop\bar.png"
     * @param isResPath indicate if the source is on resPath(jar) or not
     * @return the Image from the resources loaded
     */
    public static Image getImage(String resPath, boolean isResPath) {
        if (resPath == null)
            return null;
        if (isResPath)
            return new Image(Objects.requireNonNull(App.class.getResourceAsStream(resPath)));
        else {
            if (!Validate_img_URL(resPath)) {
                File f = new File(resPath);
                if (f.exists() && f.isFile())
                    if (ValidateFile_img(f.getName()))
                        return new Image(Objects.requireNonNull(f.getAbsoluteFile().getAbsolutePath()));
            } else if (Validate_img_URL(resPath)) {
                return new Image(resPath);
            }
            return null;
        }
    }

    /**
     * This method is used to encrypt Strings with MD5 hash
     *
     * @param s String that needs to hash
     * @return hashed MD5 string
     */
    public static String encryptSHA256(String s) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA256");
            md.update(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : md.digest()) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("no Provider supports a MessageDigestSpi implementation for the specified algorithm" + e.getMessage());
        }
        return result;
    }

    /**
     * This method is used to copy sources to a .jar path level
     *
     * @param source source file in
     * @param target target file out
     * @return true if success
     */
    public static boolean FileCopy(String source, String target) {
        boolean result;
        try {
            File f = new File(target);
            if (!Files.exists(f.toPath())) {
                Files.createDirectories(f.toPath());
            }
            Files.copy(Paths.get(source), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
            result = true;
        } catch (IOException e) {
            logger.error("Error al copiar ficheros, con el mensaje:\n" + e.getMessage());
            result = false;
        }
        return result;
    }

    /**
     * This method retrieves the URI absolute path of a file selected on a SYSTEM window
     *
     * @return the absolute path of the file image
     */
    public static String selectImageFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.bmp", "*.gif", "*.jpg", "*.jpeg", "*.png"));
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else {
            Dialog.showWarning("Advertencia", "", "Fichero no seleccionado!");
            return null;
        }
    }

    /**
     * This method is used to get Text from a txt file
     *
     * @return String with the whole text from the file, null if error happens
     */
    public static String readTextFromTXTFile() {
        String result = null;
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            BufferedReader br = null;
            FileReader fr = null;
            try {
                fr = new FileReader(selectedFile.getAbsolutePath());
                br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                result = sb.toString();
            } catch (FileNotFoundException e) {
                logger.error("Error, fichero no encontrado");
            } catch (IOException e) {
                logger.error("");
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("Error al intentar cerrar el recurso de BufferedReader");
                }
                try {
                    fr.close();
                } catch (IOException e) {
                    logger.error("Error al intentar cerrar el recurso de FileReader");
                }
            }
        }
        return result;
    }

    /**
     * This method is used to only write double values on a TextField
     *
     * @param tf TextField to update
     */
    public static void onlyDoubleValue(TextField tf) {
        UnaryOperator<TextFormatter.Change> filter = c -> Validate_Double_Value(c.getControlNewText()) ? c : null;
        StringConverter<Double> converter = new StringConverter<>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0;
                } else {
                    return Double.valueOf(s);
                }
            }

            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };
        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);
        tf.setTextFormatter(textFormatter);
    }

    /**
     * This method is used to get icons and put them into a window, making less code and more readable to coders
     * Codes are extracted from Microsoft official page -> https://docs.microsoft.com/en-us/windows/apps/design/style/segoe-ui-symbol-font
     */
    //Para añadir nuevos iconos consultar la página de Microsoft-> https://docs.microsoft.com/en-us/windows/apps/design/style/segoe-ui-symbol-font
    //Añadir "\\u" al principio (al igual que el \n imprime salto de línea) para indicar que es un icono gráfico de Windows.
    public static MDL2IconFont getIcon(String value) {
        MDL2IconFont result = switch (value) {
            case "close" -> new MDL2IconFont("\uE711");
            case "cloud" -> {
                result = new MDL2IconFont("\uE753");
                result.setStyle("-fx-text-fill: #00a8a0;");
                yield result;
            }
            case "info" -> {
                result = new MDL2IconFont("\uE946");
                result.setStyle("-fx-text-fill: lightblue;");
                yield result;
            }
            case "arrow-next" -> {
                result = new MDL2IconFont("\uF0D3");
                result.setStyle("-fx-text-fill: lightblue;");
                yield result;
            }
            case "arrow-back" -> {
                result = new MDL2IconFont("\uF0D2");
                result.setStyle("-fx-text-fill: lightblue;");
                yield result;
            }
            case "add" -> {
                result = new MDL2IconFont("\uECC8");
                result.setStyle("-fx-text-fill: lightgreen;");
                yield result;
            }
            case "remove" -> {
                result = new MDL2IconFont("\uECC9");
                result.setStyle("-fx-text-fill: red;");
                yield result;
            }
            case "edit" -> {
                result = new MDL2IconFont("\uEC87");
                result.setStyle("-fx-text-fill: orange;");
                yield result;
            }
            case "close-session" -> {
                result = new MDL2IconFont("\uF3B1");
                yield result;
            }
            case "book_chars" -> {
                result = new MDL2IconFont("\uE726");
                yield result;
            }
            case "book_scenes" -> {
                result = new MDL2IconFont("\uE91B");
                yield result;
            }
            case "profile" -> {
                result = new MDL2IconFont("\uE77B");
                yield result;
            }
            case "lock-locked" -> {
                result = new MDL2IconFont("\uE72E");
                yield result;
            }
            case "lock-unlocked" -> {
                result = new MDL2IconFont("\uE785");
                yield result;
            }
            case "list-cp" -> {
                result = new MDL2IconFont("\uE71D");
                yield result;
            }
            case "book_chapter" -> {
                result = new MDL2IconFont("\uE736");
                yield result;
            }
            case "book_part" -> {
                result = new MDL2IconFont("\uE74C");
                yield result;
            }
            case "download" -> {
                result = new MDL2IconFont("\uE896");
                result.setStyle("-fx-text-fill: lightgreen;");
                yield result;
            }
            case "upload" -> {
                result = new MDL2IconFont("\uE898");
                result.setStyle("-fx-text-fill: lightgreen;");
                yield result;
            }
            case "print" -> {
                result = new MDL2IconFont("\uEA90");
                result.setStyle("-fx-text-fill: red;");
                yield result;
            }
            default -> new MDL2IconFont("\uF16B");
        };
        return result;
    }
}
