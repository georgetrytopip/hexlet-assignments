package exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;
import io.ebean.DB;

import exercise.domain.User;
import exercise.domain.query.QUser;
import io.ebean.Database;

class AppTest {

    private static Javalin app;
    private static String baseUrl;


    @BeforeAll
    public static void beforeAll(){
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
   public static void afterAll(){
        app.stop();
    }


    /*

    Напишите тест, который проверяет, что при отправке валидных данных приходит ответ с кодом 302,
    а новый пользователь создаётся и добавляется в базу данных. Данные пользователя считаются валидными, если:

Имя и фамилия не пустые
Email валидный (состоит из двух частей — имени ящика и доменного имени почтового сервиса, разделенных знаком @)
Пароль содержит не менее 4 символов
Чтобы проверить, что пользователь с такими данными есть в
базе, получите его из базы и проверьте, что его данные соответствуют тем, что были отправлены в запросе.

Напишите тест, который проверяет, что при отправке не валидных данных приходит ответ с
кодом 422 и новый пользователь не добавляется в базу данных. Проверьте также,
 что тело ответа содержит данные пользователя и сообщения об ошибочно заполненных полях. Текст
 сообщений вы можете посмотреть в классе контроллера UserController.

Если считаете, что необходимо дополнительно протестировать ещё какие-то случаи, можете написать тесты и на них.
     */

    // Между тестами база данных очищается
    // Благодаря этому тесты не влияют друг на друга
    @BeforeEach
    void beforeEach() {
        Database db = DB.getDefault();
        db.truncate("users");
        User existingUser = new User("Wendell", "Legros", "a@a.com", "123456");
        existingUser.save();
    }

    @Test
    void testUsers() {

        // Выполняем GET запрос на адрес http://localhost:port/users
        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users")
            .asString();
        // Получаем тело ответа
        String content = response.getBody();

        // Проверяем код ответа
        assertThat(response.getStatus()).isEqualTo(200);
        // Проверяем, что страница содержит определенный текст
        assertThat(response.getBody()).contains("Wendell Legros");
    }

    @Test
    void testNewUser() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users/new")
            .asString();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testCreateUser() {
        HttpResponse<String> response = Unirest
                .post(baseUrl + "/users")
                .field("firstName", "Goshan")
                .field("lastName", "Chechenec")
                .field("email", "goshlolka@gmail.com")
                .field("password", "1234567890")
                .asString();

        assertThat(response.getStatus()).isEqualTo(302);


       User actualUser = new QUser()
               .firstName.equalTo("Goshan")
               .findOne();

       assertThat(actualUser).isNotNull();
    }


    @Test
    void testCreateUserNegative() {
        HttpResponse<String> response = Unirest
                .post(baseUrl + "/users")
                .field("firstName", "k")
                .field("lastName", "")
                .field("email", "valenok")
                .field("password", "123")
                .asString();

        assertThat(response.getStatus()).isEqualTo(422);

        User actualUser = new QUser()
                .firstName.equalTo("k")
                .findOne();

        assertThat(actualUser).isNull();
    }

}
