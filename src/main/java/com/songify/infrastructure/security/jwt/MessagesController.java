//COMMENTED OUT BECAUSE WE DO NOT USE STATEFUL USER SESSION RIGHT NOW FOR OAUTH2

//package com.songify.infrastructure.security.jwt;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//class MessagesController {
//
//    @GetMapping("/message")
//    public ResponseEntity<MessageDto> message(Authentication principal) {
//        MessageDto message = new MessageDto("Hi " + principal.getName());
//        return ResponseEntity.ok(message);
//    }
//}
