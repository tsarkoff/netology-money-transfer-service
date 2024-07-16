package ru.netology.moneytransferservice.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.netology.moneytransferservice.repository.Amount;
import ru.netology.moneytransferservice.repository.Card;
import ru.netology.moneytransferservice.repository.Currency;
import ru.netology.moneytransferservice.repository.TransferRequestDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class TransferRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(TransferRequestResolver.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws IOException {
        String requestBody = getRequestBody(webRequest.getNativeRequest(HttpServletRequest.class));
        return buildTransferRequestDto(requestBody);
    }

    public static String getRequestBody(HttpServletRequest request) {
        String postBody;
        try (BufferedReader reader = Objects.requireNonNull(request).getReader()) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            postBody = body.toString();
        } catch (IOException e) {
            throw new InternalException("Transfer/Confirm RequestArgumentResolver.getRequestBody failed", e);
        }
        return postBody;
    }

    private TransferRequestDto buildTransferRequestDto(String requestBody) {
        Card cardFrom = new Card();
        String cardToNumber = "";
        int amountValue = 0;
        Currency currency = Currency.RUR;
        JsonElement je = JsonParser.parseString(requestBody);
        for (Map.Entry<String, JsonElement> entry : je.getAsJsonObject().entrySet()) {
            if (entry.getValue().isJsonPrimitive()) {
                switch (entry.getKey()) {
                    case "cardFromNumber":
                        cardFrom.setNumber(String.valueOf(entry.getValue()));
                        break;
                    case "cardFromValidTill":
                        cardFrom.setValidTill(String.valueOf(entry.getValue()));
                        break;
                    case "cardFromCVV":
                        cardFrom.setCVV(String.valueOf(entry.getValue()));
                        break;
                    case "cardToNumber":
                        cardToNumber = String.valueOf(entry.getValue());
                        break;
                }
            } else if (entry.getValue().isJsonObject()) {
                if (entry.getKey().equals("amount")) {
                    JsonElement money = entry.getValue();
                    for (Map.Entry<String, JsonElement> amountAttrs : money.getAsJsonObject().entrySet()) {
                        switch (amountAttrs.getKey()) {
                            case "value":
                                amountValue = Integer.parseInt(String.valueOf(amountAttrs.getValue()));
                                break;
                            case "currency":
                                currency = Currency.valueOf(String.valueOf(amountAttrs.getValue()).replace("\"", ""));
                                break;
                        }
                    }
                }
            }
        }
        return new TransferRequestDto(cardFrom, cardToNumber, new Amount(amountValue, currency));
    }
}