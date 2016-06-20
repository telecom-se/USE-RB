package fr.ujm.tse.lt2c.inferray.benchmark.configuration.tests;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.DatasetsBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.LogicalFragmentBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.ReasonerBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.ListableValuesHelper;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr> Unit
 *         tests for some <code>enum</code> classes, especially
 *         {@link DatasetsBenchmarked} and {@link LogicalFragmentBenchmarked}.
 */
public class EnumsTest {

	@Test
	public void dsBenchmarkedEnumTest() {

		final List<String> possibleValues = Arrays.asList(DatasetsBenchmarked
				.names());
		//Assert.assertEquals(43, possibleValues.size());
		Arrays.asList(possibleValues);

		Assert.assertTrue(possibleValues.contains("BSBM_100"));
		Assert.assertTrue(possibleValues.contains("BSBM_500"));
		Assert.assertTrue(possibleValues.contains("BSBM_1000"));
		Assert.assertTrue(possibleValues.contains("BSBM_5000"));
		Assert.assertTrue(possibleValues.contains("BSBM_10000"));
		Assert.assertTrue(possibleValues.contains("BSBM_25000"));
		Assert.assertTrue(possibleValues.contains("BSBM_50000"));
		Assert.assertTrue(possibleValues.contains("LUBM_10"));
		Assert.assertTrue(possibleValues.contains("WIKIONTOLOGY"));
		Assert.assertTrue(possibleValues.contains("WORDNET"));

		// testing one of the possible enum values.
//		final DatasetsBenchmarked ds = DatasetsBenchmarked.BSBM_200;
//		Assert.assertEquals(200000, ds.getNumberOfTriples());
//		Assert.assertEquals("INFERRAY_DATASETS_FILENAMES_BSBM200k",
//				ds.getPropertyFilename());
	}

	@Test
	public void logicalFragmentEnumTest() {

		final List<String> possibleValues = Arrays
				.asList(LogicalFragmentBenchmarked.names());
		Assert.assertEquals(4, possibleValues.size());
		Arrays.asList(possibleValues);

		Assert.assertTrue(possibleValues.contains("RDFS_DEFAULT"));
		Assert.assertTrue(possibleValues.contains("RDFS_FULL"));
		Assert.assertTrue(possibleValues.contains("RHODF"));
		Assert.assertTrue(possibleValues.contains("RDFSPLUS"));
	}

	@Test
	public void reasonerEnumTest() {
		final List<String> possibleReasonerValues = Arrays
				.asList(ReasonerBenchmarked.names());
		Assert.assertEquals(6, possibleReasonerValues.size());
		Arrays.asList(possibleReasonerValues);

		Assert.assertTrue(possibleReasonerValues.contains("JENA"));
		Assert.assertTrue(possibleReasonerValues.contains("OWLIMSE"));
		Assert.assertTrue(possibleReasonerValues.contains("INFERRAY"));
		Assert.assertTrue(possibleReasonerValues.contains("SESAME"));
		Assert.assertTrue(possibleReasonerValues.contains("STREAM"));
	}

	@Test
	public void enumHelperTest() {
		final List<String> vals = Arrays.asList(ListableValuesHelper
				.names(myEnum.values()));
		Assert.assertEquals(3, vals.size());
		Assert.assertTrue(vals.contains("ONE"));
		Assert.assertTrue(vals.contains("TWO"));
		Assert.assertTrue(vals.contains("THREE"));
	}
}
