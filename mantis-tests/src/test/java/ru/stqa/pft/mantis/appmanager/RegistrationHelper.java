package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;
import ru.stqa.pft.mantis.model.UserData;

public class RegistrationHelper extends BaseHelper {

    public RegistrationHelper(ApplicationManager app) {
        super(app);
    }

    public void start(UserData user) {
        wd.get(app.getProperty("web.baseUrl") + "/signup_page.php");
        type(By.name("username"), user.getUsername());
        type(By.name("email"), user.getEmail());
        click(By.cssSelector("input[value='Signup']"));
    }

    public void finish(String confirmationLink, UserData user) {
        wd.get(confirmationLink);
        type(By.name("password"), user.getPassword());
        type(By.name("password_confirm"), user.getPassword());
        click(By.cssSelector("button[type='submit']"));
    }
}
