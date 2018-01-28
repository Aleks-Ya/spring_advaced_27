package booking.web.config;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
@Configuration
public class FreeMarkerConfig {

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        BeansWrapperBuilder builder = new BeansWrapperBuilder(freemarker.template.Configuration.VERSION_2_3_21);
        BeansWrapper beansWrapper = builder.build();

        Map<String, Object> sharedVariables = new HashMap<>();
        sharedVariables.put("statics", beansWrapper.getStaticModels());

        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("WEB-INF/views/ftl/");
        freeMarkerConfigurer.setFreemarkerVariables(sharedVariables);
        return freeMarkerConfigurer;
    }
}
