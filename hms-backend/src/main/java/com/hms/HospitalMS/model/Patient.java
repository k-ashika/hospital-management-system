package com.hms.HospitalMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// ✅ FIXED: Removed @Data, @NoArgsConstructor, @AllArgsConstructor from Lombok
// because manual constructors and getters/setters were already written below,
// causing compile conflict. Keep it plain Java.

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Patient name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2-50 characters")
    private String name;

    private int age;

    private String gender;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    public Patient() {}

    public Patient(Long id, String name, int age, String gender, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Patient [id=" + id + ", name=" + name + ", age=" + age +
               ", gender=" + gender + ", email=" + email +
               ", phone=" + phone + ", address=" + address + "]";
    }
}
