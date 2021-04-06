package ru.vostenzuk.jdbctest.dto;

import com.sun.istack.NotNull;

public class CreateEmployeeRequest {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String position;

    public CreateEmployeeRequest(String firstName, String lastName, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "CreateEmployeeRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
