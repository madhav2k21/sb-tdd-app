package com.techleads.app;

import com.techleads.app.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<MyUser, Integer> {
}
