package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactModificationTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        // checking presence of groups
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("TestGroup1"));
        }

        //checking presence of contact not added to any group
        if (app.db().contacts().size() == 0) {
            app.goTo().homePage();
            app.contact().create(new ContactData()
                    .withFirstName("Ivan")
                    .withLastName("Ivanov")
                    .withAddress("Saint Petersburg")
                    .withHomePhone("111")
                    .withMobilePhone("222")
                    .withWorkPhone("333")
                    .withEmailOne("ivanov@test.com"));
        }
        app.goTo().homePage();
    }

    @Test
    public void testContactModification() {
        Contacts before = app.db().contacts();
        ContactData contactToBeModified = before.iterator().next();
        ContactData contactNew = new ContactData()
                .withId(contactToBeModified.getId())
                .withFirstName("Petr")
                .withLastName("Petrov")
                .withAddress("Moscow")
                .withHomePhone("444")
                .withMobilePhone("555")
                .withWorkPhone("666")
                .withEmailOne("petrov@test.com");
        app.contact().modify(contactNew);
        app.goTo().homePage();

        assertThat(app.contact().count(), equalTo(before.size()));

        Contacts after = app.db().contacts();
        assertThat(after, equalTo(before.without(contactToBeModified).withAdded(contactNew)));

        verifyContactListOnUI();
    }

}
