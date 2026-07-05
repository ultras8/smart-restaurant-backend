package com.personProject.smartRestaurant.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // คืนค่าสิทธิ์ (Role) ของผู้ใช้งาน
        return List.of();
    }

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Brand> brands;
}
