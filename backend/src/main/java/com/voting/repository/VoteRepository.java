// src/main/java/com/voting/repository/VoteRepository.java
package com.voting.repository;

import com.voting.model.Vote;
import com.voting.model.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByVotingSession(VotingSession votingSession);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.votingSession.id = :sessionId AND v.voteOption = 'YES'")
    int countYesVotesBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.votingSession.id = :sessionId AND v.voteOption = 'NO'")
    int countNoVotesBySessionId(@Param("sessionId") Long sessionId);

    Optional<Vote> findByAssociateIdAndVotingSessionId(String associateId, Long votingSessionId);
}
