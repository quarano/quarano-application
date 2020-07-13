# FacilityControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                                  | HTTP request                                    | Description |
| ----------------------------------------------------------------------- | ----------------------------------------------- | ----------- |
| [**getAllByRegion**](FacilityControllerApi.md#getAllByRegion)           | **GET** /facilities/region/{regionUuid}/{since} |
| [**getAllUuids5**](FacilityControllerApi.md#getAllUuids5)               | **GET** /facilities/uuids                       |
| [**getAllWithoutRegion**](FacilityControllerApi.md#getAllWithoutRegion) | **GET** /facilities/general/{since}             |
| [**getByUuids10**](FacilityControllerApi.md#getByUuids10)               | **POST** /facilities/query                      |

<a name="getAllByRegion"></a>

# **getAllByRegion**

> List&lt;FacilityDto&gt; getAllByRegion(regionUuid, since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

FacilityControllerApi apiInstance = new FacilityControllerApi();
String regionUuid = "regionUuid_example"; // String |
Long since = 789L; // Long |
try {
    List<FacilityDto> result = apiInstance.getAllByRegion(regionUuid, since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllByRegion");
    e.printStackTrace();
}
```

### Parameters

| Name           | Type       | Description | Notes |
| -------------- | ---------- | ----------- | ----- |
| **regionUuid** | **String** |             |
| **since**      | **Long**   |             |

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids5"></a>

# **getAllUuids5**

> List&lt;String&gt; getAllUuids5()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

FacilityControllerApi apiInstance = new FacilityControllerApi();
try {
    List<String> result = apiInstance.getAllUuids5();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllUuids5");
    e.printStackTrace();
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

**List&lt;String&gt;**

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllWithoutRegion"></a>

# **getAllWithoutRegion**

> List&lt;FacilityDto&gt; getAllWithoutRegion(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

FacilityControllerApi apiInstance = new FacilityControllerApi();
Long since = 789L; // Long |
try {
    List<FacilityDto> result = apiInstance.getAllWithoutRegion(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getAllWithoutRegion");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getByUuids10"></a>

# **getByUuids10**

> List&lt;FacilityDto&gt; getByUuids10(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.FacilityControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

FacilityControllerApi apiInstance = new FacilityControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<FacilityDto> result = apiInstance.getByUuids10(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FacilityControllerApi#getByUuids10");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;FacilityDto&gt;**](FacilityDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: _/_
- **Accept**: application/json; charset=UTF-8
