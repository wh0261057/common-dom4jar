package org.dom4jar;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

@Component
public class SpringContext {
    @Bean
    @Qualifier("appContext")
    public WebApplicationContext getContext(){
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        return context;
    }
}
