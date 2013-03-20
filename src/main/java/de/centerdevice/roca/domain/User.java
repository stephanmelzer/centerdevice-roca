package de.centerdevice.roca.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class User {

    private String email;
    @JsonProperty("first-name")
    private String firstname;
    @JsonProperty("last-name")
    private String lastname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
