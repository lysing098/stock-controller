package com.example.stockcontroller.dto.User;

public class UserResponse {

    private String token;

    private String firstName;
    private String lastName;
    private String userName;
    private String gender;
    private String tel;
    private String email;
    private String address;
    private String dob;
    private Long roleId;

    // Constructor for login response (token + user data)
    public UserResponse(String token, UserResponse user) {
        this.token = token;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.userName = user.userName;
        this.gender = user.gender;
        this.tel = user.tel;
        this.email = user.email;
        this.address = user.address;
        this.dob = user.dob;
        this.roleId = user.roleId;
    }

    // Constructor for user data only
    public UserResponse(String firstName, String lastName, String userName,
                        String gender, String tel, String email,
                        String address, String dob, Long roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.gender = gender;
        this.tel = tel;
        this.email = email;
        this.address = address;
        this.dob = dob;
        this.roleId = roleId;
    }

    // getters
    public String getToken() { return token; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUserName() { return userName; }
    public String getGender() { return gender; }
    public String getTel() { return tel; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getDob() { return dob; }
    public Long getRoleId() { return roleId; }
}