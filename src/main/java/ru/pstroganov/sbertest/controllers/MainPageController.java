package ru.pstroganov.sbertest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.pstroganov.sbertest.database.entities.Record;
import ru.pstroganov.sbertest.database.services.impl.RecordServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для REST запросов с главной страницы
 */
@Controller
public class MainPageController {

    RecordServiceImpl recordService;

    @Autowired
    public MainPageController(RecordServiceImpl recordService) {
        this.recordService = recordService;
    }

    /**
     * Возвращяет отрисованную вью для главной страницы
     */
    @GetMapping("/")
    public ModelAndView getMainPage() {
        return new ModelAndView("mainPage.html");
    }

    /**
     * Служит для селекта всех записей, можно опционально передать критерии поиска.<br></br>
     * JSON преобразается в Map и на стороне сервера его можно распарсить как угодно.<br></br>
     * <pre>
     *     Поиск (Ключи):
     *     id - поиск по порядковому номеру
     *     name - поимск по имени
     *     phone - поиск по номеру телефона
     *     address - поиск по адресу
     * </pre>
     * @param criteria критерии поиска и сортировки
     */
    @PostMapping("/records")
    public ResponseEntity<List<Record>> getRecords(
            @RequestBody(required = false) Map<String, String> criteria
    ) {
        return ResponseEntity.ok(recordService.getAllRecords(criteria));
    }

    /**
     * Добавляет новую запись.<br></br>
     * Принимает Map с полями записи,<br></br>
     * Все поля являются опциональными, по этому принимается Map, а не целый объект Record.
     * @param newRecord поля новой записи
     */
    @PostMapping("/newRecord")
    public ResponseEntity addNewRecord(
            @RequestBody Map<String, String> newRecord
    ) {
        recordService.insertRecord(newRecord);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет запись по ее Id
     * @param id - идентификатор записи
     */
    @DeleteMapping("/records")
    public ResponseEntity deleteRecord(
            @RequestBody String id
    ) {
        return recordService.deleteRecord(id) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    /**
     * Обновляет информацию о записи.<br></br>
     * Принимает объект, потому что на уровне клиента есть все поля этого объекта.
     * @param record Объект записи
     */
    @PutMapping("/records")
    public ResponseEntity updateRecord(
            @RequestBody Record record
    ) {
        recordService.updateRecord(record);
        return ResponseEntity.ok().build();
    }

}
