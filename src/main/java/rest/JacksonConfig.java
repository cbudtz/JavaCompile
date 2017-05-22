package rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private ObjectMapper objectMapper;

    public JacksonConfig() {
        objectMapper = new ObjectMapper();
        objectMapper = objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS,true);

    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}