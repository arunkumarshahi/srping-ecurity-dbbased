package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ProfileManager {
    @Autowired
    private Environment environment;

    public String getActiveProfiles() {
        String profile = "";
        for (String profileName : environment.getActiveProfiles()) {
            System.out.println("Currently active profile - " + profileName);
            profile = profileName;
        }
        return profile;
    }
}
