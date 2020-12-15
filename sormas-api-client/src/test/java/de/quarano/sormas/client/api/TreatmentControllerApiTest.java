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

import de.quarano.sormas.client.model.PushResult;
import de.quarano.sormas.client.model.TreatmentDto;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for TreatmentControllerApi
 */
@Ignore
public class TreatmentControllerApiTest {

    private final TreatmentControllerApi api = new TreatmentControllerApi();

    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAllActiveUuids9Test() {
        List<String> response = api.getAllActiveUuids9();

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
    public void getAllTreatmentsTest() {
        Long since = null;
        List<TreatmentDto> response = api.getAllTreatments(since);

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
    public void getByUuids24Test() {
        List<String> body = null;
        List<TreatmentDto> response = api.getByUuids24(body);

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
    public void postTreatmentsTest() {
        List<TreatmentDto> body = null;
        List<PushResult> response = api.postTreatments(body);

        // TODO: test validations
    }
}
