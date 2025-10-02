package coffee.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private String customerId;
    private String name;
    private String phone;
    private String email;
    private String membershipLevel; // regular, vip

    public Customer() {}

    public Customer(String customerId, String name, String phone, String email, String membershipLevel) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.membershipLevel = membershipLevel;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMembershipLevel() { return membershipLevel; }
    public void setMembershipLevel(String membershipLevel) { this.membershipLevel = membershipLevel; }

    @Override
    public String toString() {
        return customerId + "," + name + "," + phone + "," + email + "," + membershipLevel;
    }

    public static Customer fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) return null;
        return new Customer(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
}
