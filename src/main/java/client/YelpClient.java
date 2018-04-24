package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import model.yelp.YelpResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class YelpClient {
    private String yelpApiKey;
    private static int limit = 50;
    private static ObjectMapper mapper = new ObjectMapper();

    public YelpClient(String yelpApiKey) {
        this.yelpApiKey = yelpApiKey;
    }

    @SneakyThrows
    public YelpResponse getThings(String location, List<String> categories, Long radiusMeters, Long offset) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.yelp.com/v3/businesses/search").newBuilder();
        if(StringUtils.isBlank(location)) {
            throw new RuntimeException("Location cannot be blank");
        }

        urlBuilder.addQueryParameter("location", location);

        if(categories != null || !categories.isEmpty()) {
            urlBuilder.addQueryParameter("categories", String.join(",", categories));
        }

        if(radiusMeters != null && radiusMeters > 0) {
            urlBuilder.addQueryParameter("radius", "" + radiusMeters);
        }

        if(offset != null && offset > 0) {
            urlBuilder.addQueryParameter("offset", "" + offset);
        }

        urlBuilder.addQueryParameter("limit", "" + limit);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + yelpApiKey)
                .url(url)
                .build();

        Response r = client.newCall(request).execute();
        if(!r.isSuccessful()) {
            throw new RuntimeException("Error when trying to call yelp");
        }

        return mapper.readValue(r.body().string(), YelpResponse.class);
    }

}
