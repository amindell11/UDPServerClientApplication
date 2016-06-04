package panel;

import java.awt.TextArea;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class CustomOutputStream extends OutputStream {
	public static void reallocatePrint(final TextArea textArea1) {
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea1));
		System.setOut(printStream);
		System.setErr(printStream);
	}	private TextArea textArea;

	public CustomOutputStream(TextArea textArea1) {
		this.textArea = textArea1;
	}

	@Override
	public void write(int b) throws IOException {
		// redirects data to the text area
		textArea.append(String.valueOf((char) b));
		// scrolls the text area to the end of data
		textArea.setCaretPosition(textArea.getRows());
	}

}