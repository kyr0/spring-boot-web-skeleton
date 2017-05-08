package com.mad2man.sbweb.user.service;

import com.mad2man.sbweb.entity.User;
import com.mad2man.sbweb.user.repository.UserRepository;
import com.mad2man.sbweb.user.service.dto.ManagedUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<ManagedUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByUsernameNot(pageable, User.ANONYMOUS_USER).map(ManagedUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }
}
