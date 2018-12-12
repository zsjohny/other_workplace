package com.goldplusgold.td.sltp.monitor.comparator;

import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * 止盈价格比较器
 * Created by Administrator on 2017/5/15.
 */
@Component
public class TpPriceComparator implements Comparator<UserSltpRecord> {
    @Override
    public int compare(UserSltpRecord o1, UserSltpRecord o2) {
        return (int) (o1.getTpPrice() - o2.getTpPrice());
    }
}
