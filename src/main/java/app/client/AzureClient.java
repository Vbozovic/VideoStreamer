package app.client;

import app.error_handling.AzureException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class AzureClient{

    private static String key = "d5db1512976c43668db4882691200a45";
    private static String endpoint = "https://westeurope.api.cognitive.microsoft.com/face/v1.0";


    public static <P,T> T delete(String route, Map<String,String> params,P dto,Class<T> ctype) throws AzureException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper om = new ObjectMapper();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(endpoint+route).newBuilder();
        if(params != null) params.forEach(httpUrlBuilder::addQueryParameter);

        String content = "";
        try {
            content = om.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        MediaType type = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(type, content);

        Request req = buildRequest(httpUrlBuilder.build(),body,Method.DELETE);
        System.out.println(req.url().toString());
        return returnResponse(client,req,ctype);
    }

    public static <T, P>  T put(String route, P dto,Map<String,String> params, Class<T> ctype) throws AzureException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper om = new ObjectMapper();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(endpoint+route).newBuilder();
        if(params != null){
            params.forEach(httpUrlBuilder::addQueryParameter);
        }

        String content = "";
        try {
            content = om.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MediaType type = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(type, content);

        Request req = buildRequest(httpUrlBuilder.build(),body,Method.PUT);
        System.out.println(req.url().toString());
        return returnResponse(client,req,ctype);
    }

    public static <T, P>  T post(String route, P dto,Map<String,String> params, Class<T> ctype) throws AzureException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper om = new ObjectMapper();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(endpoint+route).newBuilder();
        if(params != null){
            params.forEach(httpUrlBuilder::addQueryParameter);
        }

        String content = "";
        try {
            content = om.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MediaType type = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(type, content);

        Request req = buildRequest(httpUrlBuilder.build(),body,Method.POST);
        System.out.println(req.url().toString());
        return returnResponse(client,req,ctype);
    }

    public static <T> T post_binary(String route, byte[] binary,Map<String,String> params,Class<T> type) throws AzureException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(endpoint+route).newBuilder();
        if(params != null) params.forEach(httpUrlBuilder::addQueryParameter);

        RequestBody body = RequestBody.create(MediaType.get("application/octet-stream"), binary);
        Request req = buildRequest(httpUrlBuilder.build(),body,Method.POST);
        System.out.println(req.url().toString());
        return returnResponse(client,req,type);
    }

    public static <T> T get(String route,Map<String,String> params, Class<T> type) throws AzureException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(endpoint+route).newBuilder();
        if(params != null) params.forEach(httpUrlBuilder::addQueryParameter);

        Request request = buildRequest(httpUrlBuilder.build(),null,Method.GET);
        System.out.println(request.url().toString());
        return returnResponse(client,request,type);
    }

    private static <T> Request buildRequest(HttpUrl url,RequestBody body, Method type){
        Request.Builder req = new Request.Builder()
                .url(url)
                .header("Ocp-Apim-Subscription-Key",key);
        switch (type){
            case GET:
                req = req.get();
                break;
            case POST:
                req = req.post(body);
                break;
            case PUT:
                req = req.put(body);
                break;
            case PATCH:
                req = req.patch(body);
                break;
            case DELETE:
                req = req.delete(body);
                break;
        }
        return req.build();
    }

    private static <T> T returnResponse(OkHttpClient client, Request request, Class<T> type) throws AzureException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Response resp = client.newCall(request).execute();
            if (!resp.isSuccessful()){
                throw new AzureException("Azure api error status code: "+resp.code(),resp.body().string(),resp.code());
            }

            String body = resp.body().string();
            if(!body.isEmpty()){
                return mapper.readValue(body,type);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private enum Method{
        GET,POST,PUT,PATCH,DELETE
    }

}
