package exercise;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.crud;
import io.javalin.validation.ValidationException;

import exercise.controllers.UserController;

public final class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "8000");
        return Integer.valueOf(port);
    }

    private static void addRoutes(Javalin app) {

        app.get("/", ctx -> ctx.result("REST API"));

        app.routes(() -> {
                    crud("api/v1/users/{id}", new UserController());
                }); //как будто бы не все методы кажется что будут реализованы (спросить у чата)


        /*
        Допишите метод addRoutes().
        Добавьте в приложение пять маршрутов для CRUD пользователя,
        оторые были рассмотрены выше. Для этого удобно воспользоваться методом crud(),
        который автоматически добавит эти маршруты.

        GET-запрос на /api/v1/users вернёт список всех пользователей
        GET-запрос на /api/v1/users/{id} вернёт данные конкретного пользователя
        POST-запрос на /api/v1/users создаёт нового пользователя
        PATCH-запрос на /api/v1/users/{id} обновляет данные пользователя
        DELETE-запрос на /api/v1/users/{id} удаляет пользователя

         */
    }

    public static Javalin getApp() {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        // Устанавливаем, что при возникновении ошибок валидации
        // будет отправлен JSON с ошибками валидации
        // и установлен код ответа 422
        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.json(e.getErrors()).status(422);
        });

        addRoutes(app);

        return app;
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }
}


