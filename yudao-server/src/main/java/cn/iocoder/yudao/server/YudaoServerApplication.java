package cn.iocoder.yudao.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 项目的启动类
 *
 * @author changebylsx
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${yudao.info.base-package}
@SpringBootApplication(scanBasePackages = {"${yudao.info.base-package}.server", "${yudao.info.base-package}.module"}) //配置文件扫包
@Slf4j
public class YudaoServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(YudaoServerApplication.class, args);
        log.info("配置文件环境：{}", context.getEnvironment().getProperty("spring.profiles.active"));
    }

}
