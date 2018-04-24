
package model.yelp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "businesses",
    "total",
    "region"
})
public class YelpResponse {

    @JsonProperty("businesses")
    private List<Business> businesses = null;
    @JsonProperty("total")
    private long total;
    @JsonProperty("region")
    private Region region;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public YelpResponse() {
    }

    /**
     * 
     * @param region
     * @param total
     * @param businesses
     */
    public YelpResponse(List<Business> businesses, long total, Region region) {
        super();
        this.businesses = businesses;
        this.total = total;
        this.region = region;
    }

    @JsonProperty("businesses")
    public List<Business> getBusinesses() {
        return businesses;
    }

    @JsonProperty("businesses")
    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    @JsonProperty("total")
    public long getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(long total) {
        this.total = total;
    }

    @JsonProperty("region")
    public Region getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(Region region) {
        this.region = region;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
