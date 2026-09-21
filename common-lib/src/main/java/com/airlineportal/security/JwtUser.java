package com.airlineportal.security;

import java.security.Principal;
import java.util.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtUser implements Principal{
    private Long id;

    private String email;
    private String role;

    @Override
    public String getName(){
        return email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    public boolean hasRole(String role){
        return this.role.equals(role);
    }

    public boolean hasAnyRole(String... roles){
        for(String r: roles){
            if(this.role.equals(r)){
                return true;
            }
        }

        return false;
    }

}
