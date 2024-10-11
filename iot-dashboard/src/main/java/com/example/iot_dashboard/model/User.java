package com.example.iot_dashboard.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @NotBlank(message = "Firstname is mandatory")
    private String firstname;

    @NotBlank(message = "Lastname is mandatory")
    private String lastname;

    @Email
    @NotBlank(message = "Email is mandatory")
    @Indexed(unique = true)  // Ensure email uniqueness
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    private String genre;
    private int poids;
    private String taille;

    // List of devices associated with the user
    @DBRef
    private List<Device> devices = new ArrayList<>();

    // Method to get the active device
    public Device getActiveDevice() {
        if (devices != null) {
            return devices.stream()
                    .filter(Device::isActive)
                    .findFirst()
                    .orElse(null);  // Return null if no active device found
        }
        return null;
    }

    // =========== UserDetails Methods =========== //

    // Returning authorities (roles/permissions). For now, returning empty list.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();  // Can be updated to return actual roles in the future
    }

    // The username used for authentication, which is the email in this case
    @Override
    public String getUsername() {
        return this.email;
    }

    // Indicates whether the user's account has expired
    @Override
    public boolean isAccountNonExpired() {
        return true;  // Set to true, meaning the account is not expired
    }

    // Indicates whether the user is locked or unlocked
    @Override
    public boolean isAccountNonLocked() {
        return true;  // Set to true, meaning the account is not locked
    }

    // Indicates whether the user's credentials (password) has expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Set to true, meaning the credentials are valid
    }

    // Indicates whether the user is enabled or disabled
    @Override
    public boolean isEnabled() {
        return true;  // Set to true, meaning the user is enabled
    }
}
