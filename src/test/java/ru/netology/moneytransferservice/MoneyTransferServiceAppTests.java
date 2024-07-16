package ru.netology.moneytransferservice;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.moneytransferservice.repository.Amount;
import ru.netology.moneytransferservice.repository.ConfirmRequestDto;
import ru.netology.moneytransferservice.repository.Currency;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class MoneyTransferServiceAppTests {
    private static final Set<String> opIds = ConcurrentHashMap.newKeySet();

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private final static GenericContainer<?> moneyApp = new GenericContainer<>("moneyapp")
            .withExposedPorts(5500);

    @Test
    @Order(1)
    public void contextLoads() {
        System.out.println("moneyapp container is listening on port:" + moneyApp.getMappedPort(5500));
    }

    @ParameterizedTest
    @Order(2)
    @CsvSource(value = {
            "3333000033330000,      03/25,      333,    4444000044440000,   10000,      RUR,        200 OK",
            "333300003333000011,    03/25,      333,    4444000044440000,   10000,      RUR,        400 BAD_REQUEST",
            "3333000033330000,      03/1125,    333,    4444000044440000,   10000,      RUR,        400 BAD_REQUEST",
            "3333000033330000,      1103/25,    333,    4444000044440000,   10000,      RUR,        400 BAD_REQUEST",
            "3333000033330000,      03/25,      11333,  4444000044440000,   10000,      RUR,        400 BAD_REQUEST",
            "3333000033330000,      03/25,      1333,   444400004444000011, 10000,      RUR,        400 BAD_REQUEST",
            "3333000033330000,      03/25,      1333,   444400004444000011, 1000000000, RUR,        400 BAD_REQUEST",
            "3333000033330000,      03/25,      333,    4444000044440000,   10000,      RURRRRR,    400 BAD_REQUEST",
            "5555000055550000,      05/25,      555,    2222000022220000,   5000000,    RUR,        200 OK",
    }, ignoreLeadingAndTrailingWhitespace = true)
    public void testTransfer(String cardFormNumber, String cardFromValidTill, String cardFromCVV, String cardToNumber, int amountValue, String amountCurrency, String expected) {
        /*Why gson.toJson(request) response doesn't work if response is the same JSON just w/o LFRC =>
            String jsonRequest = buildRequestBody(cardFormNumber, cardFromValidTill, cardFromCVV, cardToNumber, amountValue, amountCurrency);*/
        String jsonRequest = format("""
                        {
                          "cardFromNumber": "%s",
                          "cardFromValidTill": "%s",
                          "cardFromCVV": "%s",
                          "cardToNumber": "%s",
                          "amount": {
                            "value": %d,
                            "currency": "%s"
                          }
                        }""",
                cardFormNumber, cardFromValidTill, cardFromCVV, cardToNumber, amountValue, amountCurrency);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + moneyApp.getMappedPort(5500) + "/transfer",
                jsonRequest,
                String.class);

        if (response.getStatusCode().toString().equals("200 OK")) {
            String body = response.getBody();
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(body, JsonElement.class);
            String operationId = jsonElement.getAsJsonObject().get("operationId").getAsString();
            opIds.add(String.valueOf(operationId));
        }

        System.out.println(response);
        String statusCode = response.getStatusCode().toString();
        Assertions.assertEquals(expected, statusCode);
    }

    @Test
    @Order(3)
    public void testConfirm() {
        Gson gson = new Gson();
        for (String opId : opIds) {
            ConfirmRequestDto confirmRequestDto = new ConfirmRequestDto(opId, "0000");
            String request = gson.toJson(confirmRequestDto);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:" + moneyApp.getMappedPort(5500) + "/confirmOperation",
                    request,
                    String.class);
            String body = response.getBody();
            JsonElement jsonElement = gson.fromJson(body, JsonElement.class);
            String operationId = jsonElement.getAsJsonObject().get("operationId").getAsString();
            System.out.println(response);
            Assertions.assertEquals(opId, operationId);
        }
    }

    private String buildRequestBody(String cardFormNumber, String cardFromValidTill, String cardFromCVV, String cardToNumber, int amountValue, String amountCurrency) {
        Object request = new FlatTransferRequestDto(
                cardFormNumber,
                cardFromValidTill,
                cardFromCVV,
                cardToNumber,
                new Amount(amountValue, Currency.valueOf(amountCurrency))
        );
        Gson gson = new Gson();
        return gson.toJson(request);
    }
}
