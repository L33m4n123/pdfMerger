package eu.patricklehmann.pdfmerger.gui;

import eu.patricklehmann.pdfmerger.PDFMerger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class GUIController {

    private final ObservableList names = FXCollections.observableArrayList();
    private File[] fileList;

    @FXML
    private TextField pdf_title;
    @FXML
    private ListView files;
    @FXML
    protected void handleQuitButtonAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    protected void handleHelpButtonAction(ActionEvent event) {

    }

    @FXML
    protected void handleMergeButtonAction(ActionEvent event) {
        try {
            PDFMerger._instance.merge(this.fileList, this.pdf_title.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleLoadButtonAction(ActionEvent event) {
        // Die Dateien aus dem FileChooser Fenster hier in einer Liste zwischenspeichern
        // um es dann in ein Array umzuwandeln (für den späteren Verlauf ist ein Array einfacher)
        List<File> tempList = PDFMerger._instance.openFileDialogue();
        if (tempList != null) {
            this.fileList = new File[tempList.size()];
            tempList.toArray(this.fileList);
            for (File f : this.fileList) {
                // Jede Datei in die "Observable List" hinzufügen um es auf der GUI darzustellen
                this.names.add(f.getName());
            }
            // Die ObservableList dem GUI Element hinzufügen
            this.files.setItems(names);
        }
    }

    // Funktion für den "Nach Oben" Knopf
    @FXML
    protected void handleUpButtonAction(ActionEvent event) {
        // Anschauen, welche Datei ausgewählt wurde
        int index = this.files.getSelectionModel().getSelectedIndex();
        // Falls es die oberste/erste Datei ist, beim Drücken des Hoch-Knopfes nichts tun
        if (index > 0) {
            swapElements(index, index - 1);
            this.files.setItems(this.names);
        }
    }

    // Funktion für den "Nach Unten" Knopf
    @FXML
    protected void handleDownButtonAction(ActionEvent event) {
        // Anschauen, welche Datei ausgewählt wurde
        int index = this.files.getSelectionModel().getSelectedIndex();
        // Falls es die unterste/letzte Datei ist, beim Drücken des Runter-Knopfes nichts tun
        if (index < this.files.getItems().size() - 1) {
            swapElements(index, index + 1);
            this.files.setItems(this.names);
        }
    }

    // Funktion für den "Löschen" Knopf
    @FXML
    protected void handleDeleteButton(ActionEvent event) {
        // Die ausgewählte Datei sowohl aus der Fileliste in Memory löschen als auch aus der GUI
        int index = this.files.getSelectionModel().getSelectedIndex();
        this.fileList = (File[]) ArrayUtils.remove(this.fileList, index);
        names.remove(index);
    }

    // Es tauscht zwei Elemente der Fileliste in Memory und auf der GUI aus
    private void swapElements(int selected, int toSwapWith) {
        Object temp = this.names.get(toSwapWith);
        this.names.set(toSwapWith, this.names.get(selected));
        this.names.set(selected, temp);
        File tempFile = this.fileList[toSwapWith];
        this.fileList[toSwapWith] = this.fileList[selected];
        this.fileList[selected] = tempFile;
        this.files.getSelectionModel().select(toSwapWith);
    }
}
