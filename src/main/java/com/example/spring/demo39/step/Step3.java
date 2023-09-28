package org.springframework.boot;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class Step3 {
    public static void main(String[] args) throws IOException {

        ApplicationEnvironment env = new ApplicationEnvironment(); // 系统环境变量，properties, yml
        // 添加新的参数来源
        env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("application.properties")));
        env.getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }
        System.out.println(env.getProperty("DATAGRIP_VM_OPTIONS"));
        System.out.println(env.getProperty("server.port"));

    }
}
