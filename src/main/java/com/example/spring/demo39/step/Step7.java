package org.springframework.boot;


import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.Map;

public class Step7 {
    public static void main(String[] args) {
        ApplicationEnvironment env = new ApplicationEnvironment();
        SpringApplicationBannerPrinter bannerPrinter = new SpringApplicationBannerPrinter(
                new DefaultResourceLoader(),
                new SpringBootBanner()
        );

        // 测试文字bannner
//        env.getPropertySources()
//                .addLast(new MapPropertySource("banner", Map.of("spring.banner.location", "banner1.txt")));

        // 测试图片bannner
//        env.getPropertySources()
//                .addLast(new MapPropertySource("banner", Map.of("spring.banner.location", "banner1.jpg")));

        // 版本号的获取
        System.out.println(SpringBootVersion
                .getVersion());
        bannerPrinter.print(env,Step7.class,System.out);

    }
}
