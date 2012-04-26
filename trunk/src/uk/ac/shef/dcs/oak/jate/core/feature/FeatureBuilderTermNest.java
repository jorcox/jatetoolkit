package uk.ac.shef.dcs.oak.jate.core.feature;

import uk.ac.shef.dcs.oak.jate.JATEException;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndex;
import org.apache.log4j.Logger;

/**
 * A specific type of feature builder that builds an instance of FeatureTermNest from a GlobalIndexStore.
 *
 * @author <a href="mailto:z.zhang@dcs.shef.ac.uk">Ziqi Zhang</a>
 */



public class FeatureBuilderTermNest extends AbstractFeatureBuilder {

	private static Logger _logger = Logger.getLogger(FeatureBuilderTermNest.class);

	/**
	 * Default constructor
	 */
	public FeatureBuilderTermNest() {
		super(null,null,null);
	}

	/**
	 * Build an instance of FeatureTermNest from an instance of GlobalIndexMem
	 * @param index
	 * @return
	 * @throws uk.ac.shef.dcs.oak.jate.JATEException
	 */
	public FeatureTermNest build(GlobalIndex index) throws JATEException {
		FeatureTermNest _feature=new FeatureTermNest(index);
		if (index.getTermsCanonical().size() == 0 || index.getDocuments().size() == 0) throw new
                JATEException("No resource indexed!");

		_logger.info("About to build FeatureTermNest...");
		int counter = 0;
		for (String np : index.getTermsCanonical()) {
			for (String anp : index.getTermsCanonical()) {
				if (anp.length() <= np.length()) continue;
				if (anp.indexOf(" " + np) != -1 || anp.indexOf(np + " ") != -1) //e.g., np=male, anp=the male
					_feature.termNestIn(np, anp);
			}
			counter++;
			if(counter%500==0) _logger.info("Batch done"+counter+" end: "+np);
		}
		return _feature;				
	}
}