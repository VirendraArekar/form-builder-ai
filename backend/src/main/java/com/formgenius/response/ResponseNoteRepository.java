package com.formgenius.response;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResponseNoteRepository extends JpaRepository<ResponseNote, UUID> {
    List<ResponseNote> findByResponseIdOrderByCreatedAtAsc(UUID responseId);
}
