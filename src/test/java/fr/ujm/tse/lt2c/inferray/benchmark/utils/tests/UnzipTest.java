package fr.ujm.tse.lt2c.inferray.benchmark.utils.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.Files;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.BenchmarkUtil;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 *         testing unzipping files as provided by {@link BenchmarkUtil#decompress(String, String)}
 */

public class UnzipTest {
    @Test
    public void decompressTest() {
        final File myTempDir = Files.createTempDir();
        BenchmarkUtil.decompress(Thread.currentThread().getContextClassLoader().getResource("test.zip").getPath(), myTempDir.getAbsolutePath());
        Assert.assertTrue(new File(myTempDir.getAbsoluteFile() + System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "file0.bla").exists());
        Assert.assertTrue(new File(myTempDir.getAbsoluteFile() + System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "onefolder"
                + System.getProperty("file.separator") + "file1").exists());
        Assert.assertTrue(new File(myTempDir.getAbsoluteFile() + System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "onefolder"
                + System.getProperty("file.separator") + "file2.txt").exists());
        try {
            FileUtils.deleteDirectory(myTempDir);
        } catch (final IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
