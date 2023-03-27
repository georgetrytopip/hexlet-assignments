package exercise;

import exercise.servlet.WelcomeServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;

import java.io.File;

public class App {

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.valueOf(port);
        }
        return 8000;
    }

    public static Tomcat getApp(int port) {
        Tomcat app = new Tomcat();

        app.setBaseDir(System.getProperty("java.io.tmpdir"));
        app.setPort(port);

        Context ctx = app.addContext("", new File(".").getAbsolutePath());

        app.addServlet(ctx, "WelcomeServlet", new WelcomeServlet());
        // Назначаем сервлет как обработчик запросов по пути "/hello"
        // На примере хоста и порта выше - "http://localhost:8080/hello"
        ctx.addServletMappingDecoded("/", "WelcomeServlet"); //тут нужно прописывать название сервлета

        return app;
    }

    public static void main(String[] args) throws LifecycleException {
        Tomcat app = getApp(getPort());
        app.start();
        app.getServer().await();
    }
}
