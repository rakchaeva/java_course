package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.appmanager.HttpSession;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class PasswordResetTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() throws IOException {

        app.adminUISession().login();

        if (app.db().users().size() == 0) {
            long now = System.currentTimeMillis();
            app.manageUsers().openUserList();
            app.manageUsers().create(new UserData()
                    .withUsername(String.format("user%s", now))
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

        app.manageUsers().initPasswordReset(userWithNewPassword);
        List<MailMessage> mailMessages = app.mail().waitForMail(1, 10000);
        String resetLink = findResetLink(mailMessages, userWithNewPassword);
        app.registrationAndReset().confirmAction(resetLink, userWithNewPassword);

        HttpSession session = app.newSession();
        assertTrue(session.login(userWithNewPassword));
        assertTrue(session.isLoggedInAs(userWithNewPassword));
    }

    private String findResetLink(List<MailMessage> mailMessages, UserData user) {
        MailMessage mailMessage = mailMessages
                .stream()
                .filter((m) -> m.to.equals(user.getEmail()))
                .findFirst()
                .get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}
