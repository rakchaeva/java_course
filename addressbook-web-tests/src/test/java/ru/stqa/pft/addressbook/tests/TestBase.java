package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.remote.BrowserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestBase {

    protected static final ApplicationManager app
            = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));

    Logger logger = LoggerFactory.getLogger(TestBase.class);

    @BeforeSuite
    public void setUp() throws Exception {
        app.init();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws Exception {
        app.stop();
    }

    @BeforeMethod
    public void logTestStart(Method m, Object[] params) {
        logger.info("START test " + m.getName() + " with parameters: " + Arrays.asList(params));
    }

    @AfterMethod(alwaysRun = true)
    public void logTestStop(Method m) {
        logger.info("STOP test " + m.getName());
    }

    public void verifyGroupListOnUI() {
        if (Boolean.getBoolean("verifyUI")) {
            logger.info("START inner check: " + new Exception().getStackTrace()[0].getMethodName());
            Groups dbGroups = app.db().groups();
            Groups uiGroups = app.group().all();

            assertThat(uiGroups, equalTo(dbGroups.stream()
                    .map((g) -> new GroupData().withId(g.getId()).withName(g.getName()))
                    .collect(Collectors.toSet())));
            logger.info("STOP inner check: " + new Exception().getStackTrace()[0].getMethodName());
        }
    }

    public void verifyContactListOnUI() {
        if (Boolean.getBoolean("verifyUI")) {
            logger.info("START inner check: " + new Exception().getStackTrace()[0].getMethodName());
            Contacts dbContacts = app.db().contacts();
            Contacts uiContacts = app.contact().allOrCurrent(true);

            assertThat(uiContacts, equalTo(dbContacts
                    .stream()
                    .map((c) -> new ContactData()
                            .withId(c.getId())
                            .withLastName(c.getLastName())
                            .withFirstName(c.getFirstName())
                            .withAddress(cleanedAddress(c.getAddress()))
                            .withAllEmails(mergeEmails(c))
                            .withAllPhones(mergePhones(c)))
                    .collect(Collectors.toSet())));
            logger.info("STOP inner check: " + new Exception().getStackTrace()[0].getMethodName());
        }
    }

    public void verifyContactListInGroupOnUI(GroupData group) {
        if (Boolean.getBoolean("verifyUI")) {
            logger.info("START inner check: " + new Exception().getStackTrace()[0].getMethodName());
            Contacts dbContacts = app.db().getGroup(group).getContacts();
            app.contact().selectGroupOnHomePage(group);
            Contacts uiContacts = app.contact().allOrCurrent(false);

            assertThat(uiContacts, equalTo(dbContacts
                    .stream()
                    .map((c) -> new ContactData()
                            .withId(c.getId())
                            .withLastName(c.getLastName())
                            .withFirstName(c.getFirstName())
                            .withAddress(cleanedAddress(c.getAddress()))
                            .withAllEmails(mergeEmails(c))
                            .withAllPhones(mergePhones(c)))
                    .collect(Collectors.toSet())));
            logger.info("STOP inner check: " + new Exception().getStackTrace()[0].getMethodName());
        }

    }

    public String mergeEmails(ContactData contact) {
        return Arrays.asList(contact.getEmailOne(), contact.getEmailTwo(), contact.getEmailThree())
                .stream()
                .filter((s) -> !s.equals(""))
                .map(TestBase::cleanedEmail)
                .collect(Collectors.joining("\n"));
    }

    public static String cleanedEmail(String email) {
        return email.trim().replaceAll("\\s+", " ");
    }

    public String mergePhones(ContactData contact) {
        return Arrays.asList(contact.getHomePhone(), contact.getMobilePhone(), contact.getWorkPhone())
                .stream()
                .filter((s) -> !s.equals(""))
                .map(TestBase::cleanedPhone)
                .collect(Collectors.joining("\n"));
    }

    public static String cleanedPhone(String phone) {
        return phone.replaceAll("[\\s-()]", "");
    }

    public String cleanedAddress(String address) {
        //firstly, trim spaces before and after;
        //secondly, remove spaces at the beginning of each line;
        //thirdly, trim spaces inside the lines
        return address.trim()
                .replaceAll("\\n( )+", "\n")
                .replaceAll(" +", " ");
    }

}
