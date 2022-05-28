/*
package com.tfm.secureappspring.services;

import com.tfm.secureappspring.data.daos.UserRepository;
import com.tfm.secureappspring.data.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    @Autowired
    UserRepository repository;

    public void save(MultipartFile file) {
        try {
            List<User> users = CSVHelper.csvToUsers(file.getInputStream());
            repository.saveAll(users);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<User> users = repository.findAll();

        return CSVHelper.usersToCSV(users);
    }

    public List<User> getAllTutorials() {
        return repository.findAll();
    }
}
*/
