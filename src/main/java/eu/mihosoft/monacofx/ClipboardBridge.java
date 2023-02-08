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


import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Bridge between javascript code and java to add and use system clipboard functionality.
 */
public class ClipboardBridge {

	private final Document document;
	private final SystemClipboardWrapper systemClipboardWrapper;

	public ClipboardBridge(Document document, SystemClipboardWrapper systemClipboardWrapper) {
		this.document = document;
		this.systemClipboardWrapper = systemClipboardWrapper;
	}

	/**
	 * Retrieves and copies the selected text into the clipboard.
	 * Triggered by context menu item 'copy' or 'cut'.
	 * @param jsSelection javascript object passed as parameter.
	 */
	public void copy(JSObject jsSelection) {
		int startLineNumber = getNumber(jsSelection, "startLineNumber") - 1;
		int startColumn = getNumber(jsSelection, "startColumn") - 1;
		int endLineNumber = getNumber(jsSelection, "endLineNumber") - 1;
		int endColumn = getNumber(jsSelection, "endColumn") - 1;
		String originText = document.getText();
		String[] lines = originText.split("\n", -1);
		StringBuilder copyText = new StringBuilder();
		if (startLineNumber == endLineNumber) {
			copyText = new StringBuilder(lines[startLineNumber].substring(startColumn, endColumn));
		} else {
			String startLine = lines[startLineNumber].substring(startColumn);
			copyText = new StringBuilder(startLine + "\n");
			for (int i = startLineNumber + 1; i < endLineNumber; i++) {
				copyText.append(lines[i]).append("\n");
			}
			String endLine = lines[endLineNumber].substring(0, endColumn);
			copyText.append(endLine);
		}
		String finalCopyText = copyText.toString();

		systemClipboardWrapper.putString(finalCopyText);
	}


	/**
	 * Pastes the text from Clipboard into the editor at the mouse position
	 * and returns the new position after the selected text.
	 * Triggerd by context menu item 'paste'.
	 * @param jsSelection mouse postion before paste.
	 * @param position mouse postion before paste.
	 * @return new mouse postion after the selected text
	 */
	public JSObject paste(JSObject jsSelection, JSObject position) {
		if (systemClipboardWrapper.hasString()) {
			String pasteString = systemClipboardWrapper.getString();
			String originText = document.getText();
			String changedText = addPasteString(jsSelection, pasteString, originText);
			document.updateText(changedText);
			calcNewCursorPosition(position, pasteString);
		}
		return position;
	}

	private String addPasteString(JSObject jsSelection, String pasteString, String originText) {
		// https://stackoverflow.com/questions/14602062/java-string-split-removed-empty-values
		String[] lines = originText.split("\n", -1);
		int startLineNumber = getNumber(jsSelection, "startLineNumber") - 1;
		int startColumn = getNumber(jsSelection, "startColumn") - 1;
		if (startLineNumber < lines.length) {
			String beforeMousePosition = lines[startLineNumber].substring(0, startColumn);
			String afterMousePosition = lines[startLineNumber].substring(startColumn);
			lines[startLineNumber] = beforeMousePosition + pasteString + afterMousePosition;;
		} else {
			List<String> list = new ArrayList<>(Arrays.asList(lines));
			list.add(pasteString);
			lines = list.toArray(new String[0]);
		}
		return String.join("\n", lines);
	}

	private void calcNewCursorPosition(JSObject position, String string) {
		int lineNumber = getNumber(position, "lineNumber");
		int column = getNumber(position, "column");
		long count = string.split("\n", -1).length - 1;
		position.setMember("lineNumber", lineNumber + count);

		Optional<String> lastLine = string.lines().skip(count).findFirst();
		if (lastLine.isPresent()) {
			position.setMember("column", column + lastLine.get().length());
		} else {
			position.setMember("column", column + string.length());
		}
	}

	private int getNumber(JSObject selection, String numberName) {
		int number = 0;
		Object selectionMember = selection.getMember(numberName);
		if (selectionMember instanceof Integer) {
			number = (Integer) selectionMember;
		} else if (selectionMember instanceof Double) {
			number = ((Double) selectionMember).intValue();
		}
		return number;
	}
}
