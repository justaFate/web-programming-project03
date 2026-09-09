package config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteMeshFilterConfig {

    @Bean
    public FilterRegistrationBean<SiteMeshConfigFilter> siteMeshFilter() {
        FilterRegistrationBean<SiteMeshConfigFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new SiteMeshConfigFilter());
        filter.addUrlPatterns("/*");
        filter.setOrder(1);
        return filter;
    }
}
