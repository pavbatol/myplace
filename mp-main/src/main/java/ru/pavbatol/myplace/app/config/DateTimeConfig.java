package ru.pavbatol.myplace.app.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import ru.pavbatol.myplace.app.annotation.ExcludeJacocoGenerated;

import java.time.format.DateTimeFormatter;

// TODO: 13.09.2023 Remove 'ExcludeJacocoGenerated' annotation when developing this service

@Configuration
@ExcludeJacocoGenerated
public class DateTimeConfig implements WebFluxConfigurer {
    @Value("${app.format.date}")
    private String dateFormat;

    @Value("${app.format.date-time}")
    private String dateTimeFormat;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.simpleDateFormat(dateTimeFormat)
                .serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)))
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)))
                .deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)))
                .deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(dateFormat));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(dateTimeFormat));
        registrar.registerFormatters(registry);
    }
}
