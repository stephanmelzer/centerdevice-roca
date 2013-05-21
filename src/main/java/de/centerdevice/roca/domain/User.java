package de.centerdevice.roca.domain;

import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.annotate.JsonProperty;

public class User {

    private String email;
    @JsonProperty("first-name")
    private String firstname;
    @JsonProperty("last-name")
    private String lastname;
    private String name;
    @JsonProperty("group-data")
    private List<Group> groups;

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

    public String getName() {
        return name;
    }

    public String getNameHtmlEscaped() {
        return StringEscapeUtils.escapeHtml4(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
