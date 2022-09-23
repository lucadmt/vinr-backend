package me.sp193235.interfaces;

import com.google.gson.Gson;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users_proxy")
public class UserProxy implements Serializable {

    @Id
    private String username; // No Setter, because it can't be changed.

    public UserProxy() {
    }

    public UserProxy(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProxy)) return false;
        UserProxy userProxy = (UserProxy) o;
        return getUsername().equals(userProxy.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
