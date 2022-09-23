package me.sp193235.interaction_mgr.model;

import com.google.gson.Gson;
import me.sp193235.interfaces.UserProxy;
import me.sp193235.interfaces.Vinr;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "circles")
public class Circle {

    @Id
    @GeneratedValue
    private long circleId;

    @OneToOne
    private UserProxy ofUser;

    @Column
    private String name;

    @ManyToMany
    private Set<UserProxy> users;

    public Circle() {
        users = new HashSet<>();
    }

    public Circle(long circleId, UserProxy ofUser, String name) {
        this.name = name;
        this.circleId = circleId;
        this.ofUser = ofUser;
    }

    public long getCircleId() {
        return circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
    }

    public UserProxy getOfUser() {
        return ofUser;
    }

    public void setOfUser(UserProxy ofUser) {
        this.ofUser = ofUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserProxy> getUsers() {
        return users;
    }

    public void setUsers(Set<UserProxy> userProxies) {
        this.users.clear();
        this.users.addAll(userProxies);
    }

    public void addUser(UserProxy user) {
        this.users.add(user);
    }

    public void delUser(UserProxy user) {
        this.users.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;
        Circle circle = (Circle) o;
        return getCircleId() == circle.getCircleId() && getOfUser().equals(circle.getOfUser()) && getName().equals(circle.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCircleId(), getOfUser(), getName());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
