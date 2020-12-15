package de.quarano.sormas.client.api;

import de.quarano.sormas.client.invoker.ApiClient;

import de.quarano.sormas.client.model.InfrastructureChangeDatesDto;
import de.quarano.sormas.client.model.InfrastructureSyncDto;

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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-12-15T22:47:05.366577+01:00[Europe/Berlin]")@Component("de.quarano.sormas.client.api.InfrastructureControllerApi")
public class InfrastructureControllerApi {
    private ApiClient apiClient;

    public InfrastructureControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public InfrastructureControllerApi(ApiClient apiClient) {
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
     * @param body The body parameter
     * @return InfrastructureSyncDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public InfrastructureSyncDto getInfrastructureSyncData(InfrastructureChangeDatesDto body) throws RestClientException {
        Object postBody = body;
        String path = UriComponentsBuilder.fromPath("/infrastructure/sync").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json; charset&#x3D;UTF-8"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "*/*"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basicAuth" };

        ParameterizedTypeReference<InfrastructureSyncDto> returnType = new ParameterizedTypeReference<InfrastructureSyncDto>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
