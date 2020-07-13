# PersonControllerApi

All URIs are relative to _/sormas-rest_

| Method                                                    | HTTP request                 | Description |
| --------------------------------------------------------- | ---------------------------- | ----------- |
| [**getAllPersons**](PersonControllerApi.md#getAllPersons) | **GET** /persons/all/{since} |
| [**getAllUuids7**](PersonControllerApi.md#getAllUuids7)   | **GET** /persons/uuids       |
| [**getByUuids13**](PersonControllerApi.md#getByUuids13)   | **POST** /persons/query      |
| [**postPersons**](PersonControllerApi.md#postPersons)     | **POST** /persons/push       |

<a name="getAllPersons"></a>

# **getAllPersons**

> List&lt;PersonDto&gt; getAllPersons(since)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PersonControllerApi apiInstance = new PersonControllerApi();
Long since = 789L; // Long |
try {
    List<PersonDto> result = apiInstance.getAllPersons(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getAllPersons");
    e.printStackTrace();
}
```

### Parameters

| Name      | Type     | Description | Notes |
| --------- | -------- | ----------- | ----- |
| **since** | **Long** |             |

### Return type

[**List&lt;PersonDto&gt;**](PersonDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=UTF-8

<a name="getAllUuids7"></a>

# **getAllUuids7**

> List&lt;String&gt; getAllUuids7()

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PersonControllerApi apiInstance = new PersonControllerApi();
try {
    List<String> result = apiInstance.getAllUuids7();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getAllUuids7");
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

<a name="getByUuids13"></a>

# **getByUuids13**

> List&lt;PersonDto&gt; getByUuids13(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PersonControllerApi apiInstance = new PersonControllerApi();
List<String> body = Arrays.asList("body_example"); // List<String> |
try {
    List<PersonDto> result = apiInstance.getByUuids13(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#getByUuids13");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                | Description | Notes      |
| -------- | ----------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;String&gt;**](String.md) |             | [optional] |

### Return type

[**List&lt;PersonDto&gt;**](PersonDto.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8

<a name="postPersons"></a>

# **postPersons**

> List&lt;PushResult&gt; postPersons(body)

### Example

```java
// Import classes:
//import de.quarano.sormas.client.invoker.ApiClient;
//import de.quarano.sormas.client.invoker.ApiException;
//import de.quarano.sormas.client.invoker.Configuration;
//import de.quarano.sormas.client.invoker.auth.*;
//import de.quarano.sormas.client.api.PersonControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();
// Configure HTTP basic authorization: http-basic
HttpBasicAuth http-basic = (HttpBasicAuth) defaultClient.getAuthentication("http-basic");
http-basic.setUsername("YOUR USERNAME");
http-basic.setPassword("YOUR PASSWORD");

PersonControllerApi apiInstance = new PersonControllerApi();
List<PersonDto> body = Arrays.asList(new PersonDto()); // List<PersonDto> |
try {
    List<PushResult> result = apiInstance.postPersons(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PersonControllerApi#postPersons");
    e.printStackTrace();
}
```

### Parameters

| Name     | Type                                      | Description | Notes      |
| -------- | ----------------------------------------- | ----------- | ---------- |
| **body** | [**List&lt;PersonDto&gt;**](PersonDto.md) |             | [optional] |

### Return type

[**List&lt;PushResult&gt;**](PushResult.md)

### Authorization

[http-basic](../README.md#http-basic)

### HTTP request headers

- **Content-Type**: application/json; charset=UTF-8
- **Accept**: application/json; charset=UTF-8
