package fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.log4j.Logger;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr> utility
 *         class for some benchmarking tasks.
 */
public class BenchmarkUtil {

	/**
	 * For logging facility
	 */
	private static final Logger LOGGER = Logger.getLogger(BenchmarkUtil.class);

	/**
	 * Check if a string is an integer.
	 * 
	 * @param s
	 *            the string to be evaluated.
	 * @param radix
	 *            recursive call argument
	 * @return true if <code>s</code> contains an integer, and only an integer.
	 */
	public static boolean isInteger(final String s, final int radix) {
		final Scanner sc = new Scanner(s.trim());
		if (!sc.hasNextInt(radix)) {
			sc.close();
			return false;
		}
		sc.nextInt(radix);
		final boolean isInt = !sc.hasNext();
		sc.close();
		return isInt;
	}

	/**
	 * Decompress a <code>.zip</code> file to a local folder.
	 * 
	 * @param zipFile
	 *            the <code>.zip</code> file to decompress.
	 * @param location
	 *            target folder of the decompressed files.
	 */
	public static void decompress(final String source, final String destination) {
		try {
			final ZipFile zipFile = new ZipFile(source);
			zipFile.extractAll(destination);

		} catch (final ZipException e) {
			LOGGER.fatal("Cannot unzip file " + source + " to " + destination);
			e.printStackTrace();
			throw new RuntimeErrorException(new Error("Cannot unzip file."));
		} catch (final Exception e) {
			LOGGER.fatal("Cannot unzip file " + source + " to " + destination);
			throw new RuntimeErrorException(new Error("Cannot unzip file."));
		}
	}

	/**
	 * Utility method to extract a Java resource in the classpath to a temporary
	 * file.
	 * 
	 * @param resourcePath
	 *            path to a resource in the classpath to be extracted.
	 * @return the absolute path to the newly created temporary file containing
	 *         the extracted Java resource.
	 */
	public String extractResourceToTemp(String resourcePath) {
		InputStream resourceStream = this.getClass().getClassLoader()
				.getResourceAsStream(resourcePath);
		File tempFile;
		try {
			String targetNameInTempDir = (resourcePath.contains("/")) ? resourcePath
					.substring(resourcePath.lastIndexOf("/"),
							resourcePath.length()) : resourcePath;
			tempFile = new File(System.getProperty("java.io.tmpdir")
					+ System.getProperty("file.separator")
					+ targetNameInTempDir);
			FileOutputStream fout = null;

			fout = new FileOutputStream(tempFile);
			int c;

			while ((c = resourceStream.read()) != -1) {
				fout.write(c);
			}
			if (resourceStream != null) {
				resourceStream.close();
			}
			if (fout != null) {
				fout.close();
			}
			return tempFile.getAbsolutePath();
		} catch (NullPointerException | IOException e) {
			LOGGER.error("cannot create temporary file to extract embeded file in this JAR file (from resource path: "
					+ resourcePath
					+ " to target temp file: "
					+ System.getProperty("java.io.tmpdir")
					+ System.getProperty("file.separator") + resourcePath + ")");
			throw new RuntimeErrorException(new Error(
					"cannot write to temporary file "));
		}
	}
}
