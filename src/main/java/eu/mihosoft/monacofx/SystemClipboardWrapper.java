/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

	private final KeyCodeCombination KEY_CODE_CTRL_C = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
	private final KeyCodeCombination KEY_CODE_CTRL_X = new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN);
	private final KeyCodeCombination KEY_CODE_CTRL_INSERT = new KeyCodeCombination(KeyCode.INSERT, KeyCombination.SHORTCUT_DOWN);

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
		if (event.getEventType().getName().equals("KEY_PRESSED")
				&& KEY_CODE_CTRL_X.match(event)
				|| (KEY_CODE_CTRL_C.match(event))
				|| (KEY_CODE_CTRL_INSERT.match(event))) {
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
