package com.IW.controllers;

import com.IW.App;
import com.IW.interfaces.IBeans.IChapter;
import com.IW.interfaces.IBeans.IPart;
import com.IW.model.dao.BookDAO;
import com.IW.model.dao.ChapterDAO;
import com.IW.model.dao.PartDAO;
import com.IW.utils.Tools;
import com.pixelduke.control.ribbon.RibbonItem;
import com.pixelduke.control.ribbon.RibbonTab;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class EditorController {

    @FXML
    private TreeTableView<String> table_parts_chapters;
    @FXML
    private TreeTableColumn<String, String> tc_part_chapter;
    @FXML
    private Button btn_controlPC;
    @FXML
    private ComboBox<IPart> cb_parts;
    @FXML
    private ComboBox<IChapter> cb_chapters;
    @FXML
    private TextArea ta_chapter;
    @FXML
    private RibbonItem chapter_item;
    @FXML
    private RibbonItem part_item;
    @FXML
    private Button btn_book_items;
    @FXML
    private RibbonTab tab_chapter;
    @FXML
    private TextArea ta_note_chapter;
    @FXML
    private TextArea ta_resume_chapter;
    @FXML
    private Button btn_save_resume;
    @FXML
    private Button btn_save_note;
    @FXML
    private Label lb_auto_save;
    @FXML
    private Button btn_save_book;
    @FXML
    private Label lb_info_save;
    @FXML
    private Button btn_open_txt;

    private static BookDAO current_book;
    private static ChapterDAO current_chapter;
    private static PartDAO current_part;
    private Timer auto_save;
    private TimerTask tt_autosave;

    @FXML
    protected void initialize() {
        createPartAndChapterIfNoOne();
        configureTreeTableAndComboBox();
        setIcons();
        setAutosave();
        //Cancelamos el auto-save cuando se cierre la ventana
        Platform.runLater(() -> btn_controlPC.getScene().getWindow().setOnCloseRequest(event -> {
            if (auto_save != null) {
                tt_autosave.cancel();
                auto_save.cancel();
            }
        }));
        //Acción del botón para ver los personajes-escenas del libro
        btn_book_items.setOnAction(event -> {
            BookItemsController.setCurrent_book(current_book);
            App.loadScene(new Stage(), "book_items", " Imagination Writer - " + current_book.getTitle(), true, false);
        });
        //Acción del botón guardar nota
        btn_save_note.setOnAction(event -> {
            if (current_chapter != null) {
                current_chapter.setNote(ta_note_chapter.getText());
                current_chapter.persist();
            }
        });
        //Acción del botón guardar resumen
        btn_save_resume.setOnAction(event -> {
            if (current_chapter != null) {
                current_chapter.setResume(ta_resume_chapter.getText());
                current_chapter.persist();
            }
        });
        //Acción del botón guardar libro
        btn_save_book.setOnAction(event -> {
            saveBook();
            lb_info_save.setText("Guardaste correctamente el libro");
            lb_auto_save.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss | dd/MM/yyyy")));
        });
        //Acción del botón abrir
        btn_open_txt.setOnAction(event -> {
            if(current_chapter!=null){
                ta_chapter.setText(Objects.requireNonNullElse(Tools.readTextFromTXTFile(), ta_chapter.getText()));
            }
        });
        //Seteamos la acción del botón para gestionar las partes-capítulos
        btn_controlPC.setOnAction(event -> {
            if(current_book!=null){
                BookPartsController.setCurrent_book(current_book);
                App.loadScene(new Stage(), "book_parts","Gestor de capítulos-partes de "+current_book.getTitle(), false, false);
            }
        });
    }

    /**
     * Con este método configuramos la TreeTable de las partes y capítulos del libro
     * Además configuramos los dos comboboxes y los bindeamos para que el de capítulos esté a la escucha del de partes y cambie sus valores
     */
    private void configureTreeTableAndComboBox() {
        tc_part_chapter.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue()));
        TreeItem<String> root = new TreeItem<>(current_book.getTitle());
        for (IPart p : current_book.getParts()) {
            if(p!=null) {
                TreeItem<String> tp = new TreeItem<>("Parte " + p.getNPart());
                root.getChildren().add(tp);
                for (IChapter c : p.getChapters()) {
                    if(c!=null) {
                        TreeItem<String> tc = new TreeItem<>("Capítulo " + c.getNPage());
                        tp.getChildren().add(tc);
                    }
                }
            }
        }
        table_parts_chapters.setRoot(root);
        cb_parts.setItems(FXCollections.observableList(current_book.getParts()));
        cb_parts.getItems().removeIf(Objects::isNull);
        cb_parts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                current_part = new PartDAO(newValue.getId());
                cb_chapters.setItems(FXCollections.observableList(newValue.getChapters()));
                cb_chapters.getItems().removeIf(Objects::isNull);
            }
        });
        cb_chapters.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                current_chapter = new ChapterDAO(newValue.getId());
                if (ta_chapter.isDisabled() && tab_chapter.isDisabled() && ta_resume_chapter.isDisabled()) {
                    ta_chapter.setDisable(false);
                    tab_chapter.setDisable(false);
                    ta_resume_chapter.setDisable(false);
                    ta_note_chapter.setDisable(false);
                    btn_save_resume.setDisable(false);
                    btn_save_note.setDisable(false);
                }
                ta_note_chapter.setText(current_chapter.getNote());
                ta_resume_chapter.setText(current_chapter.getResume());
                ta_chapter.setText(current_chapter.getText());
            }
        });
    }

    /**
     * Añadimos un auto-save cada 30 segundos
     */
    private void setAutosave() {
        auto_save = new Timer();
        tt_autosave = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    saveBook();
                    lb_info_save.setText("Último autoguardado");
                    lb_auto_save.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss | dd/MM/yyyy")));
                });
            }
        };
        Platform.runLater(() -> auto_save.schedule(tt_autosave, 0, 30000));
    }

    /**
     * Seteamos los iconos
     */
    private void setIcons() {
        btn_controlPC.setGraphic(Tools.getIcon("list-cp"));
        chapter_item.setGraphic(Tools.getIcon("book_chapter"));
        btn_book_items.setGraphic(Tools.getIcon("book_scenes"));
        part_item.setGraphic(Tools.getIcon("book_part"));
    }

    /**
     * This is called at the first time and add part 1 and chapter 1 to the book
     */
    private void createPartAndChapterIfNoOne() {
        if (current_book.getParts().size() == 0) {
            PartDAO p = new PartDAO();
            p.setNPart(1);
            p.persist();
            ChapterDAO c = new ChapterDAO();
            c.setNPage(1);
            c.persist();
            p.getChapters().add(c);
            p.persist();
            current_book.getParts().add(p);
            current_book.persist();
        }
    }

    /**
     * This method need to be called when we want to save book information
     * Sincronizamos los objetos para que no haya solapamiento de persistencia entre el auto-save y el boton de guardar
     */
    private void saveBook() {
        if (current_chapter != null) {
            synchronized (current_chapter) {
                current_chapter.setText(ta_chapter.getText());
                current_chapter.setResume(ta_resume_chapter.getText());
                current_chapter.setNote(ta_note_chapter.getText());
                current_chapter.setPart(current_part);
                current_chapter.persist();
            }
        }
        if (current_part != null) {
            synchronized (current_part) {
                if(!current_part.getChapters().contains(current_chapter)){
                    current_part.getChapters().add(current_chapter);
                }
                current_part.setBook(current_book);
                current_part.persist();
            }
        }
        if (current_book != null) {
            synchronized (current_book) {
                if(!current_book.getParts().contains(current_part)){
                    current_book.getParts().add(current_part);
                }
            }
        }
    }

    public static void setBook(BookDAO book) {
        current_book = book;
        current_part = null;
        current_chapter = null;
    }
}
