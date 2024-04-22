package com.example.cashcard.metaAnnotation;

import org.springframework.security.core.annotation.CurrentSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Мета аннотация, для того, чтобы каждый раз не вводить для поиска пользователя
// Мета аннотации позволяют на основе данных создавать свои аннотации
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@CurrentSecurityContext(expression = "authentication.name")
public @interface CurrentOwner {
}
