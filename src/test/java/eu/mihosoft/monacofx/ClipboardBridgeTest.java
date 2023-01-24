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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClipboardBridgeTest {
	@Mock
	Document document;

	@Mock
	SystemClipboardWrapper systemClipboardWrapper;

	@InjectMocks
	ClipboardBridge clipboardBridge;

	@Captor
	ArgumentCaptor<String> updateTextCapture;


	@Test
	public void copy() {
		// given
		JSObject selection = Mockito.mock(JSObject.class);
		when(selection.getMember("startLineNumber")).thenReturn(1);
		when(selection.getMember("startColumn")).thenReturn(6);
		when(selection.getMember("endLineNumber")).thenReturn(1);
		when(selection.getMember("endColumn")).thenReturn(18);
		when(document.getText()).thenReturn("some stringy text where 'stringy text' is copied");

		// when
		clipboardBridge.copy(selection);

		// then
		verify(systemClipboardWrapper).putString("stringy text");
	}

	@Test
	public void paste()  {
		// given
		when(document.getText()).thenReturn("some text where at this position '' something is pasted");

		JSObject selection = Mockito.mock(JSObject.class);
		when(selection.getMember("startLineNumber")).thenReturn(1);
		when(selection.getMember("startColumn")).thenReturn(35);

		JSObject position = Mockito.mock(JSObject.class);
		when(position.getMember("lineNumber")).thenReturn(1);
		when(position.getMember("column")).thenReturn(35);

		when(systemClipboardWrapper.hasString()).thenReturn(true);
		when(systemClipboardWrapper.getString()).thenReturn("text in \nclipboard");

		// when
		JSObject paste = clipboardBridge.paste(selection, position);

		// then
		Mockito.verify(document).updateText(updateTextCapture.capture());
		assertEquals("some text where at this position 'text in \nclipboard' something is pasted", updateTextCapture.getValue());
		verify(paste).setMember("lineNumber", 2L);
		verify(paste).setMember("column", 44);

	}

	@Test
	public void pasteAtTheEnd()  {
		// given
		when(document.getText()).thenReturn("some text where pasted at the end");

		JSObject selection = Mockito.mock(JSObject.class);
		when(selection.getMember("startLineNumber")).thenReturn(2);
		when(selection.getMember("startColumn")).thenReturn(0);

		JSObject position = Mockito.mock(JSObject.class);
		when(position.getMember("lineNumber")).thenReturn(2);
		when(position.getMember("column")).thenReturn(0);

		when(systemClipboardWrapper.hasString()).thenReturn(true);
		when(systemClipboardWrapper.getString()).thenReturn("text in clipboard");
		// when
		JSObject paste = clipboardBridge.paste(selection, position);
		// then
		Mockito.verify(document).updateText(updateTextCapture.capture());
		assertEquals("some text where pasted at the end\ntext in clipboard", updateTextCapture.getValue());
		verify(paste).setMember("lineNumber", 2L);
		verify(paste).setMember("column", 17);
	}

}