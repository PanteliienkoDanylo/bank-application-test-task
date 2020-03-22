package cn.ua.bank.application.test.task.service.impl;

import cn.ua.bank.application.test.task.exception.IncorrectPasswordException;
import cn.ua.bank.application.test.task.model.User;
import cn.ua.bank.application.test.task.repository.UserRepository;
import cn.ua.bank.application.test.task.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(User user) throws IncorrectPasswordException {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
                return foundUser.get();
            } else {
                throw new IncorrectPasswordException(user);
            }
        } else {
            return userRepository.save(new User(user.getEmail(), passwordEncoder.encode(user.getPassword())));
        }
    }
}
