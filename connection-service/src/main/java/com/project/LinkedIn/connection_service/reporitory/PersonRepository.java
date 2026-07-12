package com.project.LinkedIn.connection_service.reporitory;

import com.project.LinkedIn.connection_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person,Long> {
    Optional<Person> getByName(String name);

    @Query("MATCH (p1:Person) -[:CONNECTED_TO]- (p2:Person) " +
            "WHERE p1.userId = $userId " +
            "RETURN p2")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN COUNT(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person {userId: $senderId})-[r:CONNECTED_TO]-(p2:Person {userId: $receiverId}) " +
            "RETURN COUNT(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person {userId: $senderId}), (p2:Person {userId: $receiverId}) " +
            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person {userId: $senderId})-[r:REQUESTED_TO]->(p2:Person {userId: $receiverId}) " +
            "DELETE r " +
            "CREATE (p1)-[:CONNECTED_TO]->(p2)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person {userId: $senderId})-[r:REQUESTED_TO]->(p2:Person {userId: $receiverId}) " +
            "DELETE r")
    void rejectConnectionRequest(Long senderId, Long receiverId);
}
