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

import de.quarano.sormas.client.model.CampaignFormDataDto;
import de.quarano.sormas.client.model.PushResult;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CampaignFormDataControllerApi
 */
@Ignore
public class CampaignFormDataControllerApiTest {

    private final CampaignFormDataControllerApi api = new CampaignFormDataControllerApi();

    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAllCampaignFormDataTest() {
        Long since = null;
        List<CampaignFormDataDto> response = api.getAllCampaignFormData(since);

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
    public void getAllUuids2Test() {
        List<String> response = api.getAllUuids2();

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
    public void getByUuids3Test() {
        List<String> body = null;
        List<CampaignFormDataDto> response = api.getByUuids3(body);

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
    public void postCampaignFormDataTest() {
        List<CampaignFormDataDto> body = null;
        List<PushResult> response = api.postCampaignFormData(body);

        // TODO: test validations
    }
}
