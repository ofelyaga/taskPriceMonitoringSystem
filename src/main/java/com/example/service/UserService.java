package com.example.service;

import com.example.model.Role;
import jakarta.transaction.Transactional;
import com.example.dto.RegistrationUserDto;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.utility.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUserName(String username){
        return userRepository.findUserByFirstName(username);
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return UserDetailsImpl.build(user);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserByFirstName(username);
    }

    public User createNewUser(@RequestBody RegistrationUserDto registrationUserDto){
        User user = new User();
        user.setFirstName(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        String hashedPassword = passwordEncoder.encode(registrationUserDto.getPassword());
        user.setPassword(hashedPassword);
        user.setRoles(Set.of((Role) roleService.getUserRole()));
        return userRepository.save(user);
    }
}
