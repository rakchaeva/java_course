package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.mantis.appmanager.HttpSession;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class PasswordResetTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() throws IOException {
        HttpSession session = app.newSession();
        UserData adminUser = new UserData()
                .withUsername(app.getProperty("web.adminLogin"))
                .withPassword(app.getProperty("web.adminPassword"));
        session.login(adminUser);

        if (app.db().users().size() == 0) {
            long now = System.currentTimeMillis();
            app.manageUsers().openUserList();
            app.manageUsers().create(new UserData()
                    .withUsername(String.format("user%s", now))
                    .withPassword("password")
                    .withEmail(String.format("user%s@localhost", now)));
        }
        app.manageUsers().openUserList();
        app.mail().start();
    }

    @Test
    public void testPasswordReset() throws IOException {
        long now = System.currentTimeMillis();
        UserData userToReset = app.db().users().iterator().next();
        UserData userWithNewPassword = new UserData()
                .withId(userToReset.getId())
                .withUsername(userToReset.getUsername())
                .withEmail(userToReset.getEmail())
                .withPassword(userToReset.getPassword() + now);

        app.manageUsers().initPasswordReset(userWithNewPassword); // .selectUser --> click reset
        List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000); // count ?
        String resetLink = findResetLink(mailMessages, userWithNewPassword);
        app.passwordReset().finish(resetLink, userWithNewPassword);

        assertTrue(app.newSession().login(userWithNewPassword));
    }

    private String findResetLink(List<MailMessage> mailMessages, UserData user) {
        //TO DO
        return null;
    }

    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}
