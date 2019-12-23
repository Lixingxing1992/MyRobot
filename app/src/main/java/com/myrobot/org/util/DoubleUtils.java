package com.myrobot.org.util;

import java.math.BigDecimal;

/**
 * @author Lixingxing
 */
public class DoubleUtils {

    public static double getPrice(int realPrice){
        return new BigDecimal(realPrice).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
