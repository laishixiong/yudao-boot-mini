package cn.iocoder.yudao.framework.common.util.object;

import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * {@link cn.iocoder.yudao.framework.common.pojo.PageParam} 工具类
 *
 * @author changebylsx
 */
public class PageUtils {

    public static int getStart(PageParam pageParam) {
        return (pageParam.getPageNo() - 1) * pageParam.getPageSize();
    }

}
