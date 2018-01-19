package si.fri.rso.rsobnb.real_estates;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.List;
import si.fri.rso.rsobnb.real_estates.Payment;
import si.fri.rso.rsobnb.real_estates.PropertyLease;
import si.fri.rso.rsobnb.real_estates.PropertyRental;
import si.fri.rso.rsobnb.real_estates.Image;
import si.fri.rso.rsobnb.real_estates.Review;

@Entity(name = "real_estates")
@NamedQueries(value =
        {
                @NamedQuery(name = "RealEstates.getAll", query = "SELECT r FROM real_estates r"),
                @NamedQuery(name = "RealEstates.findByUser", query = "SELECT r FROM real_estates r WHERE r.userId = " + ":userId")
        })
@UuidGenerator(name = "idGenerator")
public class RealEstate {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "size")
    private Double size;

    @Column(name = "rooms")
    private Integer rooms;

    @Column(name = "location")
    private String location;

    @Column(name = "day_price")
    private Double day_price;

    @Column(name = "parking")
    private Boolean parking;

    @Column(name = "wifi")
    private Boolean wifi;

    @Column(name = "rating")
    private Integer rating;

    @Transient
    private List<Image> images;

    @Transient
    private List<Review> reviews;

    @Transient
    private List<Payment> payments;

    @Transient
    private List<PropertyRental> property_rentals;

    @Transient
    private List<PropertyLease> property_leases;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() { return userId;}

    public void setUserId(String userId) { this.userId = userId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getDay_price() {
        return day_price;
    }

    public void setDay_price(Double day_price) {
        this.day_price = day_price;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean getWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<PropertyRental> getProperty_rentals() {
        return property_rentals;
    }

    public void setProperty_rentals(List<PropertyRental> property_rentals) {
        this.property_rentals = property_rentals;
    }

    public List<PropertyLease> getProperty_leases() {
        return property_leases;
    }

    public void setProperty_leases(List<PropertyLease> property_leases) {
        this.property_leases = property_leases;
    }
}