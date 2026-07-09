package com.personProject.smartRestaurant.entities;

import com.personProject.smartRestaurant.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Override
    public String getUsername() {
        return this.username;
    }

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private UserRole role = UserRole.OWNER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // บัญชีไม่หมดอายุ
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // บัญชีไม่ถูกล็อก
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // รหัสผ่านไม่หมดอายุ
    }

    @Override
    public boolean isEnabled() {
        return true; // บัญชีเปิดใช้งานอยู่
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Employee employee;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Brand> brands;


}
