package org.dream.utils.quota.client.hundsun;

import com.hundsun.t2sdk.impl.util.AbstractLogAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yhj on 16/11/10.
 */
public class HundsunLogAdapter extends AbstractLogAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(HundsunLogAdapter.class) ;

    @Override
    public void export(String s) {

        LOG.debug(s);
    }
}
