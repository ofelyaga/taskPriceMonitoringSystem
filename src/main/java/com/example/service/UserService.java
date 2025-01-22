package com.example.service;

import com.example.dto.UserDTO;
import com.example.mapper.UserMapper;
import jakarta.transaction.Transactional;
import com.example.repository.UserRepository;
import com.example.model.User;
import com.example.utility.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, UserMapper userMapper){
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public Optional<User> findByUserName(String username){
        return userRepository.findUserByUsername(username);
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return UserDetailsImpl.build(user);
    }

    public boolean existsUserByUserId(UUID uuid) {
        return userRepository.existsUserByUserId(uuid);
    }

    public UserDTO createNewUser(UserDTO userDTO){
        User user = userMapper.toEntity(userDTO);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(UUID userId, UserDTO updatedUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь с ID '%s' не найден", userId)
                ));

        if (updatedUserDTO.getFirstName() != null) {
            user.setFirstName(updatedUserDTO.getFirstName());
        }
        if (updatedUserDTO.getEmail() != null) {
            user.setEmail(updatedUserDTO.getEmail());
        }
        if (updatedUserDTO.getPassword() != null) {
            String hashedPassword = passwordEncoder.encode(updatedUserDTO.getPassword());
            user.setPassword(hashedPassword);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь с ID '%s' не найден", userId)
                ));
        userRepository.delete(user);
    }
}
