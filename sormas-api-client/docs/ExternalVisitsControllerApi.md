# ExternalVisitsControllerApi

All URIs are relative to */sormas-rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getLatestFollowUpEndDates**](ExternalVisitsControllerApi.md#getLatestFollowUpEndDates) | **GET** /visits-external/followUpEndDates/{since} | Get follow up end dates
[**getPersonByUuid**](ExternalVisitsControllerApi.md#getPersonByUuid) | **GET** /visits-external/person/{personUuid} | Get person information
[**getVersion**](ExternalVisitsControllerApi.md#getVersion) | **GET** /visits-external/version | Get API version
[**isValidPersonUuid**](ExternalVisitsControllerApi.md#isValidPersonUuid) | **GET** /visits-external/person/{personUuid}/isValid | Check person validity
[**postExternalVisits**](ExternalVisitsControllerApi.md#postExternalVisits) | **POST** /visits-external | Save visits
[**postSymptomJournalStatus**](ExternalVisitsControllerApi.md#postSymptomJournalStatus) | **POST** /visits-external/person/{personUuid}/status | Save symptom journal status

<a name="getLatestFollowUpEndDates"></a>
# **getLatestFollowUpEndDates**
> String getLatestFollowUpEndDates(since)

Get follow up end dates

Get latest follow up end date assigned to the specified person. Note: Only returns values for persons who have their symptom journal status set to ACCEPTED! Only returns values changed after {since}, which is interpreted as a UNIX timestamp.

### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
Long since = 789L; // Long | 
try {
    String result = apiInstance.getLatestFollowUpEndDates(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#getLatestFollowUpEndDates");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Long**|  |

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="getPersonByUuid"></a>
# **getPersonByUuid**
> String getPersonByUuid(personUuid)

Get person information

Get some personal data for a specific person

### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
String personUuid = "personUuid_example"; // String | 
try {
    String result = apiInstance.getPersonByUuid(personUuid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#getPersonByUuid");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **personUuid** | **String**|  |

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getVersion"></a>
# **getVersion**
> String getVersion()

Get API version

### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
try {
    String result = apiInstance.getVersion();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#getVersion");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="isValidPersonUuid"></a>
# **isValidPersonUuid**
> String isValidPersonUuid(personUuid)

Check person validity

Check if a the Uuid given as parameter exists in SORMAS.

### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
String personUuid = "personUuid_example"; // String | 
try {
    String result = apiInstance.isValidPersonUuid(personUuid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#isValidPersonUuid");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **personUuid** | **String**|  |

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8

<a name="postExternalVisits"></a>
# **postExternalVisits**
> String postExternalVisits(body)

Save visits

Upload visits with all symptom and disease related data to SORMAS.

### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
List<ExternalVisitDto> body = Arrays.asList(new ExternalVisitDto()); // List<ExternalVisitDto> | 
try {
    String result = apiInstance.postExternalVisits(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#postExternalVisits");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**List&lt;ExternalVisitDto&gt;**](ExternalVisitDto.md)|  | [optional]

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

<a name="postSymptomJournalStatus"></a>
# **postSymptomJournalStatus**
> String postSymptomJournalStatus(personUuid, body)

Save symptom journal status

### Example
```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
String personUuid = "personUuid_example"; // String | 
String body = "body_example"; // String | status may be one of the following:<br/>UNREGISTERED: User has not yet sent any state<br/>REGISTERED: After successful registration in SymptomJournal<br/>ACCEPTED: User has accepted a confirmation<br/>REJECTED: User has rejected (declined) a confirmation<br/>DELETED: User was deleted
try {
    String result = apiInstance.postSymptomJournalStatus(personUuid, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#postSymptomJournalStatus");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **personUuid** | **String**|  |
 **body** | [**String**](String.md)| status may be one of the following:&lt;br/&gt;UNREGISTERED: User has not yet sent any state&lt;br/&gt;REGISTERED: After successful registration in SymptomJournal&lt;br/&gt;ACCEPTED: User has accepted a confirmation&lt;br/&gt;REJECTED: User has rejected (declined) a confirmation&lt;br/&gt;DELETED: User was deleted | [optional]

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json; charset=UTF-8
 - **Accept**: application/json; charset=UTF-8

