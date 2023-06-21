package org.alexandr1017;

import org.alexandr1017.dto.DtoMessage;
import org.alexandr1017.dto.MessageMapper;
import org.alexandr1017.model.Message;
import org.alexandr1017.model.MessageRepo;
import org.alexandr1017.model.User;
import org.alexandr1017.model.UserRepo;
import org.apache.logging.log4j.util.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ChatController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/init")
    public Map<String, Boolean> init() {
        String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
        Optional<User> optionalUser = userRepo.findBySessionId(sessionId);
        return Map.of("result", optionalUser.isPresent());
    }

    @PostMapping("/auth")
    public Map<String, Boolean> auth(@RequestParam String name) {
        if (!Strings.isNotEmpty(name)) {
            return Map.of("result", false);
        }
        String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
        User user = new User();
        user.setName(name);
        user.setSessionId(sessionId);
        userRepo.save(user);
        return Map.of("result", true);
    }

    @PostMapping("/message")
    public Map<String, Boolean> sendMessage(@RequestParam String message) {

        if (!Strings.isNotEmpty(message)) {
            return Map.of("result", false);
        }
        String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
        Optional<User> optionalUser = userRepo.findBySessionId(sessionId);
        if (!optionalUser.isPresent()) {
            return Map.of("result", false);
        }
        Message newMessage = new Message();
        newMessage.setMessage(message);
        newMessage.setDateTime(LocalDateTime.now());
        newMessage.setUser(optionalUser.get());
        messageRepo.save(newMessage);
        return Map.of("result", true);
    }

    @GetMapping("/message")
    public List<DtoMessage> getMessagesList() {
        return messageRepo
                .findAll(Sort.by(Sort.Direction.ASC,"dateTime"))
                .stream()
                .map(MessageMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUsersList() {
        List<User> users = userRepo.findAll();
        return ResponseEntity.ok(users);
}}
