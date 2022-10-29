package com.dsib.language.core.progress.infrastructure.dao.spring;

import com.dsib.language.core.progress.infrastructure.dao.WordProgressEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WordProgressSpringRepository
        extends PagingAndSortingRepository<WordProgressEntity, String>, WordProgressRepositoryExtension
{
    @Query(
        "SELECT * FROM word_progress wp\n" +
                "WHERE wp.failed - wp.approved > 0 AND owner_id = :ownerId\n" +
                "ORDER BY wp.failed - wp.approved DESC\n" +
                "LIMIT :limit;"
    )
    Iterable<WordProgressEntity> findMostFailedNonApproved(@Param("limit") int limit, @Param("ownerId") String ownerId);

    @Query(
        "SELECT * FROM word_progress wp\n" +
                "WHERE wp.failed - wp.approved > 0 AND owner_id = :ownerId\n" +
                "ORDER BY updated_at ASC\n" +
                "LIMIT :limit;"
    )
    Iterable<WordProgressEntity> findOldNonApproved(@Param("limit") int limit, @Param("ownerId") String ownerId);

    @Query("SELECT * FROM word_progress WHERE origin IN (:origins) AND owner_id = :ownerId")
    Iterable<WordProgressEntity> findAllByOrigin(
      @Param("origins") Iterable<String> origins, @Param("ownerId") String ownerId
    );
}
