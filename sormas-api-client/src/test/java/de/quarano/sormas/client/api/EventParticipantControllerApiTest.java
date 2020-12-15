/*
 * SORMAS REST API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.52.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package de.quarano.sormas.client.api;

import de.quarano.sormas.client.model.EventParticipantDto;
import de.quarano.sormas.client.model.PushResult;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for EventParticipantControllerApi
 */
@Ignore
public class EventParticipantControllerApiTest {

    private final EventParticipantControllerApi api = new EventParticipantControllerApi();

    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAllActiveUuids3Test() {
        List<String> response = api.getAllActiveUuids3();

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAllEventParticipantsAfterTest() {
        Long since = null;
        List<EventParticipantDto> response = api.getAllEventParticipantsAfter(since);

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getByUuids13Test() {
        List<String> body = null;
        List<EventParticipantDto> response = api.getByUuids13(body);

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getDeletedUuidsSince2Test() {
        Long since = null;
        List<String> response = api.getDeletedUuidsSince2(since);

        // TODO: test validations
    }
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void postEventParticipantsTest() {
        List<EventParticipantDto> body = null;
        List<PushResult> response = api.postEventParticipants(body);

        // TODO: test validations
    }
}
