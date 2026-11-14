package com.formgenius.response;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResponseAnswerRepository extends JpaRepository<ResponseAnswer, UUID> {
    List<ResponseAnswer> findByResponseId(UUID responseId);
    List<ResponseAnswer> findByQuestionId(UUID questionId);

    @Query("SELECT ra FROM ResponseAnswer ra WHERE ra.response.form.id = :formId")
    List<ResponseAnswer> findAllByFormId(@Param("formId") UUID formId);
}
