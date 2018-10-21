/**
 * При инициалазии DOM-а заполняется шапка таблицы с записями и находятся все записи
 */
$(document).ready(function () {
   var table = $(".content-table");
   table.append(
       "<tr>" +
       "<td>№</td>"+
       "<td>Имя</td>"+
       "<td>Телефон</td>"+
       "<td>Адрес</td>"+
       "</tr>");
   getRecords();
});

/**
 * POST запрос для селекта всех записей
 */
function getRecords(){
    $.ajax({
        url:"/records",
        type:"post",
        contentType:"application/json",
        data: getFormData("search-form"),
        processData:false,
        success:function (result) {
            showRecordsInTable(result);
        }
    })
}

/**
 * Добавляет запись в БД
 */
function addRecord(record) {
    $.ajax({
        url:"/newRecord",
        type:"post",
        contentType:"application/json",
        data:record,
        processData:false,
        success:function () {
            $("#add-new-record-header").text("Новая запись добавлена");
            getRecords();
        },
        error:function () {
            $("#add-new-record-header").text("Ошибка добавления");
        }
    })
}

/**
 * Удаляет запись
 */
function deleteRecord(id) {
    $.ajax({
        url:"/records",
        type:"delete",
        contentType:"application/json",
        data:id,
        processData:false,
        success:function () {
            $("#record-edit-header").text("Запись удалена");
            getRecords();
        },
        error:function () {
            $("#record-edit-header").text("Ошибка удаления");
        }
    })
}

/**
 * Обновляет запись
 */
function updateRecord(record) {
    $.ajax({
        url:"/records",
        type:"put",
        contentType:"application/json",
        data:record,
        processData:false,
        success:function () {
            $("#record-edit-header").text("Запись обновлена");
            getRecords();
        },
        error:function () {
            $("#record-edit-header").text("Ошибка обновления");
        }
    })
}

/**
 * Отображает все найденные записи в таблице
 * @param records
 */
function showRecordsInTable(records){
    if(Array.isArray(records)){
        var table = $(".content-table");
        clearTable();
        // Если ничего не было найдено
        if(records.length<=0){
            table.append(
                "<tr class='content-table-row-not-found'>" +
                    "<td colspan='4'>Ничего не найдено</td>"+
                "</tr>"
            )
        }
        // Добавляет строки в таблице
        records.forEach(function (elem) {
            table.append(
                "<tr class='content-table-row'>" +
                    "<td>"+elem.id+"</td>"+
                    "<td>"+elem.name+"</td>"+
                    "<td>"+elem.phone+"</td>"+
                    "<td>"+elem.address+"</td>"+
                "</tr>"
            )
        });
        // Что происходит когда нажимается строка с записью
        $(".content-table-row").unbind("click").click(function () {
            var record = $(this).find("td");
            $("<div class='transparent-back'></div>").appendTo("body");
            $(".transparent-back").append("<div class='record-edit-container'></div>");
            $(".record-edit-container").append(
                "<table id='record-edit-table' class='record-edit-table'>" +
                    "<tr><td id='record-edit-header'>Информация о записи</td></tr>"+
                    "<tr>" +
                        "<td>Номер №:</td>" +
                        "<td><input id='record-edit-id' name='id' type='text' placeholder='Номер' value="+record[0].innerText+"></td>"+
                    "</tr>"+
                    "<tr>" +
                        "<td>Имя</td>" +
                        "<td><input id='record-edit-name' name='name' type='text' placeholder='Имя' value="+record[1].innerText+"></td>"+
                    "</tr>"+
                    "<tr>" +
                        "<td>Телефон</td>" +
                        "<td><input id='record-edit-phone' name='phone' placeholder='Телефон' value="+record[2].innerText+"></td>"+
                    "</tr>"+
                    "<tr>" +
                        "<td>Адрес</td>" +
                        "<td><input id='record-edit-address' name='address' placeholder='Адрес' value="+record[3].innerText+"></td>"+
                    "</tr>"+
                    "<tr>" +
                        "<td><button id='delete-button' type='button'>Удалить</button>"+
                        "<td><button id='update-button' type='button'>Изменить</button>"+
                    "</tr>"+
                "</table>"
            );
            // Нажатие куда-угодно возвращает все назад
            $(".transparent-back").unbind("click").click(function (ev) {
                if(ev.target==$(".transparent-back")[0]){
                    $(".record-edit-container").remove();
                    $(this).remove();
                }
            });
            // Кнопка удаления
            $("#delete-button").unbind("click").click(function () {
                deleteRecord($("#record-edit-id").val());
            });
            // Кнопка обновления
            $("#update-button").unbind("click").click(function () {
                var record = {};
                $.each(
                    $("#record-edit-table").find("input"),
                    function () {
                        record[$(this).attr("name")]=$(this).val();
                    });
                updateRecord(JSON.stringify(record));
            });
        })
    }
}

/**
 * Агрегирует форму в JSON, если поле пустое - записывает null
 * @param formName имя формы
 * @returns {string} Строку JSON для передачи на сервер
 */
function getFormData(formName) {
    var data = {};
    $.each(
        $("#"+formName).serializeArray(),
        function () {
            if (this.value === "") return null;
            else data[this.name] = this.value;
        });
    return JSON.stringify(data);
}

/**
 * Очищает всю таблицу
 */
function clearTable(){
    $(".content-table-row").remove();
    $(".content-table-row-not-found").remove();
}

/**
 * Форма для поиска
 */
$(function () {
    $("#search-form").unbind("submit").submit(function () {
        getRecords();
        return false;
    })
});

/**
 * Форма добавления новой записи
 */
$(function () {
    $("#add-new-record-form").unbind("submit").submit(function () {
        addRecord(getFormData("add-new-record-form"));
        $(this)[0].reset();
        return false;
    })
});


