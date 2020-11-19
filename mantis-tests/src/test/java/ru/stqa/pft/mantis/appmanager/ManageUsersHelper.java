package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;
import ru.stqa.pft.mantis.model.UserData;

import java.util.concurrent.TimeUnit;

public class ManageUsersHelper extends BaseHelper {

    public ManageUsersHelper(ApplicationManager app) {
        super(app);
    }

    public void openUserList() {
        click(By.xpath("//div[@id='sidebar']/ul/li[6]/a"));
        click(By.linkText("Manage Users"));
    }

    public void create(UserData user) {
        initUserCreation();
        fillNewUserForm(user);
        submitUserCreationForm();
    }

    public void initUserCreation() {
        click(By.linkText("Create New Account"));
    }

    public void fillNewUserForm(UserData user) {
        type(By.name("username"), user.getUsername());
        type(By.name("email"), user.getEmail());
    }

    public void submitUserCreationForm() {
        click(By.xpath("//input[@value='Create User']"));
    }

    public void initPasswordReset(UserData user) {
        selectUserForEdit(user);
        click(By.xpath("//input[@value='Reset Password']"));
    }

    public void selectUserForEdit(UserData user) {
        click(By.linkText(String.format("%s", user.getUsername())));
    }
}
