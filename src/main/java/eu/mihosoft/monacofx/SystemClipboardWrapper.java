package eu.mihosoft.monacofx;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * Wrapper class to make the clipboard functionality testable.
 */
public class SystemClipboardWrapper {

	private final KeyCodeCombination KEY_CODE_CTRL_C = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
	private final KeyCodeCombination KEY_CODE_CTRL_X = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);

	/**
	 * Puts the text into clipboard.
	 * @param text string set into clipboard
	 */
	public void putString(String text) {
		Platform.runLater(() -> {
			var clipboardContent = new ClipboardContent();
			clipboardContent.putString(text);
			Clipboard.getSystemClipboard().setContent(clipboardContent);
		});
	}

	/**
	 * When ever KeyEvent.KEY_PRESSED with 'Ctrl x' or 'Ctrl c' happens the passed string obj is copied in
	 * to the clipboard
	 * @param event key event
	 * @param obj cut or copied text object.
	 */
	public void handleCopyCutKeyEvent(KeyEvent event, Object obj) {
		if (event.getEventType().getName().equals("KEY_PRESSED") && KEY_CODE_CTRL_X.match(event) || (KEY_CODE_CTRL_C.match(event))) {
			String selectedText = String.valueOf(obj);
			if (selectedText.isEmpty()) {
				event.consume();
			} else {
				Platform.runLater(() -> {
					var clipboardContent = new ClipboardContent();
					clipboardContent.putString(String.valueOf(obj));
					Clipboard.getSystemClipboard().setContent(clipboardContent);
				});
			}
		}
	}

	/**
	 * Used to check if the clipboard has a string/
	 * @return string content of the clipboard.
	 */
	public boolean hasString() {
		return Clipboard.getSystemClipboard().hasString();
	}

	/**
	 * Used to get the content of the clipboard.
	 * @return the string in the clipboard.
	 */
	public String getString() {
		return Clipboard.getSystemClipboard().getString();
	}
}
