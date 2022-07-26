package com.project.shopping.repository;

import com.project.shopping.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    public void save(){

        User user = new User();
        user.setUsername("a");
        user.setAddress("경기도부천");
        user.setAge(24);
        user.setNickname("kkm");
        user.setPhoneNumber("010");
        User saved = userRepository.save(user);

        assertEquals(user.getUsername(),saved.getUsername());




    }

}