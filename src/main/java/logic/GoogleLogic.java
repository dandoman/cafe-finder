package logic;

import client.GoogleClient;
import model.geocode.GeocodeResult;
import model.geocode.Result;
import model.placeDetails.PlaceDetailsResult;
import model.placeSearch.PlaceSearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GoogleLogic {
    private GoogleClient client;
    private static int maxPlaces = 400;

    public GoogleLogic(String googleApiKey) {
        client = new GoogleClient(googleApiKey);
    }

    public List<PlaceDetailsResult> getPlaces(String address) {
        GeocodeResult geocodeResult = client.geocodeAddress(address);
        Result result = geocodeResult.getResults().stream().findAny().get();
        double lat = result.getGeometry().getLocation().getLat();
        double lng = result.getGeometry().getLocation().getLng();

        List<String> placeIds = new ArrayList<>();
        int totalResults = 0;
        String paginationToken = null;
        PlaceSearchResult searchResult = client.searchPlaces(lat, lng, Arrays.asList("cafe"), 5000l, null);
        placeIds.addAll(searchResult.getResults().stream().map(x -> x.getPlaceId()).collect(Collectors.toList()));
        totalResults += searchResult.getResults().size();
        paginationToken = searchResult.getNextPageToken();
        while(paginationToken != null && totalResults < maxPlaces) {
            PlaceSearchResult sr = client.searchPlaces(lat, lng, Arrays.asList("cafe"), 5000l, paginationToken);
            placeIds.addAll(sr.getResults().stream().map(x -> x.getPlaceId()).collect(Collectors.toList()));
            totalResults += sr.getResults().size();
            paginationToken = sr.getNextPageToken();
        }

        List<CompletableFuture<PlaceDetailsResult>> placeDetailsFutureList = placeIds.stream().map(id -> CompletableFuture.supplyAsync(() -> client.getPlaceDetails(id))).collect(Collectors.toList());
        return placeDetailsFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }
}
