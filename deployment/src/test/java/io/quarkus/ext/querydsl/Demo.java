package io.quarkus.ext.querydsl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * 
 * @author Leo Tu
 */
@SuppressWarnings("serial")
@RegisterForReflection
public class Demo implements Serializable {

    private String id;
    private String name;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return super.toString() + "[id=" + id + ", name=" + name + ", amount=" + amount + ", createdAt=" + createdAt
                + "]";
    }

}
