package cn.iocoder.yudao.framework.apilog.core.service;

/**
 * API 错误日志 Framework Service 接口
 *
 * @author changebylsx
 */
public interface ApiErrorLogFrameworkService {

    /**
     * 创建 API 错误日志
     *
     * @param apiErrorLog API 错误日志
     */
    void createApiErrorLog(ApiErrorLog apiErrorLog);
}
