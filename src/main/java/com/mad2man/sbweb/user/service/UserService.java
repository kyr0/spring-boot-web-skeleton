package com.mad2man.sbweb.user.service;

import com.mad2man.sbweb.entity.UserEntity;
import com.mad2man.sbweb.user.aggregate.ManagedUserAggregate;
import com.mad2man.sbweb.user.repository.UserRepository;
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
    public Page<ManagedUserAggregate> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByUsernameNot(pageable, UserEntity.ANONYMOUS_USER).map(ManagedUserAggregate::new);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }
}
