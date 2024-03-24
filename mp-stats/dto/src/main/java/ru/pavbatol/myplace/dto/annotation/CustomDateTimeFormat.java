package ru.pavbatol.myplace.dto.annotation;

import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
public @interface CustomDateTimeFormat {
}
