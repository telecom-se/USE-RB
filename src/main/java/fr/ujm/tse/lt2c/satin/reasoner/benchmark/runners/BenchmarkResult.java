package fr.ujm.tse.lt2c.satin.reasoner.benchmark.runners;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 *         The stdout result when calling a {@link BenchmarkExternalRun}. For
 *         now this is a POJO with a single String for the stdout of the call,
 *         this may be extedned in the future, hence an ad hoc class.
 */
public class BenchmarkResult {

	/**
	 * The stdout String.
	 */
	private String res = null;

	/**
	 * @param res
	 *            stdout for an external benchmark command.
	 */
	public BenchmarkResult(final String res) {
		super();
		this.res = res;
	}

	/**
	 * @return the res
	 */
	public String getRes() {
		return this.res;
	}

	/**
	 * @param res
	 *            the res to set
	 */
	public void setRes(final String res) {
		this.res = res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.res == null) ? 0 : this.res.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final BenchmarkResult other = (BenchmarkResult) obj;
		if (this.res == null) {
			if (other.res != null) {
				return false;
			}
		} else if (!this.res.equals(other.res)) {
			return false;
		}
		return true;
	}
}
