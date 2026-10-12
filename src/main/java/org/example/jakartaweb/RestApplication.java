package org.example.jakartaweb;

import org.example.jakartaweb.validation.ConstraintViolationExceptionMapper;
import org.example.jakartaweb.validation.NotFoundExceptionMapper;
import org.example.jakartaweb.validation.ValidationExceptionMapper;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Register endpoints manually to override server configurations
        try {
            classes.add(Class.forName("org.example.jakartaweb.resource.PetResource"));
        } catch (ClassNotFoundException e) {
            // Fallback if packages differ dynamically
        }

        // Force register your custom validation exception mappers to bypass WildFly defaults
        classes.add(ConstraintViolationExceptionMapper.class);
        classes.add(ValidationExceptionMapper.class);
        classes.add(NotFoundExceptionMapper.class);

        return classes;
    }
}


