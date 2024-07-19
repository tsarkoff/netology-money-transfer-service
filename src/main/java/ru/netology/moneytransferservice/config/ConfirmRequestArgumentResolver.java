package ru.netology.moneytransferservice.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.netology.moneytransferservice.repository.ConfirmRequestDto;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ConfirmRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(ConfirmRequestResolver.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws IOException {
        String requestBody = TransferRequestArgumentResolver.getRequestBody(webRequest.getNativeRequest(HttpServletRequest.class));
        return buildTConfirmRequestDto(requestBody);
    }

    private ConfirmRequestDto buildTConfirmRequestDto(String requestBody) {
        JsonElement operationId = null;
        String code = "";
        JsonElement je = JsonParser.parseString(requestBody);
        for (Map.Entry<String, JsonElement> entry : je.getAsJsonObject().entrySet()) {
            if (entry.getValue().isJsonPrimitive()) {
                switch (entry.getKey()) {
                    case "operationId":
                        operationId = entry.getValue();
                        break;
                    case "code":
                        code = String.valueOf(entry.getValue());
                        break;
                }
            }
        }
        return new ConfirmRequestDto(Objects.requireNonNull(operationId).getAsInt(), code);
    }
}