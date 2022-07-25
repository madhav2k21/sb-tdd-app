package com.techleads.app.service;

import com.techleads.app.UsersRepository;
import com.techleads.app.exception.UsersServiceException;
import com.techleads.app.model.MyUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EmailVerficationService emailVerficationService;

    @Override
    public MyUser createUser(MyUser user) {
        if (StringUtils.isEmpty(user.getName())) {
            throw new IllegalArgumentException("Name should not be empty");
        }
        MyUser savedUser = usersRepository.save(user);
        if (Objects.isNull(savedUser)) {
            throw new UsersServiceException("Could not create user");
        }
        try {
            emailVerficationService.scheduleEmailConfirmation(savedUser);
        } catch (RuntimeException e) {
            throw new UsersServiceException("User is created but emailVerficationService is failed");
        }
        return savedUser;
    }
}
