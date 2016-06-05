package panel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class CustomOutputStream extends OutputStream {
	public static void reallocatePrint(final JTextArea textArea1) {
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea1));
		System.setOut(printStream);
		System.setErr(printStream);
	}	private JTextArea textArea;

	public CustomOutputStream(JTextArea textArea1) {
		this.textArea = textArea1;
	}

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException
    {
        final String text = new String (buffer, offset, length);
        SwingUtilities.invokeLater(new Runnable ()
            {
                @Override
                public void run() 
                {
                    textArea.append (text);
                }
            });
    }

    @Override
    public void write(int b) throws IOException
    {
        write (new byte [] {(byte)b}, 0, 1);
    }
}