package exercise.controllers;
import io.javalin.http.Context;

import io.javalin.http.Handler;
import io.ebean.PagedList;
import java.util.List;

import exercise.domain.query.QArticle;
import exercise.domain.Article;
import exercise.domain.Category;
import exercise.domain.query.QCategory;

public final class ArticleController {

    public static Handler listArticles = ctx -> {
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        int rowsPerPage = 10;
        int offset = (page - 1) * rowsPerPage;

        PagedList<Article> pagedArticles = new QArticle()
            .setFirstRow(offset)
            .setMaxRows(rowsPerPage)
            .orderBy()
                .id.asc()
            .findPagedList();

        List<Article> articles = pagedArticles.getList();

        ctx.attribute("articles", articles);
        ctx.attribute("page", page);
        ctx.render("articles/index.html");
        removeFlashMessage(ctx);
    };

    public static Handler newArticle = ctx -> {
        List<Category> categories = new QCategory().findList();
        ctx.attribute("categories", categories);
        ctx.render("articles/new.html");
    };

    public static Handler createArticle = ctx -> {
        String title = ctx.formParam("title");
        String body = ctx.formParam("body");
        long categoryId = ctx.formParamAsClass("categoryId", Long.class).getOrDefault(null);

        Category category = new QCategory()
            .id.equalTo(categoryId)
            .findOne();

        Article article = new Article(title, body, category);
        article.save();

        ctx.sessionAttribute("flash", "Статья успешно создана");
        ctx.redirect("/articles");
    };

    public static Handler showArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Article article = new QArticle()
            .id.equalTo(id)
            .findOne();

        ctx.attribute("article", article);
        ctx.render("articles/show.html");
    };

    public static Handler editArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Article article = new QArticle()
                .id.equalTo(id)
                .findOne();


        List<Category> categories = new QCategory().findList();


        ctx.attribute("categories", categories);
        ctx.attribute("article", article);
        ctx.render("articles/edit.html");
    };

    public static Handler updateArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", long.class).getOrDefault(null);
        String title = ctx.formParam("title");
        String body = ctx.formParam("body");



       new QArticle()
               .id.equalTo(id)
               .asUpdate()
               .set("title", title)
               .set("body", body)
               .update();

        ctx.sessionAttribute("flash", "Статья успешно обновлена");
        ctx.redirect("/articles");
    };

    public static Handler deleteArticle = ctx -> {
        long id = ctx.pathParamAsClass("id", long.class).getOrDefault(null);

        Article article = new QArticle()
                .id.equalTo(id)
                .findOne();

        ctx.attribute("article", article);
        ctx.render("articles/delete.html");
    };

    public static Handler destroyArticle = ctx -> {

        long id = ctx.pathParamAsClass("id", long.class).getOrDefault(null);

      new QArticle()
              .id.equalTo(id)
              .delete();


        ctx.sessionAttribute("flash", "Статья успешно удалена");
        ctx.redirect("/articles");
    };

    private static void removeFlashMessage(Context ctx) {
        ctx.sessionAttribute("flash", null);
    }
}
