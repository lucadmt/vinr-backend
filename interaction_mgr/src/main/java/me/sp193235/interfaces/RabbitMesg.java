package me.sp193235.interfaces;

import java.io.Serializable;
import java.util.Objects;

public class RabbitMesg<T extends Serializable> implements Serializable {
    private Integer type;

    public static final Integer TYPE_CREATE = 1;
    public static final Integer TYPE_DELETE = 2;

    private T obj;

    public RabbitMesg(Integer type, T obj) {
        this.type = type;
        this.obj = obj;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RabbitMesg)) return false;
        RabbitMesg<?> that = (RabbitMesg<?>) o;
        return getType().equals(that.getType()) && getObj().equals(that.getObj());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getObj());
    }
}