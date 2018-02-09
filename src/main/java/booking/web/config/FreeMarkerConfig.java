package booking.web.config;

import booking.web.freemarker.CurrentUserAccountMethod;
import booking.web.freemarker.CurrentUserMethod;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import({CurrentUserMethod.class, CurrentUserAccountMethod.class})
public class FreeMarkerConfig {

    private final CurrentUserMethod currentUserMethod;
    private final CurrentUserAccountMethod currentUserAccountMethod;

    @Autowired
    public FreeMarkerConfig(CurrentUserMethod currentUserMethod, CurrentUserAccountMethod currentUserAccountMethod) {
        this.currentUserMethod = currentUserMethod;
        this.currentUserAccountMethod = currentUserAccountMethod;
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
        BeansWrapperBuilder builder = new BeansWrapperBuilder(freemarker.template.Configuration.VERSION_2_3_21);
        BeansWrapper beansWrapper = builder.build();

        Map<String, Object> sharedVariables = new HashMap<>();
        sharedVariables.put("currentUser", currentUserMethod);
        sharedVariables.put("currentAccount", currentUserAccountMethod);
        sharedVariables.put("statics", beansWrapper.getStaticModels());

        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("WEB-INF/views/ftl/");
        freeMarkerConfigurer.setFreemarkerVariables(sharedVariables);
        return freeMarkerConfigurer;
    }
}
