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
import de.quarano.sormas.client.model.WeeklyReportDto;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for WeeklyReportControllerApi
 */
@Ignore
public class WeeklyReportControllerApiTest {

    private final WeeklyReportControllerApi api = new WeeklyReportControllerApi();

    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAllUuids17Test() {
        List<String> response = api.getAllUuids17();

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
    public void getAllWeeklyReportsTest() {
        Long since = null;
        List<WeeklyReportDto> response = api.getAllWeeklyReports(since);

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
    public void getByUuids27Test() {
        List<String> body = null;
        List<WeeklyReportDto> response = api.getByUuids27(body);

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
    public void postWeeklyReportsTest() {
        List<WeeklyReportDto> body = null;
        List<PushResult> response = api.postWeeklyReports(body);

        // TODO: test validations
    }
}
