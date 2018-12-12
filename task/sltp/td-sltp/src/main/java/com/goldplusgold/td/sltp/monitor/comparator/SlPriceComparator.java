package com.goldplusgold.td.sltp.monitor.comparator;

import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * 止损价格比较器
 * Created by Administrator on 2017/5/15.
 */
@Component
public class SlPriceComparator implements Comparator<UserSltpRecord> {
    @Override
    public int compare(UserSltpRecord o1, UserSltpRecord o2) {
        return (int) (o1.getSlPrice() - o2.getSlPrice());
    }
}
