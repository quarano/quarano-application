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

import de.quarano.sormas.client.model.EventDto;
import de.quarano.sormas.client.model.PushResult;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for EventControllerApi
 */
@Ignore
public class EventControllerApiTest {

    private final EventControllerApi api = new EventControllerApi();

    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAllActiveUuids4Test() {
        List<String> response = api.getAllActiveUuids4();

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
    public void getAllEventsTest() {
        Long since = null;
        List<EventDto> response = api.getAllEvents(since);

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
    public void getArchivedUuidsSince1Test() {
        Long since = null;
        List<String> response = api.getArchivedUuidsSince1(since);

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
    public void getByUuids14Test() {
        List<String> body = null;
        List<EventDto> response = api.getByUuids14(body);

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
    public void getDeletedUuidsSince3Test() {
        Long since = null;
        List<String> response = api.getDeletedUuidsSince3(since);

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
    public void postEventsTest() {
        List<EventDto> body = null;
        List<PushResult> response = api.postEvents(body);

        // TODO: test validations
    }
}
