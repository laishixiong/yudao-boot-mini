package cn.iocoder.yudao.framework.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 流量单位转换工具类
 *
 * @author laishixiong
 * @date 2023年11月30日 9:33
 */

public class TrafficFormatUtil {


    public final static Long TRAFFIC_MB = 1024L;
    public final static Long TRAFFIC_GB = 1024L * 1024L;
    public final static Long TRAFFIC_TB = 1024L * 1024L * 1024L;

    /**
     * 给流量值带上单位，支持所有的单位。如果单位超过了BB，则不会在向上显示更高级单位，比如1025BB。
     * 保留两位小数，向上取整。
     * 1024KB->1.00MB
     * 1025KB->1.01MB
     * 1048575KB->1.00GB
     * 1073741824KB->1.00TB
     *
     * @param traffic 单位是KB
     * @return
     */
    public static String formatTrafficAll(Long traffic) {
        String[] suffixs = {"KB", "MB", "GB", "TB", "PB", "EB", "YB", "BB"};
        int maxIndex = suffixs.length - 1;
        BigDecimal trafficBD = new BigDecimal(traffic);
        BigDecimal scaleBD = new BigDecimal(1024);
        int index = 0;
        while (trafficBD.compareTo(scaleBD) >= 0) {
            trafficBD = trafficBD.divide(scaleBD, 2, RoundingMode.CEILING);
            index++;
            if (maxIndex <= index) {
                break;
            }
        }
        return trafficBD + suffixs[index];
    }

    /***
     * 给流量值带上单位，单位最大支持到TB，流量超过1024TB的时候，不会在向上继续转换单位，比如超出TB之后，显示1025TB。
     * 保留两位小数，向上取整。
     * 1024KB->1.00MB
     * 1025KB->1.01MB
     * 1048575KB->1.00GB
     * 1073741824KB->1.00TB
     * @param traffic
     * @return
     */
    public static String formatTrafficSimple(Long traffic) {
        if (traffic == null) {
            return "";
        }
        if (traffic < TRAFFIC_MB) {
            return traffic + "KB";
        } else if (TRAFFIC_MB <= traffic && traffic < TRAFFIC_GB) {
            BigDecimal bigDecimal = new BigDecimal(traffic);
            bigDecimal = bigDecimal.divide(new BigDecimal(TRAFFIC_MB), 2, RoundingMode.CEILING);
            if (bigDecimal.intValue() == TRAFFIC_MB.intValue()) {
                return "1.00GB";
            } else {
                return bigDecimal + "MB";
            }
        } else if (TRAFFIC_GB <= traffic && traffic < TRAFFIC_TB) {
            BigDecimal bigDecimal = new BigDecimal(traffic);
            bigDecimal = bigDecimal.divide(new BigDecimal(TRAFFIC_GB), 2, RoundingMode.CEILING);
            if (bigDecimal.intValue() == TRAFFIC_MB.intValue()) {
                return "1.00TB";
            }
            return bigDecimal + "GB";
        } else {
            BigDecimal bigDecimal = new BigDecimal(traffic);
            bigDecimal = bigDecimal.divide(new BigDecimal(TRAFFIC_TB), 2, RoundingMode.CEILING);
            // 最高单位只到TB
            return bigDecimal + "TB";
        }
    }
}
