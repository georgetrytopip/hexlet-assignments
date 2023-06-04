package exercise.controllers;

import io.javalin.http.Handler;
import java.util.List;
import java.util.Map;
import io.javalin.validation.Validator;
import io.javalin.validation.ValidationError;
import io.javalin.validation.JavalinValidation;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.lang3.StringUtils;
import io.javalin.http.Context;

import exercise.domain.User;
import exercise.domain.query.QUser;




public final class UserController {

    private static void removeFlashMessage(Context ctx) {
        ctx.sessionAttribute("flash", null);
    }

    public static Handler listUsers = ctx -> {

        List<User> users = new QUser()
            .orderBy()
                .id.asc()
            .findList();

        ctx.attribute("users", users);
        ctx.render("users/index.html");
        removeFlashMessage(ctx);
    };

    public static Handler showUser = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        User user = new QUser()
            .id.equalTo(id)
            .findOne();

        ctx.attribute("user", user);
        ctx.render("users/show.html");
    };

    public static Handler newUser = ctx -> {

        ctx.attribute("errors", Map.of());
        ctx.attribute("user", Map.of());
        ctx.render("users/new.html");
    };

    public static Handler createUser = ctx -> {

        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");


        Validator<String> firstNameValidator = ctx.formParamAsClass("firstName", String.class)
                .check(it -> !it.isEmpty(), "First name can not be null");

        Validator<String> lastNameValidator = ctx.formParamAsClass("lastName", String.class)
                .check(it -> !it.isEmpty(), "Last name can not be null");

        Validator<String> emailValidator = ctx.formParamAsClass("email", String.class)
                .check(it -> it.contains("@"), "Email should be email (contain symbol @ and damain name)");

        Validator<String> passwordValidator = ctx.formParamAsClass("password", String.class)
                .check(it -> StringUtils.isNumeric(it), "Password must contain only digits")
                .check(it -> it.length() >= 4, "Password should be longer than 4 digits");


        Map<String, List<ValidationError<? extends Object>>> errors = JavalinValidation.collectErrors(
                firstNameValidator,
                lastNameValidator,
                emailValidator,
                passwordValidator
                );

        if(!errors.isEmpty()) {
            ctx.status(422);
            ctx.attribute("errors", errors);
            User invalidUser = new User(firstName, lastName, email, password);
            ctx.attribute("user", invalidUser);
            ctx.render("users/new.html");
            return;
        }

        User user = new User(firstName, lastName, email, password);
        user.save();
        ctx.sessionAttribute("flash", "User added successfully");
        ctx.redirect("/users");

    };
}
