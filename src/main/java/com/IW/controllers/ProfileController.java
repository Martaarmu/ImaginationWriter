package com.IW.controllers;

import com.IW.App;
import com.IW.model.dao.AuthorDAO;
import com.IW.utils.Dialog;
import com.IW.utils.Tools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Objects;

public class ProfileController {
    @FXML
    private ImageView iview_profile;
    @FXML
    private TextField tf_name;
    @FXML
    private PasswordField tf_passwd;
    @FXML
    private Button btn_save;
    @FXML
    private Button lock;
    @FXML
    private Button btn_photo;
    private static final String default_photo = "assets/user_default.png";
    private static String photo = "";

    private static AuthorDAO actual_author;

    @FXML
    protected void initialize() {
        lock.setGraphic(Tools.getIcon("lock-locked"));
        if (actual_author.getId() == -1) {
            lock.setVisible(false);
            btn_save.setText("Registrarse");
        } else {
            tf_name.setDisable(true);
            tf_passwd.setDisable(true);
            iview_profile.setDisable(true);
            btn_photo.setDisable(true);
        }
        iview_profile.setImage(Objects.requireNonNullElseGet(Tools.getImage(actual_author.getPhoto(), false), () -> Tools.getImage(default_photo, true)));
        tf_name.setText(actual_author.getName());
        tf_name.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) btn_save.fire();
        });
        tf_passwd.setText(actual_author.getPassword());
        tf_passwd.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) btn_save.fire();
        });
        //Acción del botón Cambiar Foto
        btn_photo.setOnAction(event -> {
            photo = Tools.selectImageFile();
            if (photo == null) {
                iview_profile.setImage(Tools.getImage(default_photo, true));
            } else {
                iview_profile.setImage(Tools.getImage(photo, false));
            }
        });
        //Acción del botón desbloquear
        lock.setOnAction(event -> {
            if (Dialog.showConfirmation("¿Desbloquear?", "¿Desea realizar cambios en su perfil?", "")) {
                AuthorDAO a = new AuthorDAO();
                a.setName(tf_name.getText());
                a.setPassword(Dialog.showDialogString("Cuidado", "Para verificar que es ud, le pedimos otra vez la contraseña", "Introduzca su contraseña actual"));
                if (a.getPassword() != null && !a.getPassword().equals("")) {
                    if (a.checkUser() && a.equals(actual_author)) {
                        tf_passwd.clear();
                        tf_name.setDisable(false);
                        tf_passwd.setDisable(false);
                        iview_profile.setDisable(false);
                        btn_photo.setDisable(false);
                        lock.setGraphic(Tools.getIcon("lock-unlocked"));
                    } else {
                        Dialog.showError("Error", "Error en la contraseña", "No coinciden las contraseñas");
                    }
                }
            } else {
                tf_name.setDisable(true);
                tf_passwd.setDisable(true);
                iview_profile.setDisable(true);
                btn_photo.setDisable(true);
                lock.setGraphic(Tools.getIcon("lock-locked"));
            }
        });
        //Acción del botón guardar
        btn_save.setOnAction(event -> {
            if (!tf_name.getText().trim().equals("") && !tf_passwd.getText().trim().equals("")) {
                actual_author.setName(tf_name.getText().replace("\n", ""));
                actual_author.setPassword(tf_passwd.getText().replace("\n", ""));
                if (!photo.equals("")) {
                    actual_author.setPhoto(photo);
                    if (Tools.FileCopy(photo, "assets/profile_photo/" + actual_author.getName() + photo.substring(photo.lastIndexOf(".")))) {
                        Dialog.showInformation("", "Exito al copiar la foto", "Hemos guardado una copia de tu foto en otra carpeta!");
                        actual_author.setPhoto("assets/profile_photo/" + actual_author.getName() + photo.substring(photo.lastIndexOf(".")));
                    }
                }
                actual_author.persist();
                if (btn_save.getText().equals("Registrarse")) {
                    Dialog.showInformation("¡Éxito!", "Te has registrado correctamente", "Te devolvemos a la página de Login, ahora puedes iniciar sesión con tus credenciales!");
                } else {
                    Dialog.showInformation("¡Éxito!", "Tus datos se han actualizado", "");
                }
                App.closeScene((Stage) tf_name.getScene().getWindow());
            }
        });
    }

    public static void setActual_author(AuthorDAO a) {
        actual_author = a;
    }
}
