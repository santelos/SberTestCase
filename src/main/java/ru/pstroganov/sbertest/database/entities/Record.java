package ru.pstroganov.sbertest.database.entities;

import javax.persistence.*;

/**
 * Ентити записи
 */
@Entity
@Table(name = "phonebook")
public class Record {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue
    Integer id;

    /**
     * Имя
     */
    @Basic
    @Column
    String name;

    /**
     * Телефон
     */
    @Basic
    @Column
    String phone;

    /**
     * Адрес
     */
    @Basic
    @Column
    String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
