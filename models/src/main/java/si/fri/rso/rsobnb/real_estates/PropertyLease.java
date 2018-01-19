package si.fri.rso.rsobnb.real_estates;

import java.util.Date;

public class PropertyLease {

    private String id;
    private String realEstateId;
    private String renterId;
    private String leaserId;
    private Integer duration;
    private Boolean available;
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(String realEstateId) {
        this.realEstateId = realEstateId;
    }

    public String getRenterId() {
        return renterId;
    }

    public void setRenterId(String renterId) {
        this.renterId = renterId;
    }

    public String getLeaserId() {
        return leaserId;
    }

    public void setLeaserId(String leaserId) {
        this.leaserId = leaserId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}