package cn.ua.bank.application.test.task.service.impl;

import cn.ua.bank.application.test.task.exception.IncorrectPasswordException;
import cn.ua.bank.application.test.task.model.User;
import cn.ua.bank.application.test.task.repository.UserRepository;
import cn.ua.bank.application.test.task.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(User user) throws IncorrectPasswordException {
        if (userRepository.existsById(user.getEmail())) {
            return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())
                    .orElseThrow(() -> new IncorrectPasswordException(user));
        }
        return userRepository.save(user);
    }
}
