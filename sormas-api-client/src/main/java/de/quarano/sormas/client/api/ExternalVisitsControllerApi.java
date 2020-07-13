package de.quarano.sormas.client.api;

import de.quarano.sormas.client.invoker.ApiClient;

import de.quarano.sormas.client.model.ExternalVisitDto;
import de.quarano.sormas.client.model.PersonQuarantineEndDto;
import de.quarano.sormas.client.model.PushResult;

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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-07-09T17:56:16.099120+02:00[Europe/Berlin]")@Component("de.quarano.sormas.client.api.ExternalVisitsControllerApi")
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
     * 
     * 
     * <p><b>0</b> - default response
     * @param since The since parameter
     * @return List&lt;PersonQuarantineEndDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<PersonQuarantineEndDto> getLatestQuarantineEndDates(Long since) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'since' is set
        if (since == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'since' when calling getLatestQuarantineEndDates");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("since", since);
        String path = UriComponentsBuilder.fromPath("/visits-external/quarantineEndDates/{since}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "http-basic" };

        ParameterizedTypeReference<List<PersonQuarantineEndDto>> returnType = new ParameterizedTypeReference<List<PersonQuarantineEndDto>>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * 
     * <p><b>0</b> - default response
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

        String[] authNames = new String[] { "http-basic" };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * 
     * <p><b>0</b> - default response
     * @param personUuid The personUuid parameter
     * @return Boolean
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Boolean isValidPersonUuid(String personUuid) throws RestClientException {
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

        String[] authNames = new String[] { "http-basic" };

        ParameterizedTypeReference<Boolean> returnType = new ParameterizedTypeReference<Boolean>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * 
     * <p><b>0</b> - default response
     * @param body The body parameter
     * @return List&lt;PushResult&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<PushResult> postExternalVisits(List<ExternalVisitDto> body) throws RestClientException {
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

        String[] authNames = new String[] { "http-basic" };

        ParameterizedTypeReference<List<PushResult>> returnType = new ParameterizedTypeReference<List<PushResult>>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
