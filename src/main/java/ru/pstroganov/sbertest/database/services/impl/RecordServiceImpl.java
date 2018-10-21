package ru.pstroganov.sbertest.database.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.pstroganov.sbertest.database.entities.Record;
import ru.pstroganov.sbertest.database.repositories.RecordRepository;
import ru.pstroganov.sbertest.database.services.RecordsService;

import static ru.pstroganov.sbertest.database.specifications.RecordSpecifications.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class RecordServiceImpl implements RecordsService {

    @Value("${local.maxItemsOnPage}")
    Integer maxItemsOnPage;

    private RecordRepository recordRepository;

    @Autowired
    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public List<Record> getAllRecords(Map<String,String> criteria) {
        return recordRepository.findAll(
                buildSearchSpecification(criteria),
                buildSort(criteria)
        );
    }

    @Override
    public void insertRecord(Map<String,String> record) {
        recordRepository.save(parseNewRecord(record));
    }

    @Override
    public Boolean deleteRecord(String id) {
        try {
            recordRepository.delete(recordRepository.findById(Integer.parseInt(id)).get());
            return true;
        } catch (NumberFormatException | NoSuchElementException e){
            return false;
        }
    }

    @Override
    public void updateRecord(Record record) {
        recordRepository.save(record);
    }

    /**
     * Агрегирует все критерии поиска в одно
     * @param criteria Критерии поиска
     * @return {@link Specification} - агрегированное условие поиска
     */
    private Specification<Record> buildSearchSpecification(Map<String, String> criteria) {
        if (criteria==null || criteria.isEmpty()) return getTrue();

        List<Specification<Record>> specificationsList = new ArrayList<>();

        criteria.forEach((k, v) -> {
            switch (k) {

                // Поиск по номеру записи
                case "number": {
                    try {
                        specificationsList.add(numberEquals(Integer.parseInt(v)));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                // Поиск по имени
                case "name": {
                    specificationsList.add(nameContains(v));
                    break;
                }

                // Поиск по телефону
                case "phone": {
                    specificationsList.add(phoneContains(v));
                    break;
                }

                // Поиск по адресу
                case "address": {
                    specificationsList.add(addressContains(v));
                    break;
                }
            }
        });

        // Составляем все вместе
        return composeAnd(specificationsList);
    }

    // TODO: 21/10/2018 Не доделано на стороне клиента, не отправляются критерии сортировки
    /**
     * Составляет сортировку в поиске.
     * @param criteria Критерии сортировки
     */
    private Sort buildSort(Map<String, String> criteria) {
        // Стандартная сортировка по убыванию порядкового номера, если больше ничего нет
        if (criteria==null || (!(criteria.containsKey("orderByDesc") || criteria.containsKey("orderByAsc")))) return Sort.by("id");
        return Sort.by(
                // Если указана кастомная сортировка, то в первую очередь сортируем по ней, а потом по убыванию порядкого номера
                criteria.containsKey("orderByDesc") ? Sort.Order.desc(criteria.get("orderByDesc")) : Sort.Order.asc(criteria.get("orderByAsc")),
                Sort.Order.desc("id")
        );
    }

    /**
     * Составляет новый объект записи из Map-а с полями
     * @param newRecord Map с полями
     */
    private Record parseNewRecord(Map<String,String> newRecord){
        Record record = new Record();
        record.setName(newRecord.get("name"));
        record.setPhone(newRecord.get("phone"));
        record.setAddress(newRecord.get("address"));
        return record;
    }
}