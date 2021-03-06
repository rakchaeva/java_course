package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactEmailsTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        // checking presence of groups
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("TestGroup1"));
        }

        //checking presence of contacts
        if (app.db().contacts().size() == 0) {
            app.goTo().homePage();
            app.contact().create(new ContactData()
                    .withFirstName("Ivan")
                    .withLastName("Ivanov")
                    .withAddress("Saint Petersburg")
                    .withHomePhone("111")
                    .withMobilePhone("222")
                    .withWorkPhone("333")
                    .withEmailOne("ivanov@test.com")
                    .withEmailTwo("ivanov@gmail.com")
                    .withEmailThree("ivanov@ya.ru"));
        }
        app.goTo().homePage();
        app.contact().selectAllOnHomePage();
    }

    @Test
    public void testContactEmails() {
        app.goTo().homePage();
        ContactData contact = app.contact().allOrCurrent(true).iterator().next();
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);

        assertThat(contact.getAllEmails(), equalTo(mergeEmails(contactInfoFromEditForm)));
    }

}
