package ru.pstroganov.sbertest.database.services;

import ru.pstroganov.sbertest.database.entities.Record;

import java.util.List;
import java.util.Map;

/**
 * Сервис - обертка для работы с базой данных
 */
public interface RecordsService {

    /**
     * Собрает все записи и применяет критерии поиска
     * @param criteria критерии поиска
     */
    List<Record> getAllRecords(Map<String,String> criteria);

    /**
     * Встявляет запись в бд
     * @param record Map с полями записи
     */
    void insertRecord(Map<String,String> record);

    /**
     * Удаляет запись по Id
     * @param id Идентификатор
     */
    Boolean deleteRecord(String id);

    /**
     * Обновляет запись
     * @param record обовленная запись
     */
    void updateRecord(Record record);

}
