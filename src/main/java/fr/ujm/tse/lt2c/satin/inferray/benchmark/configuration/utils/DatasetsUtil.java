package fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.DatasetsBenchmarked;
import fr.ujm.tse.lt2c.satin.reasoner.benchmark.main.Benchmark;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr> Some
 *         utility function for handling datasets.
 */
public class DatasetsUtil {

    /**
     * A logging facility using log4j API.
     */
    private static final Logger LOGGER = Logger.getLogger(DatasetsUtil.class);

    /**
     * The file separator stirng property for System.getProperty.
     */
    private static final String FILESEPARATOR_PROPERTY = "file.separator";

    /**
     * This is a utility class, the default constructor should not be called, we
     * therefore hide it.
     */
    private DatasetsUtil() {

    }

    /**
     * Download the datasets from the URL specified by the property
     * ${INFERRAY_DATASETS_REMOTESTORAGE} in the
     * <code>inferray-benchmark.properties</code> to the folder specified by the
     * property ${INFERRAY_DATASETS_LOCALSTORAGE}
     * 
     * @param limited
     *            if true, only download BSBM100, BSBM200, BSBM 500 (because we
     *            are in testing environment). All datasets are downloaded
     *            otherwise.
     */
    public static void downloadDatasets(final boolean limited, Benchmark bench) {

        try {
            final PropertiesConfiguration config = new PropertiesConfiguration("inferray-benchmark.properties");
            if (!limited) {
                for (final DatasetsBenchmarked ds : DatasetsBenchmarked.values()) {

                    // only if eligible! bench.benchmarkTablebenchmarkTable
                    
                    if (Benchmark.benchmarkTable.rowKeySet().contains(ds)) {
                        LOGGER.info("The benchmark will need the dataset " + ds.getDatasetName() + ". Let's download it.");
                        downloadDataset(config, ds);
                    } else {
                        LOGGER.info("The benchmark will not need the dataset " + ds.getDatasetName()
                                + " given the benchmark table. Let's skip its download.");
                    }
                }
            } else {
                LOGGER.info("Downloading datasets BSBM100k, BSBM200k, BSBM500k...");
                downloadDataset(config, DatasetsBenchmarked.BSBM_100);
              //  downloadDataset(config, DatasetsBenchmarked.BSBM_200);
                downloadDataset(config, DatasetsBenchmarked.BSBM_500);
            }
        } catch (final ConfigurationException e) {
            LOGGER.fatal("Cannot find or read inferray-benchmark.properties file because" + e.getMessage(), e);
        } catch (final MalformedURLException e) {
            LOGGER.fatal("Cannot create an URL", e);
        } catch (final IOException e) {
            LOGGER.fatal("Cannot download from the dataset URL.", e);
            throw new RuntimeErrorException(new Error("Cannot download from dataset URL."));
        }
    }

    /**
     * Download a remote dataset to filesystem.
     * 
     * @param config
     *            the properties of the inferray benchmark call.
     * @param ds
     *            the dataset to download.
     * @throws MalformedURLException
     *             When the URL of the remote dataset cannot be created
     *             properly.
     * @throws IOException
     *             When the remote URL is not a "fetchable" remote dataset.
     */
    private static void downloadDataset(final PropertiesConfiguration config, final DatasetsBenchmarked ds) throws IOException {
        LOGGER.info("Downloading dataset " + ds.getDatasetName() + "...");
        final String baseUrl = config.getString("INFERRAY_DATASETS_REMOTESTORAGE");
        final String filename = config.getString(ds.getPropertyFilename());
        final URL url = new URL(baseUrl + filename);
        final File localFile = new File(config.getString("INFERRAY_DATASETS_LOCALSTORAGE") + System.getProperty(FILESEPARATOR_PROPERTY)
                + config.getString(ds.getPropertyFilename()));
        LOGGER.info("Downloading file " + url.toString() + " to " + localFile + "...");
        download(url, localFile);
    }

    /**
     * Download the file located at <code>url</code> to local file
     * <code>localFile</code>.
     * 
     * @param url
     *            where to download from.
     * @param localFile
     *            where to download to.
     * @throws IOException
     * @throws MalformedURLException
     */
    private static void download(final URL url, final File localFile) throws IOException {
        final ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        final FileOutputStream fos = new FileOutputStream(localFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
    }

    /**
     * Verify that the folder specified by the property
     * ${INFERRAY_DATASETS_LOCALSTORAGE} :
     * <ul>
     * <li>Exists</li>
     * <li>Contains all required datasets file. For this the check is as
     * follows: the file must exist and must not be empty</li>
     * </ul>
     * 
     * @param fileStorage
     *            the absolute path to the folder expected to contain the
     *            benchmark datasets.
     * @param datasetUnderTest
     * @return true if the filesystem storage satisfies the aforementioned
     *         constraints, false otherwise.
     * @throws ConfigurationException
     *             when <code>inferray-benchmark.properties</code> was not found
     *             on the filesystem.
     */
    public static boolean hasDatasetsOnFilesystem(final String fileStorage, final List<DatasetsBenchmarked> datasetUnderTest)
            throws ConfigurationException {
        final File localStorage = new File(fileStorage);
        final PropertiesConfiguration config = new PropertiesConfiguration("inferray-benchmark.properties");
        if (!localStorage.exists()) {
            return false;
        } else {
            for (final DatasetsBenchmarked dataset : datasetUnderTest) {
                final String datasetFileName = config.getString(dataset.getPropertyFilename());
                final File datasetFile = new File(localStorage.getAbsoluteFile() + System.getProperty(FILESEPARATOR_PROPERTY) + datasetFileName);
                if (!datasetFile.exists()) {
                	LOGGER.error("Following dataset do not exist on file system "+datasetFileName);
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * A convenient way to call
     * {@link DatasetsUtil#hasDatasetsOnFilesystem(String)}
     * 
     * @param fileStorage
     *            a file on the filesystem that must contains a valid dataset of
     *            ontologies to benchmark.
     * @return true if the filesystem storage satisfies the aforementioned
     *         contraints, false otherwise.
     * @throws ConfigurationException
     *             when <code>inferray-benchmark.properties</code> was not found
     *             on the filesystem.
     */
    public static boolean hasDatasetsOnFilesystem(final File localFile, final List<DatasetsBenchmarked> datasetUnderTest)
            throws ConfigurationException {
        return hasDatasetsOnFilesystem(localFile.getAbsolutePath(), datasetUnderTest);
    }
}
