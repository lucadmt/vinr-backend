package me.sp193235.interfaces;

import com.google.gson.Gson;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Vinr implements Serializable {

    @Id
    private String username; // No Setter, because it can't be changed.

    @ManyToMany
    private final Set<UserProxy> vinrs;

    public Vinr() {
        this.vinrs = new HashSet<>();
    }

    public Vinr(String username) {
        this.username = username;
        this.vinrs = new HashSet<>();
    }

    public Set<UserProxy> getVinrs() {
        return vinrs;
    }

    public void addVinr(UserProxy vinr) {
        this.vinrs.add(vinr);
    }

    public void removeVinr(UserProxy vinr) {
        this.vinrs.remove(vinr);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserProxy toUserProxy() {
        return new UserProxy(this.username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vinr)) return false;
        if (!super.equals(o)) return false;
        Vinr vinr = (Vinr) o;
        return Objects.equals(getVinrs(), vinr.getVinrs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVinrs());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
