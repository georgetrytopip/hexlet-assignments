package exercise.controllers;

import io.javalin.http.Context;
import io.javalin.apibuilder.CrudHandler;
import io.ebean.DB;
import java.util.List;
import java.util.Map;

import exercise.domain.User;
import exercise.domain.query.QUser;

import io.javalin.validation.BodyValidator;
import io.javalin.validation.JavalinValidation;
import io.javalin.validation.ValidationError;
import io.javalin.validation.Validator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.lang3.StringUtils;

public class UserController implements CrudHandler {

    public void getAll(Context ctx) {

        List<User> users = new QUser()
                .orderBy()
                .id.asc()
                .findList();

        String json = DB.json().toJson(users);
        ctx.json(json);

    };

    public void getOne(Context ctx, String id) {

        Long newId = Long.parseLong(id);


        User user = new QUser()
                .id.equalTo(newId)
                .findOne();


        /*

       // Company company = new QCompany()
    //     .id.equalTo(id)
    //     .findOne();

    // String json = DB.json().toJson(companies);
         */

        String json = DB.json().toJson(user);
        ctx.json(json);
    };

    public void create(Context ctx) {
        String body = ctx.body();


        BodyValidator<User> firstNameValidator = ctx.bodyValidator(User.class)
                .check(obj -> !obj.getFirstName().isEmpty(), "First name shouldn't be empty");

        BodyValidator<User> lastNameValidator = ctx.bodyValidator(User.class)
                .check(obj -> !obj.getLastName().isEmpty(), "Last name shouldn't be empty");


        Map<String, List<ValidationError<? extends Object>>> errors = JavalinValidation.collectErrors
                (firstNameValidator, lastNameValidator);

        if(!errors.isEmpty()) {
            ctx.status(422);
            ctx.json(errors);
            return;
        }



        User user = DB.json().toBean(User.class, body);
        user.save();


    };

    public void update(Context ctx, String id) {
        String body = ctx.body();
        User user = DB.json().toBean(User.class, body);
        user.setId(id);
        user.update();
    };

    public void delete(Context ctx, String id) {

        Long newId = Long.parseLong(id);


        new QUser()
                .id.equalTo(newId)
                .delete();
    };
}


/*

Задачи
Допишите метод getAll(), который возвращает список всех пользователей в виде JSON-представления.
Для преобразования списка пользователей в JSON можно воспользоваться методом DB.json().toJson()

Допишите метод getOne(), который возвращает конкретного пользователя в виде JSON-представления.

Допишите метод create(), который создаёт нового пользователя из полученного JSON-представления
и добавляет его в базу. Для получения экземпляра модели из JSON-представления можно воспользоваться
методом DB.json().toBean().

Допишите метод update(), который обновляет данные
пользователя в базе данными из полученного JSON-представления.

Допишите метод, который удаляет пользователя из базы.

Запустите свое приложение и попробуйте
отправлять различные запросы при помощи Postman.

Подсказки
Метод crud() автоматически добавляет в приложение пять маршрутов для
CRUD операций: получение всех сущностей getAll(), получение одной
сущности getOne(), создание create(), редактирование update() и удаление delete() сущности.

Чтобы отправить JSON строку в теле ответа, используйте метод контекста json().

В классе User есть метод setId(), который добавляет id
пользователя в экземпляр модели. Вы можете воспользоваться
им для обновления пользователя в базе.
Для этого нужно установить нужный id и вызвать метод update() на модели.

 */
