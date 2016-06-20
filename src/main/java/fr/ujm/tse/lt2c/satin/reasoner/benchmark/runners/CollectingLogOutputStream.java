package fr.ujm.tse.lt2c.satin.reasoner.benchmark.runners;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;

/**
 * This class allows to manipulate the standard output of a benchmark launched
 * using commons exec as a stream. This is important to detect errors and make
 * the external command stops before the timeout, which can be set to a high
 * value hence waiting for a benchmark in failure a long time for nothing.
 * 
 * From <a
 * href="http://stackoverflow.com/questions/7340452/process-output-from-apache-
 * commons -exec
 * ">http://stackoverflow.com/questions/7340452/process-output-from-apache-
 * commons-exec</a> With some adaptation, especially
 * {@link CollectingLogOutputStream#getStdOutputAsString()}
 * 
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 */
public class CollectingLogOutputStream extends LogOutputStream {
	private final List<String> lines = new LinkedList<String>();
	Thread mayHaveErrors = null;
	DefaultExecutor executor = null;
	private String errorMessage = "";
	private boolean erroneous = false;

	public boolean isErroneous() {
		return erroneous;
	}

	public void setErroneous(boolean erroneous) {
		this.erroneous = erroneous;
	}

	public CollectingLogOutputStream(Thread thread, DefaultExecutor exe) {
		mayHaveErrors = thread;
		executor = exe;
	}

	@Override
	protected void processLine(final String line, final int level) {

		if (line.contains("Exception")) {
			erroneous = true;
			this.errorMessage = line;
			mayHaveErrors.stop();
		}
		this.lines.add(line);
	}

	/**
	 * Get the standard output from the stream buffer.
	 * 
	 * @return The standard output gathered so far as a string
	 */
	public String getStdOutputAsString() {
		final StringBuffer temp = new StringBuffer();
		for (final String line : this.lines) {
			temp.append(line).append("\n");
		}
		return temp.toString();
	}

	/**
	 * @return the lines of the standard output as an array of String.
	 */
	public List<String> getLines() {
		return this.lines;
	}

	/**
	 * The error message found in the standard output, if any.
	 * 
	 * @return the error message found in the standard output stream, possibly
	 *         empty (\"\")
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}