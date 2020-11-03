package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ContactModificationTests extends TestBase {

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
                    .withPhone("+79111234567")
                    .withEmail("ivanov@test.com")
                    .withGroup("TestGroup1"));
        }
        app.goTo().homePage();
    }

    @Test
    public void testContactModification() {
        Contacts before = app.contact().all();
        ContactData modifiedContact = before.iterator().next();
        ContactData contact = new ContactData()
                .withId(modifiedContact.getId())
                .withFirstName("Petr")
                .withLastName("Petrov")
                .withAddress("Moscow")
                .withPhone("+79119876543")
                .withEmail("petrov@test.com");
        app.contact().modify(contact);
        app.goTo().homePage();
        Contacts after = app.contact().all();

        assertThat(after.size(), equalTo(before.size()));
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
    }

}
