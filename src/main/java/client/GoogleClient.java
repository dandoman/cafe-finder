package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import model.geocode.GeocodeResult;
import model.placeDetails.PlaceDetailsResult;
import model.placeSearch.PlaceSearchResult;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class GoogleClient {
    private String googleApiKey;
    private static ObjectMapper mapper = new ObjectMapper();

    public GoogleClient(String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }

    @SneakyThrows
    public GeocodeResult geocodeAddress(String address) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/geocode/json").newBuilder();
        urlBuilder.addQueryParameter("key", googleApiKey);
        urlBuilder.addQueryParameter("address", address);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response r = client.newCall(request).execute();
        if(!r.isSuccessful()) {
            throw new RuntimeException("Error when trying to call google geocode");
        }

        return mapper.readValue(r.body().string(), GeocodeResult.class);
    }

    @SneakyThrows
    public PlaceSearchResult searchPlaces(double latitude, double longitude, List<String> categories, Long radius, String pageToken) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/place/nearbysearch/json").newBuilder();
        urlBuilder.addQueryParameter("key", googleApiKey);
        urlBuilder.addQueryParameter("location", String.format("%f,%f", latitude, longitude));
        urlBuilder.addQueryParameter("radius", "" + radius);
        urlBuilder.addQueryParameter("type", String.join(",", categories));
        if(!StringUtils.isBlank(pageToken)) {
            urlBuilder.addQueryParameter("pagetoken", pageToken);
        }

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response r = client.newCall(request).execute();
        if(!r.isSuccessful()) {
            throw new RuntimeException("Error when trying to call google places search");
        }

        return mapper.readValue(r.body().string(), PlaceSearchResult.class);
    }

    @SneakyThrows
    public PlaceDetailsResult getPlaceDetails(String placeId) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/place/details/json").newBuilder();
        urlBuilder.addQueryParameter("key", googleApiKey);
        urlBuilder.addQueryParameter("placeid", placeId);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response r = client.newCall(request).execute();
        if(!r.isSuccessful()) {
            throw new RuntimeException("Error when trying to call google place details");
        }

        return mapper.readValue(r.body().string(), PlaceDetailsResult.class);
    }
}
