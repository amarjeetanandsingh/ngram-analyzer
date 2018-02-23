package ngramanalyzer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import in.amarjeet.ngramanalyzer.NGramAnalyzerApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class AnalyzerController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtTargetFolder;

	@FXML
	private Button btnSelectFolder;

	@FXML
	private TextArea txtConsole;

	@FXML
	private Button btnClearConsole;

	@FXML
	private TextField txtFreqThreshold;

	@FXML
	private Button btnCopyConsole;

	@FXML
	private Button btnGenerateWordList;

	@FXML
	private Button btnOpenResult;

	private PrintStream ps;

	@FXML
	void chooseFolder(ActionEvent event) {
		Node source = (Node) event.getSource();
		Window theStage = source.getScene().getWindow();

		DirectoryChooser chooser = new DirectoryChooser();
		File dir = chooser.showDialog(theStage);
		if (dir != null) {
			txtTargetFolder.setText(dir.getAbsolutePath());
		}
	}

	@FXML
	void generateWordList(ActionEvent event) {
		if(txtTargetFolder.getText() != null || !txtTargetFolder.getText().equals("")){
			Thread thread = new Thread(()-> new NGramAnalyzerApp(txtTargetFolder.getText()).generateWordList());
			thread.start();
			btnOpenResult.setDisable(false);
		}
	}
	

	@FXML
	void openResult(ActionEvent event) {
		Desktop dt = Desktop.getDesktop();
        try {
            dt.open( Paths.get(txtTargetFolder.getText(), "word_list.txt").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnOpenResult.setDisable(true);
	}

	@FXML
	void copyConsole(ActionEvent event) {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(txtConsole.getText());
		clipboard.setContent(content);
	}

	@FXML
	void clearConsole(ActionEvent event) {
		txtConsole.setText("");
	}

	@FXML
	void initialize() {
		assert txtTargetFolder != null : "fx:id=\"txtTargetFolder\" was not injected: check your FXML file 'Analyzer.fxml'.";
		assert btnSelectFolder != null : "fx:id=\"btnSelectFolder\" was not injected: check your FXML file 'Analyzer.fxml'.";
		assert txtConsole != null : "fx:id=\"txtConsole\" was not injected: check your FXML file 'Analyzer.fxml'.";
		assert btnClearConsole != null : "fx:id=\"btnClearConsole\" was not injected: check your FXML file 'Analyzer.fxml'.";
		assert txtFreqThreshold != null : "fx:id=\"txtFreqThreshold\" was not injected: check your FXML file 'Analyzer.fxml'.";
		assert btnCopyConsole != null : "fx:id=\"btnCopyConsole\" was not injected: check your FXML file 'Analyzer.fxml'.";
		assert btnGenerateWordList != null : "fx:id=\"btnGenerateWordList\" was not injected: check your FXML file 'Analyzer.fxml'.";
		assert btnOpenResult != null : "fx:id=\"btnOpenResult\" was not injected: check your FXML file 'Analyzer.fxml'.";

		ps = new PrintStream(new Console(txtConsole));
		System.setOut(ps);
		System.setErr(ps);
		
		// add handler
		btnSelectFolder.setOnAction(this::chooseFolder);
		btnClearConsole.setOnAction(this::clearConsole);
		txtFreqThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(!newValue.matches("^[1-9]+[0-9]*$")){
		    	txtFreqThreshold.setText(oldValue);
		    }
		});
		}
}

class Console extends OutputStream {
	private TextArea console;

	public Console(TextArea console) {
		this.console = console;
	}

	public void appendText(String valueOf) {
		Platform.runLater(() -> console.appendText(valueOf));
	}

	public void write(int b) throws IOException {
		appendText(String.valueOf((char) b));
	}
}
