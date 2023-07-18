package com.learn.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@SQLDelete(sql = "UPDATE users SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@NoArgsConstructor
public class Users implements Serializable {
    @Id
    private String id;
    @JsonIgnore
    private String password;
    private String name;
    private String email;
    @JsonIgnore
    private String roles;
    @JsonIgnore
    private Boolean isActive = true;

    public Users(String username) {
        this.id = username;
    }
}
