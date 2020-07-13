# ExternalVisitsControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                                        | HTTP request                                         | Description |
| --------------------------------------------------------------------------------------------- | ---------------------------------------------------- | ----------- |
| [**getLatestQuarantineEndDates**](ExternalVisitsControllerApi.md#getLatestQuarantineEndDates) | **GET** /visits-external/quarantineEndDates/{since}  |
| [**getVersion**](ExternalVisitsControllerApi.md#getVersion)                                   | **GET** /visits-external/version                     |
| [**isValidPersonUuid**](ExternalVisitsControllerApi.md#isValidPersonUuid)                     | **GET** /visits-external/person/{personUuid}/isValid |
| [**postExternalVisits**](ExternalVisitsControllerApi.md#postExternalVisits)                   | **POST** /visits-external                            |

<a name="getLatestQuarantineEndDates"></a>

# **getLatestQuarantineEndDates**

> List&lt;PersonQuarantineEndDto&gt; getLatestQuarantineEndDates(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
Long since = 789L; // Long |
try {
    List<PersonQuarantineEndDto> result = apiInstance.getLatestQuarantineEndDates(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#getLatestQuarantineEndDates");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;PersonQuarantineEndDto&gt;**](PersonQuarantineEndDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getVersion"></a>

# **getVersion**

> String getVersion()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

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

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="isValidPersonUuid"></a>

# **isValidPersonUuid**

> Boolean isValidPersonUuid(personUuid)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
String personUuid = "personUuid_example"; // String |
try {
    Boolean result = apiInstance.isValidPersonUuid(personUuid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#isValidPersonUuid");
    e.printStackTrace();
}
```

### Parameters

| Name           | Type       | Description | Notes |
| -------------- | ---------- | ----------- | ----- |
| **personUuid** | **String** |             |

### Return type

**Boolean**

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="postExternalVisits"></a>

# **postExternalVisits**

> List&lt;PushResult&gt; postExternalVisits(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.ExternalVisitsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

ExternalVisitsControllerApi apiInstance = new ExternalVisitsControllerApi();
List<ExternalVisitDto> body = Arrays.asList(new ExternalVisitDto()); // List<ExternalVisitDto> |
try {
    List<PushResult> result = apiInstance.postExternalVisits(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExternalVisitsControllerApi#postExternalVisits");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                                    | Description | Notes      |
| -------- | ------------------------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;ExternalVisitDto&gt;**](ExternalVisitDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
