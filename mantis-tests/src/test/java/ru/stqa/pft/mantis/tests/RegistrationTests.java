package ru.stqa.pft.mantis.tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class RegistrationTests extends TestBase {

    // used only for tests with inner Mail Server -->
    // @BeforeMethod
    public void startMailServer() {
        app.mail().start();
    }


    @Test
    public void testRegistration() throws IOException, MessagingException {
        long now = System.currentTimeMillis();
        UserData user = new UserData()
                .withUsername(String.format("user%s", now))
                .withPassword("password")
                .withEmail(String.format("user%s@localhost", now));

        app.james().createUser(user);
        app.registration().start(user);

        // getting messages from inner Mail Server -->
        // List<MailMessage> mailMessages = app.mail().waitForMail(2, 10000);

        // getting messages from external Mail Server -->
        List<MailMessage> mailMessages = app.james().waitForMail(user, 60000);

        String confirmationLink = findConfirmationLink(mailMessages, user.getEmail());
        app.registration().finish(confirmationLink, user);
        assertTrue(app.newSession().login(user));
    }

    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    // used only for tests with inner Mail Server -->
    // @AfterMethod(alwaysRun = true)
    public void stopMailServer() {
        app.mail().stop();
    }
}
