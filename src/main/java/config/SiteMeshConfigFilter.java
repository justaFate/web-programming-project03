package config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.DispatchMode;

public class SiteMeshConfigFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.setDispatchMode(DispatchMode.INCLUDE)
               .addDecoratorPath("/*", "/main.jsp")
               .addExcludedPath("/image*")
               .addExcludedPath("/assets/*")
               .addExcludedPath("/static/*");
    }
}
