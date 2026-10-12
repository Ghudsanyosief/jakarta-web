package org.example.jakartaweb.config;


import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonbConfigResolver implements ContextResolver<Jsonb> {

    @Override
    public Jsonb getContext(Class<?> type) {
        // Denna inställning tvingar WildFly att lyssna på @JsonbPropertyOrder!
        JsonbConfig config = new JsonbConfig();
        return JsonbBuilder.create(config);
    }
}

