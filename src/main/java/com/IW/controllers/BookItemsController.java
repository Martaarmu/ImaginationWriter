package com.IW.controllers;

import com.IW.interfaces.IBeans.ICharacter;
import com.IW.interfaces.IBeans.IScene;
import com.IW.model.dao.BookDAO;
import com.IW.model.dao.CharacterDAO;
import com.IW.model.dao.SceneDAO;
import com.IW.utils.Dialog;
import com.IW.utils.Tools;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class BookItemsController {

    @FXML
    private TableView<ICharacter> table_items_characters;
    @FXML
    private TableColumn<ICharacter, String> tc_items_characters;
    @FXML
    private ImageView iview_photo;
    @FXML
    private Label lb_description;
    @FXML
    private TableView<IScene> table_items_scenes;
    @FXML
    private TableColumn<IScene, String> tc_items_scenes;
    @FXML
    private Button btn_add_character;
    @FXML
    private Button btn_edit_character;
    @FXML
    private Button btn_delete_character;
    @FXML
    private Button btn_add_scene;
    @FXML
    private Button btn_edit_scene;
    @FXML
    private Button btn_delete_scene;

    private static BookDAO current_book;

    private static final String default_photo_scene = "assets/user_default.png";
    private static final String default_photo_character = "assets/user_default.png";

    @FXML
    protected void initialize() {
        configureTables();
        table_items_characters.setItems(FXCollections.observableList(current_book.getCharacters()));
        table_items_scenes.setItems(FXCollections.observableList(current_book.getScenes()));
        btn_add_character.setOnAction(event -> {
            CharacterDAO c = new CharacterDAO();
            c.setName(Dialog.showDialogString("Nombre del nuevo personaje", "Nombre personaje", "Introduce el nuevo nombre del personaje"));
            c.setDescription(Dialog.showDialogString("Descripción del nuevo personaje", "Descripción personaje", "Introduce descripción del personaje"));
            String cover = Dialog.showDialogExamine("Foto personaje", "Selecciona el personaje", "Introduce la URL de la foto del personaje");
            if (c.getName() != null) {
                if (!cover.equals("")) {
                    if (Tools.FileCopy(cover, "assets/books_characters/" + c.getName() + cover.substring(cover.lastIndexOf(".")))) {
                        Dialog.showInformation("", "Exito al copiar la foto", "Hemos guardado una copia de tu foto en otra carpeta!");
                        c.setPhoto("assets/books_characters/" + c.getName() + cover.substring(cover.lastIndexOf(".")));
                    }
                }
                c.setBook(current_book);
                c.persist();
                table_items_characters.getItems().add(c);
            }
        });
        btn_edit_character.setOnAction(event -> {
            if (table_items_characters.getSelectionModel().getSelectedItem() != null) {
                CharacterDAO c = new CharacterDAO(table_items_characters.getSelectionModel().getSelectedItem().getId());
                c.setName(Dialog.showDialogString("Nombre del nuevo personaje", "Nombre personaje", c.getName()));
                c.setDescription(Dialog.showDialogString("Descripción del nuevo personaje", "Descripción personaje", c.getDescription()));
                String cover = Dialog.showDialogExamine("Foto personaje", "Selecciona el personaje", c.getPhoto());
                if (c.getName() != null) {
                    if (!cover.equals("")) {
                        if (Tools.FileCopy(cover, "assets/books_characters/" + c.getName() + cover.substring(cover.lastIndexOf(".")))) {
                            Dialog.showInformation("", "Exito al copiar la foto", "Hemos guardado una copia de tu foto en otra carpeta!");
                            c.setPhoto("assets/books_characters/" + c.getName() + cover.substring(cover.lastIndexOf(".")));
                        }
                    }
                    c.setBook(current_book);
                    c.persist();
                    table_items_characters.getItems().remove(c);
                    table_items_characters.getItems().add(c);
                }
            }

        });
        btn_delete_character.setOnAction(event -> {
            if (table_items_characters.getSelectionModel().getSelectedItem() != null) {
                CharacterDAO c = new CharacterDAO(table_items_characters.getSelectionModel().getSelectedItem().getId());
                c.remove();
                table_items_characters.getItems().remove(c);
            }
        });
        btn_add_scene.setOnAction(event -> {
            SceneDAO s = new SceneDAO();
            s.setTitle(Dialog.showDialogString("Título de la nueva escena", "Título escena", "Introduce el titulo de la nueva escena"));
            s.setDescription(Dialog.showDialogString("Descripción la nueva escena", "Descripción escena", "Introduce descripci󮠤e la escena"));
            String cover = Dialog.showDialogExamine("Foto escena", "Selecciona escena", "Introduce la URL de la foto de la escena");
            if (s.getTitle() != null) {
                if (!cover.equals("")) {
                    if (Tools.FileCopy(cover, "assets/books_scenes/" + s.getTitle() + cover.substring(cover.lastIndexOf(".")))) {
                        Dialog.showInformation("", "Exito al copiar la foto", "Hemos guardado una copia de tu foto en otra carpeta!");
                        s.setPhoto("assets/books_scenes/" + s.getTitle() + cover.substring(cover.lastIndexOf(".")));
                    }
                }
                s.setBook(current_book);
                s.persist();
                table_items_scenes.getItems().add(s);

            }
        });
        btn_edit_scene.setOnAction(event -> {
            if (table_items_scenes.getSelectionModel().getSelectedItem() != null) {
                SceneDAO s = new SceneDAO(table_items_scenes.getSelectionModel().getSelectedItem().getId());
                s.setTitle(Dialog.showDialogString("Título de la nueva escena", "Título escena", s.getTitle()));
                s.setDescription(Dialog.showDialogString("Descripción la escena", "Descripción la escena", s.getDescription()));
                String cover = Dialog.showDialogExamine("Foto escena", "Selecciona la foto de la escena", s.getPhoto());
                if (s.getTitle() != null) {
                    if (!cover.equals("")) {
                        if (Tools.FileCopy(cover, "assets/books_scenes/" + s.getTitle() + cover.substring(cover.lastIndexOf(".")))) {
                            Dialog.showInformation("", "Exito al copiar la foto", "Hemos guardado una copia de tu foto en otra carpeta!");
                            s.setPhoto("assets/books_scenes/" + s.getTitle() + cover.substring(cover.lastIndexOf(".")));
                        }
                    }
                    s.setBook(current_book);
                    s.persist();
                    table_items_scenes.getItems().remove(s);
                    table_items_scenes.getItems().add(s);
                }
            }
        });
        btn_delete_scene.setOnAction(event -> {
            if (table_items_scenes.getSelectionModel().getSelectedItem() != null) {
                SceneDAO s = new SceneDAO(table_items_scenes.getSelectionModel().getSelectedItem().getId());
                s.remove();
                table_items_scenes.getItems().remove(s);
            }
        });
        setIcons();
    }

    /**
     * Configuramos las tablas y añadimos los listener para cambiar los datos conforme hacemos click en los elementos de las tablas
     */
    private void configureTables() {
        tc_items_scenes.setCellValueFactory(eachScene -> new SimpleStringProperty(eachScene.getValue().getTitle()));
        tc_items_characters.setCellValueFactory(eachCharacter -> new SimpleStringProperty(eachCharacter.getValue().getName()));
        table_items_scenes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (btn_edit_scene.isDisabled() && btn_delete_scene.isDisabled()) {
                    btn_edit_scene.setDisable(false);
                    btn_delete_scene.setDisable(false);
                }
                lb_description.setText(newValue.getDescription());
                iview_photo.setImage(Objects.requireNonNullElseGet(Tools.getImage(newValue.getPhoto(), false), () -> Tools.getImage(default_photo_scene, true)));
            }
        });
        table_items_characters.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (btn_edit_character.isDisabled() && btn_delete_character.isDisabled()) {
                    btn_edit_character.setDisable(false);
                    btn_delete_character.setDisable(false);
                }
                lb_description.setText(newValue.getDescription());
                iview_photo.setImage(Objects.requireNonNullElseGet(Tools.getImage(newValue.getPhoto(), false), () -> Tools.getImage(default_photo_character, true)));
            }
        });
    }

    /**
     * Este método sirve para cuando llamamos a la vista, que el libro no esté a null y puedan cargar los elementos correspondientes de la vista con el controlador
     * @param book
     */
    public static void setCurrent_book(BookDAO book) {
        current_book = book;
    }

    /**
     * Seteamos los iconos con este método
     */
    private void setIcons() {
        btn_add_character.setGraphic(Tools.getIcon("add"));
        btn_edit_character.setGraphic(Tools.getIcon("edit"));
        btn_delete_character.setGraphic(Tools.getIcon("remove"));
        btn_add_scene.setGraphic(Tools.getIcon("add"));
        btn_edit_scene.setGraphic(Tools.getIcon("edit"));
        btn_delete_scene.setGraphic(Tools.getIcon("remove"));
    }
}