package models;

public class Booking {

    private String name;
    private String phone;
    private Double value;
    private String checkIn;
    private String checkOut;

    public Booking(){

    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public Double getValue() {
        return value;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
