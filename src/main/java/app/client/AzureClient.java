package app.client;

import app.error_handling.AzureException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.HttpUrl.Builder;
import org.asynchttpclient.RequestBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Map;

public class AzureClient{

    private static String key = "d5db1512976c43668db4882691200a45";
    private static String endpoint = "https://westeurope.api.cognitive.microsoft.com/face/v1.0";

    private RequestBuilder setupAuthentication(RequestBuilder request){
        request.setHeader("Ocp-Apim-Subscription-Key",key);
        return request;
    }

    public static <T, P>  T post(String route, P dto,Map<String,String> params, Class<T> ctype) throws AzureException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper om = new ObjectMapper();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(endpoint+route).newBuilder();
        if(params != null){
            params.forEach(httpUrlBuilder::addQueryParameter);
        }

        String content = null;
        try {
            content = om.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MediaType type = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(type, content);

        Request req = new Request.Builder()
                .url(httpUrlBuilder.build())
                .header("Ocp-Apim-Subscription-Key",key)
                .post(body)
                .build();
        System.out.println(req.url().toString());
        return returnResponse(client,req,ctype);
    }

    public static <T> T post_binary(String route, byte[] binary,Map<String,String> params,Class<T> type) throws AzureException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(endpoint+route).newBuilder();
        if(params != null) params.forEach(httpUrlBuilder::addQueryParameter);

        RequestBody body = RequestBody.create(MediaType.get("application/octet-stream"), binary);
        Request req = new Request.Builder()
                .url(httpUrlBuilder.build())
                .header("Ocp-Apim-Subscription-Key",key)
                .post(body)
                .build();
        System.out.println(req.url().toString());
        return returnResponse(client,req,type);
    }

    public static <T> T get(String route,Map<String,String> params, Class<T> type) throws AzureException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(endpoint+route).newBuilder();
        if(params != null) params.forEach(httpUrlBuilder::addQueryParameter);

        Request request = new Request.Builder()
                .url(httpUrlBuilder.build())
                .header("Ocp-Apim-Subscription-Key",key)
                .get()
                .build();
        System.out.println(request.url().toString());
        return returnResponse(client,request,type);
    }

    private static <T> T returnResponse(OkHttpClient client, Request request, Class<T> type) throws AzureException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Response resp = client.newCall(request).execute();
            if (!resp.isSuccessful()){
                throw new AzureException("Azure api error status code: "+resp.code(),resp.body().string(),resp.code());
            }
            return mapper.readValue(resp.body().string(),type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
