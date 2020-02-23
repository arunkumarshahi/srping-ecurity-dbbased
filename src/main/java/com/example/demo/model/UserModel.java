package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(base = "ou=people,dc=example,dc=com", objectClasses = {
        "top",
        "inetOrgPerson", "person", "organizationalPerson",
        "simpleSecurityObject"})
@Data
public class UserModel {
    @JsonIgnore
    @Id
    private Name id;

    @JsonProperty("userName")
    private @Attribute(name = "cn")
    String uid;

    @JsonProperty("password")
    private @Attribute(name = "cn")
    String password;

    @JsonProperty("lastName")
    private @Attribute(name = "cn")
    String lastName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
