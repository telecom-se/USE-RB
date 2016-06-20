package fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.DatasetsBenchmarked;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 *         This is the actual implementation for merging the possible values of an <code>enum</code> in a string array. An example of usage in an enum is located at
 *         {@link DatasetsBenchmarked#names()}. I learnt this trick from <a href="http://stackoverflow.com/questions/77213/eliminating-duplicate-enum-code">here</a>.
 */
public class ListableValuesHelper {

    /**
     * Hides default, unexpected, constructor.
     */
    private ListableValuesHelper() {

    }

    /**
     * Build an array of the possible values from an enum implementing the interface {@link ListableValues}.
     * 
     * @param states
     *            the possible enum values retrieved using the method <code>values()</code> on an enum object.
     * @return The array of all corresponding string values of items in <code>states</code>
     */
    public static String[] names(final ListableValues[] states) {
        final String[] names = new String[states.length];

        for (int i = 0; i < states.length; i++) {
            names[i] = states[i].toString();
        }
        return names;
    }
}