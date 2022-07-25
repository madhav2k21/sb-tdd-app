package com.techleads.app;

import com.techleads.app.exception.EmailNotificationServiceException;
import com.techleads.app.exception.UsersServiceException;
import com.techleads.app.model.MyUser;
import com.techleads.app.service.EmailVerficationService;
import com.techleads.app.service.UsersServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyUserServiceImplTest {
    @InjectMocks
    private UsersServiceImpl usersService;
    @Mock
    private UsersRepository usersRepository;

    @Mock
    private EmailVerficationService emailVerficationService;

    private MyUser user;

    @BeforeEach
    void setUp() {
        user = buildUser();
    }

    @Test
    void testCreateUser_whenUserDetailsAreProvided_ReturnUserObject() {
        //arrange
        when(usersRepository.save(any(MyUser.class))).thenReturn(user);

        //Act
        MyUser createdUser = usersService.createUser(user);

        //assert
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getName()).isEqualTo(user.getName());
        assertThat(createdUser.getLocation()).isEqualTo(user.getLocation());
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(createdUser.getId()).isEqualTo(user.getId());

        verify(usersRepository, times(1)).save(any(MyUser.class));
    }

    @Test
    void testCreateUser_whenUserDetailsAreProvided_throwUsersServiceException() {
        //arrange
        when(usersRepository.save(any(MyUser.class))).thenReturn(null);

        //Act
        UsersServiceException usersServiceException = Assertions.assertThrows(UsersServiceException.class, () -> {

            usersService.createUser(user);
        });

        //assert
        assertThat(usersServiceException.getMessage()).isEqualTo("Could not create user");
        verify(usersRepository, times(1)).save(any(MyUser.class));
    }

    @Test
    void testCreateUser_whenFirstNameIsEmpty_ThrowIllegalArgumentException() {
        //arrange
        user.setName("");
        //Act
        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            MyUser createdUser = usersService.createUser(user);
        });
        //assert
        assertThat(illegalArgumentException.getMessage()).isEqualTo("Name should not be empty");

    }


    @Test
    void testCreateUser_WhenEmailNotificationExceptionThrown_ThrowUserServiceException() {
        //arrange
        when(usersRepository.save(any(MyUser.class))).thenReturn(user);
        doThrow(EmailNotificationServiceException.class).when(emailVerficationService).scheduleEmailConfirmation(any(MyUser.class));
        //Act
        UsersServiceException usersServiceException = Assertions.assertThrows(UsersServiceException.class, () -> {
            usersService.createUser(user);
        });
        //assert
        assertThat(usersServiceException.getMessage()).isEqualTo("User is created but emailVerficationService is failed");
        verify(usersRepository, times(1)).save(any(MyUser.class));
        verify(emailVerficationService, times(1)).scheduleEmailConfirmation(any(MyUser.class));


    }

    @Test
    void callRealMethod(){
        when(usersRepository.save(any(MyUser.class))).thenReturn(user);
        doCallRealMethod().when(emailVerficationService).scheduleEmailConfirmation(any(MyUser.class));

        usersService.createUser(user);
        verify(emailVerficationService, times(1)).scheduleEmailConfirmation(any(MyUser.class));
    }

    private MyUser buildUser() {
        MyUser u1 = new MyUser(101, "madhav", "Hyderabad", "ma@ma");
        return u1;
    }
}