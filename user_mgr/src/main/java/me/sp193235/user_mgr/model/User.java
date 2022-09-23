package me.sp193235.user_mgr.model;

import com.google.gson.Gson;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {
    
    @Id
    private String username; // No Setter, because it can't be changed.

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Column
    private String bio;

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return this.bio;
    }

    @Column(name = "profile_pic", length=5000000)
    private String profilePic;

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return this.profilePic;
    }

    @Column
    private String password;

    public void setPassword(String pw) {
        this.password = pw;
    }

    public String hashPw() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return passwordEncoder.encode(this.password);
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
