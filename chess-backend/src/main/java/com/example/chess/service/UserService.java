package com.example.chess.service;

import com.example.chess.domain.User;
import com.example.chess.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
public class UserService {
    private static List<User> users = new ArrayList<>();
    static {
        users.add(new User("srini", "srinivasvj01@gmail.com", "AsdFSADFsadfsafd", 0, 0, 0 , 900));
        users.add(new User("ratna", "rats12@gmail.com", "asdlkfjkljsk", 0, 0, 0 , 600));
        users.add(new User("rahul", "rahult@gmail.com", "sdfsdgwdfsafd", 0, 0, 0 , 300));
        users.add(new User("gokul", "gokul10@gmail.com", "asdkfjwlkelkwejf", 0, 0, 0 , 1000));
    }
    public List<User> getAllUser(){
        return users;
    }
    public User getUserByUserName(String userName){
       for (User user : users)
           if (userName.equals(user.getUserName()))
               return user;
       throw new UserNotFoundException();
    }

    public User addUser(User user) {
        users.add(user);
        return user;
    }

    public User deleteUserByUserName(String userName) {
        Iterator iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = (User) iterator.next();
            if (user.getUserName().equals(userName)) {
                iterator.remove();
                return user;
            }
        }
        return null;
    }
}
