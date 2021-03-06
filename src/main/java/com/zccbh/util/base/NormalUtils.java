package com.zccbh.util.base;

import com.zccbh.test.exception.CustomException;

import java.math.BigDecimal;

public class NormalUtils {

    /**
     * 舍弃小数并加一
     */
    public static final int ADD_ONE = 1;
    /**
     * 舍弃小数
     */
    public static final int IGNORE_DECIMAL = 2;

    /**
     * 将String转为double，并传入是否需要四舍五入
     *
     * @param doubleStr
     * @param b         b为true时，四舍五入
     * @return
     */
    public static Double stringToDouble(String doubleStr, boolean b) {
        if (null == doubleStr || "".equals(doubleStr))
            doubleStr = "0.00";
        BigDecimal decimal = new BigDecimal(doubleStr);
        return decimalToDouble(decimal,b);
    }

    /**
     * 将BigDecimal转为2位小数，并传入是否四舍五入
     *
     * @param decimal
     * @param b       b为true时，四舍五入
     * @return
     */
    public static Double decimalToDouble(BigDecimal decimal, boolean b) {
        if (true == b)
            return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return decimal.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     * 将double的string字符串转成int类型，并传入舍弃小数时是否加一
     *
     * @param intStr
     * @param i 1-加一；2-直接舍弃
     * @return
     */
    public static Integer doubleStringToInt(String intStr, int i) {
        Double d = stringToDouble(intStr, true);
        if (ADD_ONE == i)
            return (int) Math.ceil(d);
        else if (IGNORE_DECIMAL == i)
            return d.intValue();
        else
            throw new RuntimeException("请传入舍弃小数时是否加一（1-加一；2-直接舍弃）。");
    }

    /**
     * 将BigDecimal转成int类型，并传入舍弃小数时是否加一
     * @param intStr
     * @param i 1-加一；2-直接舍弃
     * @return
     */
    public static Integer decimalToInt(BigDecimal intStr, int i) {
        if (ADD_ONE == i)
            return intStr.intValue()+1;
        else if (IGNORE_DECIMAL == i)
            return intStr.intValue();
        else
            throw new RuntimeException("请传入舍弃小数时是否加一（1-加一；2-直接舍弃）。");
    }

    public static void main(String[] args) {
//        int c = doubleStringToInt("123.4567", 1);
//        System.out.println(c);

//        BigDecimal a = new BigDecimal(123.4567);
//        int i = a.intValue();
//        System.out.println(i);
//        int ii = a.signum();
//        System.out.println(ii);
//        int iii = a.precision();
//        System.out.println(iii);
//        int iiii = a.scale();
//        System.out.println(iiii);

//         int a = 3;
//         float b = 99.00f;
//         float f = a/b;
//        System.out.println(f);
//        double d = new BigDecimal((float)a/b).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

//        double d = stringToDouble("22",true);
//        System.out.println(d);
//        int i = 3;
//        System.out.println(i/d);
        System.out.println(-stringToDouble("-0.001",true));

    }
}
