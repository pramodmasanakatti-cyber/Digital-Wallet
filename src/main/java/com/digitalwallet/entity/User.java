package com.digitalwallet.entity;

import com.digitalwallet.validation.annotation.ValidUserAge;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false,unique = true)
    @Email
    private  String email;

    @Column(unique = true)
    private String phone;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Wallet> wallets=new ArrayList<>();

    @ValidUserAge
    private Integer age;
}
