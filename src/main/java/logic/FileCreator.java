package logic;

import lombok.Cleanup;
import lombok.SneakyThrows;
import model.placeDetails.PlaceDetailsResult;
import model.placeDetails.Result;
import model.yelp.Business;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

public class FileCreator {

    @SneakyThrows
    public static void writeYelpToFile(List<Business> businesses, String filename) {
        System.out.println("Writing to file: " + filename);
        @Cleanup FileWriter writer = new FileWriter(new File(filename));
        String parametrized = "%s,%s,%s,%s,%s\n";
        writer.write(String.format(parametrized, "Name", "Address", "Phone", "Distance", "Url"));
        for (Business business : businesses) {
            writer.write(String.format(parametrized, business.getName().replaceAll(",", "-"), getAddress(business).replaceAll(",", "-"), business.getPhone().replaceAll(",", "-"), business.getDistance(), business.getUrl().replaceAll(",", "-")));
        }
    }

    @SneakyThrows
    public static void writeGoogleToFile(List<PlaceDetailsResult> places, String filename) {
        System.out.println("Writing to file: " + filename);
        @Cleanup FileWriter writer = new FileWriter(new File(filename));
        String parametrized = "%s,%s,%s,%s\n";
        writer.write(String.format(parametrized, "Name", "Address", "Phone", "Website"));
        for (Result place : places.stream().map(x -> x.getResult()).collect(Collectors.toList())) {
            writer.write(String.format(parametrized, place.getName().replaceAll(",", "-"), place.getFormattedAddress().replaceAll(",", "-"), place.getInternationalPhoneNumber(), place.getWebsite()));
        }
    }

    private static String getAddress(Business business) {
        if(business.getLocation() == null) {
            return "";
        }

        if(business.getLocation().getDisplayAddress() == null) {
            return "";
        }

        return String.join(", ", business.getLocation().getDisplayAddress());
    }
}
