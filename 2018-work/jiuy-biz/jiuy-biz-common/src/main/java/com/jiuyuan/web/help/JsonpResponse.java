package com.jiuyuan.web.help;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.yujj.ext.jackson.spring.ObjectMapperFactory;

public class JsonpResponse extends JsonResponse implements JsonSerializable {

    public static final Pattern CALLBACK_PATTERN = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");

    private String jsonpCallback;

    @Override
    public JsonpResponse setCode(int code) {
        return (JsonpResponse) super.setCode(code);
    }

    @Override
    public JsonpResponse setData(Object data) {
        return (JsonpResponse) super.setData(data);
    }

    @Override
    public JsonpResponse setError(String error) {
        return (JsonpResponse) super.setError(error);
    }

    @Override
    public JsonpResponse setSuccessful() {
        return (JsonpResponse) super.setSuccessful();
    }

    public JsonpResponse(String jsonpCallback) {
        if (StringUtils.isBlank(jsonpCallback)) {
            this.jsonpCallback = null;
        } else {
            if (!CALLBACK_PATTERN.matcher(jsonpCallback).matches()) {
                jsonpCallback = "callback";
            }
            this.jsonpCallback = jsonpCallback;
        }
    }

    @Override
    public void serialize(JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (StringUtils.isNotBlank(jsonpCallback)) {
            jgen.writeRaw("\n"); // prevent jsonp utf-7 injection attack
            jgen.writeRaw(jsonpCallback);
            jgen.writeRaw('(');
        }

        provider.findTypedValueSerializer(getClass().getSuperclass(), true, null).serialize(this, jgen, provider);

        if (StringUtils.isNotBlank(jsonpCallback)) {
            jgen.writeRaw(");");
        }
    }

    @Override
    public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
        throws IOException, JsonProcessingException {
        // No type for JSONP wrapping: value serializer will handle typing for value:
        serialize(jgen, provider);
    }

}
