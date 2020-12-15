package de.quarano.sormas.client.api;

import de.quarano.sormas.client.invoker.ApiClient;

import de.quarano.sormas.client.model.ExternalVisitDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-12-15T22:47:05.366577+01:00[Europe/Berlin]")@Component("de.quarano.sormas.client.api.ExternalVisitsControllerApi")
public class ExternalVisitsControllerApi {
    private ApiClient apiClient;

    public ExternalVisitsControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public ExternalVisitsControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get follow up end dates
     * Get latest follow up end date assigned to the specified person. Note: Only returns values for persons who have their symptom journal status set to ACCEPTED! Only returns values changed after {since}, which is interpreted as a UNIX timestamp.
     * <p><b>0</b> - List of personUuids and their latest follow up end dates as UNIX timestamps.
     * @param since The since parameter
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getLatestFollowUpEndDates(Long since) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'since' is set
        if (since == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'since' when calling getLatestFollowUpEndDates");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("since", since);
        String path = UriComponentsBuilder.fromPath("/visits-external/followUpEndDates/{since}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Get person information
     * Get some personal data for a specific person
     * <p><b>0</b> - A selection of personal data, including first and last name, e-mail, phone number(s) and birth date if availablefor that person. Note that Null value fields may not be returned. If you get an unexpected result, it might help to verifyif the personUuid is existing in your system via the isValid controller.
     * @param personUuid The personUuid parameter
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getPersonByUuid(String personUuid) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'personUuid' is set
        if (personUuid == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'personUuid' when calling getPersonByUuid");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("personUuid", personUuid);
        String path = UriComponentsBuilder.fromPath("/visits-external/person/{personUuid}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Get API version
     * 
     * <p><b>0</b> - The minimal version needed for compatibility with the external ReST API of SORMAS.
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String getVersion() throws RestClientException {
        Object postBody = null;
        String path = UriComponentsBuilder.fromPath("/visits-external/version").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Check person validity
     * Check if a the Uuid given as parameter exists in SORMAS.
     * <p><b>0</b> - true a person with the given Uuid exists in SORMAS, false otherwise.
     * @param personUuid The personUuid parameter
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String isValidPersonUuid(String personUuid) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'personUuid' is set
        if (personUuid == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'personUuid' when calling isValidPersonUuid");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("personUuid", personUuid);
        String path = UriComponentsBuilder.fromPath("/visits-external/person/{personUuid}/isValid").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Save visits
     * Upload visits with all symptom and disease related data to SORMAS.
     * <p><b>0</b> - OK when visit was successfully saved, ERROR otherwise.
     * @param body The body parameter
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String postExternalVisits(List<ExternalVisitDto> body) throws RestClientException {
        Object postBody = body;
        String path = UriComponentsBuilder.fromPath("/visits-external").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Save symptom journal status
     * 
     * <p><b>0</b> - true if the status was set succesfully, false otherwise.
     * @param personUuid The personUuid parameter
     * @param body status may be one of the following:&lt;br/&gt;UNREGISTERED: User has not yet sent any state&lt;br/&gt;REGISTERED: After successful registration in SymptomJournal&lt;br/&gt;ACCEPTED: User has accepted a confirmation&lt;br/&gt;REJECTED: User has rejected (declined) a confirmation&lt;br/&gt;DELETED: User was deleted
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String postSymptomJournalStatus(String personUuid, String body) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'personUuid' is set
        if (personUuid == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'personUuid' when calling postSymptomJournalStatus");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("personUuid", personUuid);
        String path = UriComponentsBuilder.fromPath("/visits-external/person/{personUuid}/status").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
