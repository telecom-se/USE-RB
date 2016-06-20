package fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.ListableValues;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.ListableValuesHelper;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 *         We list the different Datasets Under Test (DUT) in this benchmark,
 *         which are :
 *         <ul>
 *         <li>Several datasets generated using the <a href=
 *         "http://wifo5-03.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/"
 *         >Berlin SPARQL BenchMark (BSBM)</a>. The datasets were generated
 *         using <a href="https://gist.github.com/cgravier/8658389">this</a>
 *         gist. Other datasets from the <a
 *         href="http://swat.cse.lehigh.edu/projects/lubm/">LUBM (Lehigh
 *         University Benchmark)</a> can be used. Each one reuse the previous
 *         triples and add new ones to match the expected number of triples.
 *         therefore pairwise consistent :
 *         <ul>
 *         <li><BSBM_100k : 100,000 triples/li>
 *         <li><BSBM_200k : 200,000 triples/li>
 *         <li><BSBM_500k : 500,000 triples/li>
 *         <li><BSBM_1M : 1,000,000 triples/li>
 *         <li><BSBM_5M : 5,000,000 triples/li>
 *         <li><BSBM_10M : 10,000,000 triples/li>
 *         <li><BSBM_25M : 25,000,000 triples/li>
 *         <li><BSBM_50M : 50,000,000 triples/li>
 *         <li>LUBM_1 : 103,104 triples</li>
 *         <li>LUBM_5 : 645,835 triples</li>
 *         <li>LUBM_10 : 1,316,700</li>
 *         </ul>
 *         </li> Another group of datasets including a SubclassOf subsomption
 *         tree, which is very computational intensive when computing its
 *         closure :
 *         <ul>
 *         <li>SubclassOf10 : 20 triples</li>
 *         <li>SubclassOf20 : 40 triples</li>
 *         <li>SubclassOf50 : 100 triples</li>
 *         <li>SubclassOf100 : 200 triples</li>
 *         <li>SubclassOf200 : 400 triples</li>
 *         <li>SubclassOf500 : 1000 triples</li>
 *         </ul>
 *         <li>Other datasets are real-life example :
 *         <ul>
 *         <li>WikiOntology</li>
 *         <li>Wordnet</li>
 *         <li>Yago schema + taxonomy</li>
 *         </ul>
 *         </li> </ul>
 * 
 *         All these dataset files are supposed to be hosted on the Web. Their
 *         Web location is defined by the property
 *         \"INFERRAY_DATASETS_REMOTESTORAGE\" in inferray-benchmark.properties
 *         files that is supposed to be in the classpath (see
 *         src/main/resources/inferray-benchmark.properties). For each dataset,
 *         this file also hosts the filename at the Web location for the
 *         dataset. The ID of this property is hosted in this enum at
 *         {@link DatasetsBenchmarked#propertyNameForDatasetFilename}.<br />
 * 
 *         For instance, if you set the following :
 * 
 *         <pre>
 *            INFERRAY_DATASETS_REMOTESTORAGE = http://datasets-satin.telecom-st-etienne.fr/cgravier/inferray/
 *            INFERRAY_DATASETS_FILENAMES_BSBM100k = BSBM_100k.zip
 * </pre>
 * 
 *         Then BSBM 100k triples benchmark is expected to be located at
 *         http://datasets
 *         -satin.telecom-st-etienne.fr/cgravier/inferray/BSBM_100k.zip
 * 
 *         While no guarantee is provided, we will try to maintain the
 *         aforementioned URL, it is safe to use it in a Benchmark run as each
 *         benchmark file we be downloaded only once.<br />
 * 
 *         As for now, the number of triples is not significant for the
 *         application, yet it can be expected to use for some stats in the
 *         future and logging info.
 * 
 */
public enum DatasetsBenchmarked implements ListableValues {

	/**
	 * BSBM with about 100,000 triples.
	 */
	BSBM_100(100 * 1000, "INFERRAY_DATASETS_FILENAMES_BSBM100k") {
		@Override
		public String getDatasetName() {
			return "BSBM 100k triples";
		}
	},
	/**
	 * BSBM with about 200,000 triples.
	 */
	BSBM_200(200 * 1000, "INFERRAY_DATASETS_FILENAMES_BSBM200k") {
		@Override
		public String getDatasetName() {
			return "BSBM 200k triples";
		}
	},
	/**
	 * BSBM with about 500,000 triples.
	 */
	BSBM_500(500 * 1000, "INFERRAY_DATASETS_FILENAMES_BSBM500k") {
		@Override
		public String getDatasetName() {
			return "BSBM 500k triples";
		}
	},
	/**
	 * BSBM with about 1,00,000 triples.
	 */
	BSBM_1000(1 * 1000 * 1000, "INFERRAY_DATASETS_FILENAMES_BSBM1M") {
		@Override
		public String getDatasetName() {
			return "BSBM 1 million triples";
		}
	},
	/**
	 * BSBM with about 5,00,000 triples.
	 */
	BSBM_5000(5 * 1000 * 1000, "INFERRAY_DATASETS_FILENAMES_BSBM5M") {
		@Override
		public String getDatasetName() {
			return "BSBM 5 millions triples";
		}
	},
	/**
	 * BSBM with about 10,00,000 triples.
	 */
	BSBM_10000(10 * 1000 * 1000, "INFERRAY_DATASETS_FILENAMES_BSBM10M") {
		@Override
		public String getDatasetName() {
			return "BSBM 10 millions triples";
		}
	},
	/**
	 * BSBM with about 25,00,000 triples.
	 */
	BSBM_25000(25 * 1000 * 1000, "INFERRAY_DATASETS_FILENAMES_BSBM25M") {
		@Override
		public String getDatasetName() {
			return "BSBM 25 millions triples";
		}
	},
	/**
	 * BSBM with about 50,00,000 triples.
	 */
	BSBM_50000(50 * 1000 * 1000, "INFERRAY_DATASETS_FILENAMES_BSBM50M") {
		@Override
		public String getDatasetName() {
			return "BSBM 50 millions triples";
		}
	},
	/**
	 * LUBM with 1 university
	 */
	LUBM_1(103_104, "INFERRAY_DATASETS_FILENAMES_LUBM_1") {
		@Override
		public String getDatasetName() {
			return "LUBM 1 university (103,104 triples) ";
		}
	},
	/**
	 * LUBM with 5 universities
	 */
	LUBM_5(645_835, "INFERRAY_DATASETS_FILENAMES_LUBM_5") {
		@Override
		public String getDatasetName() {
			return "LUBM 5 universities (645,835 triples) ";
		}
	},
	/**
	 * LUBM with 10 universities
	 */
	LUBM_10(1_316_700, "INFERRAY_DATASETS_FILENAMES_LUBM_10") {
		@Override
		public String getDatasetName() {
			return "LUBM 10 universities (1,316,700 triples) ";
		}
	},
	/**
	 * LUBM with 25 universities
	 */
	LUBM_25(3_431_967, "INFERRAY_DATASETS_FILENAMES_LUBM_25") {
		@Override
		public String getDatasetName() {
			return "LUBM 25 universities  (3,431,967 triples)";
		}
	},
	/**
	 * LUBM with 50 universities
	 */
	LUBM_50(10_322_607, "INFERRAY_DATASETS_FILENAMES_LUBM_50") {
		@Override
		public String getDatasetName() {
			return "LUBM 50 universities (10,322,607 triples) ";
		}
	},
	/**
	 * LUBM with 75 universities
	 */
	LUBM_75(13_854_087, "INFERRAY_DATASETS_FILENAMES_LUBM_75") {
		@Override
		public String getDatasetName() {
			return "LUBM 75 universities  (13,854,087 triples)";
		}
	},
	/**
	 * LUBM with 100 universities
	 */
	LUBM_100(17_311_937, "INFERRAY_DATASETS_FILENAMES_LUBM_100") {
		@Override
		public String getDatasetName() {
			return "LUBM 100 universities (17,311,937 triples) ";
		}
	},
	/**
	 * The wikipedia ontology.
	 */
	WIKIONTOLOGY(458369, "INFERRAY_DATASETS_FILENAMES_WIKIONTOLOGY") {
		@Override
		public String getDatasetName() {
			return "Wiki Ontology";
		}
	},
	/**
	 * The wordnet ontology in RDF
	 */
	WORDNET(473589, "INFERRAY_DATASETS_FILENAMES_WORDNET") {
		@Override
		public String getDatasetName() {
			return "Wordnet ontology";
		}
	},
	/**
	 * The Yago schema + taxonomy ontology
	 */
	YAGO(452057, "INFERRAY_DATASETS_FILENAMES_YAGO") {
		@Override
		public String getDatasetName() {
			return "Yago ontology";
		}
	},
	
	SUBCLASSOF50(100, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_50k") {
		@Override
		public String getDatasetName() {
			return "SublassOf50";
		}
	},
	SUBCLASSOF100(200, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_100k") {
		@Override
		public String getDatasetName() {
			return "SublassOf100";
		}
	},
	SUBCLASSOF200(200, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_200k") {
		@Override
		public String getDatasetName() {
			return "SublassOf200";
		}
	},
	SUBCLASSOF500(1000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_500k") {
		@Override
		public String getDatasetName() {
			return "SublassOf500";
		}
	},
	SUBCLASSOF1000(2000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_1000k") {
		@Override
		public String getDatasetName() {
			return "SublassOf1000";
		}
	},
	SUBCLASSOF2500(5000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_2500k") {
		@Override
		public String getDatasetName() {
			return "SublassOf2500";
		}
	},
	SUBCLASSOF5000(10000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_5000k") {
		@Override
		public String getDatasetName() {
			return "SublassOf5000";
		}
	},
	SUBCLASSOF7500(15000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_7500k") {
		@Override
		public String getDatasetName() {
			return "SublassOf7500";
		}
	},
	SUBCLASSOF10000(20000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_10000k") {
		@Override
		public String getDatasetName() {
			return "SublassOf10000";
		}
	},
	SUBCLASSOF15000(30000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_15000k") {
		@Override
		public String getDatasetName() {
			return "SubclassOf15000";
		}
	},
	SUBCLASSOF20000(40000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_20000k") {
		@Override
		public String getDatasetName() {
			return "SubclassOf20000";
		}
	},
	SUBCLASSOF25000(50000, "INFERRAY_DATASETS_FILENAMES_SUBCLASS_25000k") {
		@Override
		public String getDatasetName() {
			return "SubclassOf25000";
		}
	};
//	SUBCLASSOFBINLVL_10(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL10") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_10";
//		}
//	},
//	SUBCLASSOFBINLVL_11(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL11") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_11";
//		}
//	},
//	SUBCLASSOFBINLVL_12(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL12") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_12";
//		}
//	},
//	SUBCLASSOFBINLVL_13(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL13") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_13";
//		}
//	},
//	SUBCLASSOFBINLVL_14(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL14") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_14";
//		}
//	},
//	SUBCLASSOFBINLVL_15(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL15") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_15";
//		}
//	},
//	SUBCLASSOFBINLVL_16(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL16") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_16";
//		}
//	},
//	SUBCLASSOFBINLVL_17(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL17") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_17";
//		}
//	},
//	SUBCLASSOFBINLVL_18(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL18") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_18";
//		}
//	},
//	SUBCLASSOFBINLVL_19(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL19") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_19";
//		}
//	},
//	SUBCLASSOFBINLVL_20(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL20") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_20";
//		}
//	},
//	SUBCLASSOFBINLVL_21(1, "INFERRAY_DATASETS_FILENAMES_BINSUBCLASS_LVL21") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfBinLvl_21";
//		}
//	},
//	GRIDSUBCLASS_LVL7X5(1, "INFERRAY_DATASETS_FILENAMES_GRIDSUBCLASS_LVL7X5") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfGridLvl_7x5";
//		}
//	},
//	GRIDSUBCLASS_LVL10X5(1, "INFERRAY_DATASETS_FILENAMES_GRIDSUBCLASS_LVL10X5") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfGridLvl_10x5";
//		}
//	},
//	GRIDSUBCLASS_LVL10X10(1, "INFERRAY_DATASETS_FILENAMES_GRIDSUBCLASS_LVL10X10") {
//		@Override
//		public String getDatasetName() {
//			return "SubclassOfGridLvl_10x10";
//		}
//	},
//	SUBCLASS_REAL_PRODUCTS(1, "SUBCLASS_REAL_PRODUCTS") {
//		@Override
//		public String getDatasetName() {
//			return "Subclass real product";
//		}
//	};

	/**
	 * Number of triples in this dataset.
	 */
	private int numberOfTriples;

	/**
	 * The property in <code>inferray-benchmark.properties</code> for getting
	 * the filename of this dataset.
	 */
	private String propertyNameForDatasetFilename;

	/**
	 * Expected constructor.
	 * 
	 * @param nbTriples
	 *            amount of triples in <code>this</code>.
	 * @param propertyName
	 *            name of the property to read for fetching the name of the
	 *            dataset in local and remote storage from the
	 *            <code>inferray-benchmark.properties</code> file.
	 */
	private DatasetsBenchmarked(final int nbTriples, final String propertyName) {
		this.numberOfTriples = nbTriples;
		this.propertyNameForDatasetFilename = propertyName;
	}

	/**
	 * A string label for presentation and logging purpose.
	 * 
	 * @return the name of the dataset for logging purpose
	 */
	public abstract String getDatasetName();

	/**
	 * @return the numberOfTriples
	 */
	public int getNumberOfTriples() {
		return this.numberOfTriples;
	}

	/**
	 * @param numberOfTriples
	 *            the numberOfTriples to set
	 */
	public void setNumberOfTriples(final int numberOfTriples) {
		this.numberOfTriples = numberOfTriples;
	}

	/**
	 * @return the filename
	 */
	public String getPropertyFilename() {
		return this.propertyNameForDatasetFilename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setPropertyFilename(final String filename) {
		this.propertyNameForDatasetFilename = filename;
	}

	/**
	 * List all possible values for <code>this</code> as a String array.
	 * Implementing factorized methods in a enum is a trick I learnt from
	 * <code>http://stackoverflow.com/questions/77213/eliminating-duplicate-enum-code</code>
	 */
	public static String[] names() {
		return ListableValuesHelper.names(values());
	}
}