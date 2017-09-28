package com.global.elastic.builder;

import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;

/**
 * Builder to execute the update by query.
 * <P>
 *
 * @author Sopra Steria.
 * @copyright � Airbus 2016. All rights reserved.
 */
public class UpdateByQueryBuilder implements Builder<BulkByScrollResponse> {

    /** Comment for <code>ubqrb</code>. */
    UpdateByQueryRequestBuilder ubqrb;

    /**
     * Default constructor.
     *
     * @param ubqrb the ubqrb
     */
    public UpdateByQueryBuilder(UpdateByQueryRequestBuilder ubqrb) {
        this.ubqrb = ubqrb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BulkByScrollResponse get() {
        return ubqrb.get();
    }

}
