package com.mad2man.sbweb.auth.service;

import com.google.common.collect.Lists;
import com.mad2man.sbweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class SkeletonUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.mad2man.sbweb.domain.entity.User> userOPT = userService.getUserByLogin(username);
        if (!userOPT.isPresent()) {
            throw new UsernameNotFoundException("unknown user '" + username + "'");
        }
        com.mad2man.sbweb.domain.entity.User user = userOPT.get();
        List<GrantedAuthority> authorities = Lists.newArrayList();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new User(user.getLogin(), user.getPassword(), user.isActivated(),
            true, true, true, authorities);
    }


}
