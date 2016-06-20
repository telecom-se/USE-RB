package fr.ujm.tse.lt2c.inferray.benchmark.configuration.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.google.common.io.Files;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.DatasetsBenchmarked;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 *         This class the mechanism to initialize the filesystem with required datasets in order to run the benchmark.
 *         Especially, it tests :
 *         <ul>
 *         <li>A non existing folder is identified as an un-initialized folder, see {@link #nonExistingFolderTest()}</li>
 *         <li>A existing folder without all dataset files is identified as an un-initialized folder, see {@link #existingButIncorrectFolderTest()}</li>
 *         <li>A existing folder with all dataset files is identified as an initialized folder, see {@link #existingAndCorrectFolderTest()}.</li>
 *         </ul>
 */
public class DatasetsInitializationTest {

    /**
     * Logging facility using log4j.
     */
    private static final Logger LOGGER = Logger.getLogger(DatasetsInitializationTest.class);

    /**
     * A non existing folder
     */
    private File nonExistingFolder = null;
    private File ExistingButIncorrectFolder = null;
    private File ExistingAndCorrectFolder = null;
    private File myTempFolder = null;

    /**
     * Creates some folders to test in the unit tests in a temporary folder.
     */
    @Before
    public void initTest() {
        LOGGER.info("Creating temporary files for " + DatasetsInitializationTest.class.getName() + " unit tests...");
        this.myTempFolder = Files.createTempDir();

        this.nonExistingFolder = new File(this.myTempFolder + "doesntexist");
        this.ExistingButIncorrectFolder = new File(this.myTempFolder + System.getProperty("file.separator") + "exist");
        this.ExistingButIncorrectFolder.mkdirs();
        this.ExistingAndCorrectFolder = new File(this.myTempFolder + System.getProperty("file.separator") + "correct");
        this.ExistingAndCorrectFolder.mkdirs();
        for (final String datasetFileName : DatasetsBenchmarked.names()) {
            final File datasetFile = new File(this.ExistingAndCorrectFolder + System.getProperty("file.separator") + datasetFileName);
            try {
                if (!datasetFile.exists()) {
                    datasetFile.createNewFile();
                }
                Files.write("a non Empty String because the dataset file should not be empty.".getBytes(), datasetFile);
            } catch (final IOException e) {
                LOGGER.fatal("Cannot create dataset file at " + datasetFile.getAbsolutePath() + " because : " + e.getMessage(), e);
            }
        }
        LOGGER.info("Temporary files created.");
    }

    // TODO : create test here where the dataset folder contains acutal tests datasets filename as expected from configuration file.

    /**
     * Remove temporary folder and files used in this test.
     */
    @After
    public void finishTest() {
        LOGGER.info("Deleting temporary files for " + DatasetsInitializationTest.class.getName() + " unit tests...");
        try {
            FileUtils.deleteDirectory(this.nonExistingFolder);
            FileUtils.deleteDirectory(this.ExistingButIncorrectFolder);
            FileUtils.deleteDirectory(this.ExistingAndCorrectFolder);
            FileUtils.deleteDirectory(this.myTempFolder);
        } catch (final IOException e) {
            LOGGER.error("cannot delete temporary folder for DatasetInitialization unit tets.");
        } finally {
            LOGGER.info("Temporary files deleted.");
        }
    }
}
