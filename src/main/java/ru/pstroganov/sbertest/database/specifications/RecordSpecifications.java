package ru.pstroganov.sbertest.database.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.pstroganov.sbertest.database.entities.Record;

/**
 * Спецификации для работы с записями
 */
public class RecordSpecifications extends BasicSpecifications{

    /**
     * Поиск по порядковому номеру
     * @param number порядковый номер
     */
    public static Specification<Record> numberEquals(Integer number){
        return (r,cq,cb)-> cb.equal(r.get("id"),number);
    }

    /**
     * Поиск по имени
     * @param name Имя
     */
    public static Specification<Record> nameContains(String name){
        // сравниваются ловер кейсы строк, чтобы облегчить поиск
        return (r,cq,cb)-> cb.like(cb.lower(r.get("name")),"%"+name.toLowerCase()+"%");
    }

    /**
     * Поиск по номеру телефона
     * @param phone Номер телефона
     */
    public static Specification<Record> phoneContains(String phone){
        return (r,cq,cb)-> cb.like(r.get("phoneNumber"),"%"+phone+"%");
    }

    /**
     * Поиск по адресу
     * @param address Адрес
     */
    public static Specification<Record> addressContains(String address){
        // сравниваются ловер кейсы строк, чтобы облегчить поиск
        return (r,cq,cb)-> cb.like(cb.lower(r.get("address")),"%"+address.toLowerCase()+"%");
    }

}
