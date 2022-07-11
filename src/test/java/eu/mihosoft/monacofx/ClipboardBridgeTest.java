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
		when(position.getMember("column")).thenReturn(33);

		when(systemClipboardWrapper.hasString()).thenReturn(true);
		when(systemClipboardWrapper.getString()).thenReturn("text in \nclipboard");

		// when
		JSObject paste = clipboardBridge.paste(selection, position);

		// then
		Mockito.verify(document).updateText(updateTextCapture.capture());
		assertEquals("some text where at this position 'text in \nclipboard' something is pasted", updateTextCapture.getValue());
		verify(paste).setMember("lineNumber", 2L);
		verify(paste).setMember("column", 42);

	}


}