package booking.web.config;

import booking.web.freemarker.CurrentUserMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
@Configuration
@Import(CurrentUserMethod.class)
public class FreeMarkerConfig {

    private final CurrentUserMethod currentUserMethod;

    @Autowired
    public FreeMarkerConfig(CurrentUserMethod currentUserMethod) {
        this.currentUserMethod = currentUserMethod;
    }

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
        Map<String, Object> sharedVariables = new HashMap<>();
        sharedVariables.put("currentUser", currentUserMethod);

        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("WEB-INF/views/ftl/");
        freeMarkerConfigurer.setFreemarkerVariables(sharedVariables);
        return freeMarkerConfigurer;
    }
}
