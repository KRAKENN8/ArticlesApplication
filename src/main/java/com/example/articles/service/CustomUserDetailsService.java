package com.example.articles.service;

import com.example.articles.entities.User;
import com.example.articles.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Предполагается, что метод findByUsername возвращает null, если пользователь не найден
        User appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        // Преобразуем роль из нашей сущности в GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (appUser.getRole() == User.Roles.ADMIN_ROLE) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // Здесь предполагается, что пароли хранятся в БД в виде {noop}123 или в зашифрованном виде,
        // в зависимости от вашей логики. При необходимости добавьте bean BCryptPasswordEncoder и настройте его.
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(),
                appUser.getPassword(),
                authorities);
    }
}
