package com.project.LinkedIn.connection_service.service;

import com.project.LinkedIn.connection_service.auth.UserContextHolder;
import com.project.LinkedIn.connection_service.entity.Person;
import com.project.LinkedIn.connection_service.event.AcceptConnectionRequestEvent;
import com.project.LinkedIn.connection_service.event.SendConnectionRequestEvent;
import com.project.LinkedIn.connection_service.reporitory.PersonRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestEventKafkaTemplate;

    public List<Person> getFirstDegreeConnection(){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting First Degree Connection for user with ID: {}", userId);

        return personRepository.getFirstDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId) {
       Long senderId = UserContextHolder.getCurrentUserId();
       log.info("Sending connection request sender: {}, receiver: {}", senderId, receiverId);

       if(senderId.equals(receiverId)){
           throw new RuntimeException("Sender and Receiver Ids are the same");
       }

       boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
       if(alreadySentRequest){
           throw new RuntimeException("Connection request already exists");
       }

       boolean alreadyConnected = personRepository.connectionRequestExists(senderId, receiverId);
       if(alreadyConnected){
           throw new RuntimeException(" already connected users, can't send connection request");
       }

       personRepository.addConnectionRequest(senderId, receiverId);
       log.info("Successfully sent connection request");

       SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
               .senderId(senderId)
               .receiverId(receiverId)
               .build();
       sendRequestEventKafkaTemplate.send("sendConnectionRequestTopic", sendConnectionRequestEvent);

       return true;
    }

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("Accepting connection request for sender: {}, receiver: {}", senderId, receiverId);

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId, receiverId);
        if(!connectionRequestExists){
            throw new RuntimeException("No connection request exists to accept");
        }
        personRepository.acceptConnectionRequest(senderId, receiverId);
        log.info("Successfully accepted connection request");

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        acceptRequestEventKafkaTemplate.send("acceptConnectionRequestTopic", acceptConnectionRequestEvent);

        return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId, receiverId);
        if(!connectionRequestExists){
            throw new RuntimeException("No connection request exist. Can't reject connection request");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);
        return true;
    }
}
