package com.finace.miscroservice.official_website.rpc;

import com.finace.miscroservice.commons.entity.ChannelBanner;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "ACTIVITY")
public interface ActivityRpcService {
    /**
     * 根据红包id获取红包信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/sys/activity/getRpById", method = RequestMethod.POST)
    public UserRedPackets getRpById(@RequestParam("id") int id);

    /**
     * 红包使用修改红包状态
     * @param
     * @return
     */
    @RequestMapping(value = "/sys/activity/updateHbStatus", method = RequestMethod.POST)
    public UserRedPackets updateHbStatus(@RequestParam("hbid") String hbid,
                                         @RequestParam("userId") int userId,
                                         @RequestParam("borrowName") String borrowName,
                                         @RequestParam("account") double account);

    /**
     *
     * @param channel
     * @return
     */
    @RequestMapping(value = "/sys/activity/getChannelBanner", method = RequestMethod.GET)
    public List<ChannelBanner> getChannelBanner(@RequestParam("channel") String channel);


}
