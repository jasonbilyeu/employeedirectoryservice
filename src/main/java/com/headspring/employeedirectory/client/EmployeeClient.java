package com.headspring.employeedirectory.client;

import com.headspring.employeedirectory.controllers.EmployeeController;
import com.headspring.employeedirectory.db.Employee;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

public class EmployeeClient {

    private static URI baseUri;

    private final RestTemplate restTemplate = getRestTemplate();

    private String email;

    private String password;

    public EmployeeClient(String hostname, int port) {
        baseUri = URI.create(String.format("http://%s:%s", hostname, port));
    }

    public EmployeeClient forEmployee(Employee employee) {
        this.email = employee.getEmail();
        this.password = employee.getPassword();
        return this;
    }

    public Employee findOne(long id) {
        return restTemplate.exchange(baseUri.resolve(EmployeeController.EMPLOYEE_PATH + "/" + id), HttpMethod.GET, new HttpEntity<>(createAuthHeaders()), Employee.class).getBody();
    }

    public List<Employee> findMany() {
        return findMany(new HashMap<>());
    }

    public List<Employee> findMany(Map<Object, Object> params) {
        String queryParams = getQueryParams(params);
        return restTemplate.exchange(baseUri.resolve(EmployeeController.EMPLOYEE_PATH + "?" + queryParams) , HttpMethod.GET, new HttpEntity<>(createAuthHeaders()), getParameterizedPageTypeReference()).getBody().getContent();
    }

    public Employee create(Employee employee) {
        return restTemplate.exchange(baseUri.resolve(EmployeeController.EMPLOYEE_PATH), HttpMethod.POST, new HttpEntity<>(employee, createAuthHeaders()), Employee.class).getBody();
    }

    public Employee update(Employee employee) {
        restTemplate.exchange(baseUri.resolve(EmployeeController.EMPLOYEE_PATH + "/" + employee.getId()), HttpMethod.PUT, new HttpEntity<>(employee, createAuthHeaders()), Employee.class);
        return findOne(employee.getId());
    }

    public void delete(long id) {
        restTemplate.exchange(baseUri.resolve(EmployeeController.EMPLOYEE_PATH + "/" + id), HttpMethod.DELETE, new HttpEntity<String>(createAuthHeaders()), String.class);
    }

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        restTemplate.getMessageConverters().add(converter);

        return restTemplate;
    }

    private String getQueryParams(Map<Object, Object> params) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Object, Object> e : params.entrySet()){
            if(sb.length() > 0){
                sb.append('&');
            }
            sb.append(e.getKey()).append('=').append(e.getValue());
        }

        return sb.toString();
    }

    private ParameterizedTypeReference<PageImpl<Employee>> getParameterizedPageTypeReference() {
        return new ParameterizedTypeReference<PageImpl<Employee>>() {
        };
    }

    private HttpHeaders createAuthHeaders(){
        return new HttpHeaders(){
            {
                String auth = email + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
            }
        };
    }
}
