import client.YelpClient;
import logic.FileCreator;
import logic.GoogleLogic;
import lombok.Cleanup;
import lombok.SneakyThrows;
import model.placeDetails.PlaceDetailsResult;
import model.yelp.Business;
import model.yelp.YelpResponse;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup Scanner keyboard = new Scanner(System.in);

        try {
            System.out.println("Welcome to the business finder");

            System.out.println("Please enter your google api key");
            GoogleLogic googleLogic = new GoogleLogic(keyboard.nextLine().trim());

            System.out.println("Please enter your yelp api key");
            YelpClient client = new YelpClient(keyboard.nextLine().trim());

            System.out.println("Enter location (ex: richmond,victoria,australia):");
            String location = keyboard.nextLine();
            Long maxRadius = 5000l;

            CompletableFuture<List<PlaceDetailsResult>> googleResultFuture = CompletableFuture.supplyAsync(() -> googleLogic.getPlaces(location));

            Long offset = null;
            long total = Long.MAX_VALUE;
            List<Business> businessList = new ArrayList<>();
            do {
                YelpResponse response = client.getThings(location, Arrays.asList("cafes"), maxRadius, offset);
                businessList.addAll(response.getBusinesses());
                if(total == Long.MAX_VALUE) {
                    total = response.getTotal();
                }
                offset = (offset == null) ? 50 :  offset + 50;
            } while (offset == null || offset <= total);

            FileCreator.writeYelpToFile(businessList.stream().sorted(Comparator.comparing(x -> (x.getDistance()))).collect(Collectors.toList()), "yelp-business.csv");
            FileCreator.writeGoogleToFile(googleResultFuture.join(), "google-business.csv");
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            System.exit(0);
        }

    }
}
