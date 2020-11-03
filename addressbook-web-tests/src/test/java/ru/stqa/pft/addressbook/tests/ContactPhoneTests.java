package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

public class ContactPhoneTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        // checking presence of groups
        app.goTo().groupPage();
        if (app.group().all().size() == 0) {
            app.group().create(new GroupData().withName("TestGroup1"));
        }

        //checking presence of contacts
        app.goTo().homePage();
        if (app.contact().all().size() == 0) {
            app.contact().create(new ContactData()
                    .withFirstName("Ivan")
                    .withLastName("Ivanov")
                    .withAddress("Saint Petersburg")
                    .withMobilePhone("+79111234567")
                    .withEmail("ivanov@test.com")
                    .withGroup("TestGroup1"));
        }
        app.goTo().homePage();
    }

    @Test (enabled = false)
    public void testContactPhones() {
        app.goTo().homePage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);
    }
}
