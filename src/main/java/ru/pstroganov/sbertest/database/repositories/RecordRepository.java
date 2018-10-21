package ru.pstroganov.sbertest.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.pstroganov.sbertest.database.entities.Record;

/**
 * Репозиторий записей
 */
public interface RecordRepository extends JpaRepository<Record, Integer>, JpaSpecificationExecutor<Record> {
}
