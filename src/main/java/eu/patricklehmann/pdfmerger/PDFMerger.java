package eu.patricklehmann.pdfmerger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Leeman on 04.07.2017
 */
public class PDFMerger extends Application {

    public static PDFMerger _instance;


    private FileChooser fileChooser;
    private Stage primStage;


    public PDFMerger() {
        _instance = this;
    }

    /**
     * @param args Startparameter
     *             <p>
     *             Eigentlich brauche ich nicht sicherzustellen, dass es ein Singleton ist, da ich aber faul war/bin, ich aber
     *             KEINE statische Funktion erstellen wollte, hab ich eben ne statische Instanz gemacht.
     *             (Und dann immerhin geschaut, dass es nur einmal passiert auch wenn ich eigentlich noch ne Fehler-
     *             Melung werfen sollte, falls es schon existiert
     *
     *             und ruft launch-Funktion auf um die JavaFX GUI darzustellen
     */
    public static void main(String[] args) {
        if (_instance == null)
            new PDFMerger();
        launch(args);
    }

    // Setup/Einstiegspunkt der GUI
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/gui.fxml"));
        Scene scene = new Scene(root);
        this.primStage = primaryStage;

        // Das FileChooser Fenster vorbereiten
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("Wähle die zu mergenden PDFs");
        this.fileChooser.setInitialFileName("pdf");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        this.fileChooser.getExtensionFilters().add(extFilter);

        primaryStage.setTitle("PDF Merger v 0.1");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();


        primaryStage.show();
    }

    // Das FileChoose Fenster darstellen
    public List<File> openFileDialogue() {
        return this.fileChooser.showOpenMultipleDialog(primStage);
    }

    // Die ausgewählten PDF Dateien mergen
    public void merge(File[] files, String title) throws IOException {

        // Hier den Pfad ermitteln in der die Jar-Datei liegt um dort die neue PDF-Datei anzulegen
        String path = new File(PDFMerger.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();

        // Eine Instanz der PDFMergerUtility erstellen um gleich die Dateien aneinander zu reihen
        PDFMergerUtility ut = new PDFMergerUtility();
        // Ein neues, leeres PDF-Dokument erstellen
        PDDocument targetDoc = new PDDocument();

        // Jede einzelne Datei die übergeben wurde an das leere Dokument anhängen
        for (File f : files) {
            PDDocument fil = PDDocument.load(f);
            try {
                ut.appendDocument(targetDoc, fil);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fil.close();
            }
        }

        try {
            // Die neue, zusammengefügte PDF Datei speichern
            targetDoc.save(path + "\\" + title + ".pdf");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Den Stream schließen
            // This will close the underlying COSDocument object.
            targetDoc.close();
        }

    }
}
