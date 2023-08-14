package ru.pavbatol.myplace.server.app.context;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public final class ApplicationContextProvider implements ApplicationContextAware {

    @Getter
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext ctx) throws BeansException {
        context = ctx;
    }
}
