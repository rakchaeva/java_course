package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class AdminUISessionHelper extends BaseHelper {

    public AdminUISessionHelper(ApplicationManager app) {
        super(app);
    }

    public void login() {
        type(By.name("username"), app.getProperty("web.adminLogin"));
        click(By.xpath("//input[@value='Login']"));
        type(By.name("password"), app.getProperty("web.adminPassword"));
        click(By.xpath("//input[@value='Login']"));
    }

}
