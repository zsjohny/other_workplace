package com.finace.miscroservice.official_website.rpc;

import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.official_website.entity.response.DataCollectionResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "BORROW")
public interface BorrowRpcService {
    /**
     * 数据明细
     * @return
     */
    @RequestMapping(value = "/sys/borrow/getDataCollection", method = RequestMethod.POST)
    DataCollectionResponse getDataCollection();
}
