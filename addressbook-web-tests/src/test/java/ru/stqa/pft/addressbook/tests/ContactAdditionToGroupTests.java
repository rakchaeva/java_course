package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAdditionToGroupTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        // checking presence of groups
        if (app.db().groups().size() == 0) {
            logger.info("START creating a group 'cause no groups exist");
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("TestGroup1"));
        }

        //checking presence of contacts not added to at least one group
        if (app.db().contactsNotAddedToAnyGroup().size() == 0) {
            logger.info("START creating a contact 'cause no contacts not added to at least one group exist");
            app.goTo().homePage();
            app.contact().create(new ContactData()
                    .withFirstName("Ivan")
                    .withLastName("Ivanov")
                    .withAddress("Saint Petersburg")
                    .withMobilePhone("222")
                    .withEmailOne("ivanov@test.com"));
        }
        app.goTo().homePage();
    }

    @Test
    public void testAdditionOfContactToGroup() {
        ContactData contactToBeAdded = app.db().contactsNotAddedToAnyGroup().iterator().next();
        GroupData groupForContact = app.db().groups()
                .stream()
                .filter((g) -> !contactToBeAdded.getGroups().contains(g))
                .collect(Collectors.toSet())
                .iterator().next();
        Contacts groupListBefore = groupForContact.getContacts();
        app.contact().addContactToGroup(contactToBeAdded, groupForContact);
        app.goTo().homePage();
        app.contact().selectGroupOnHomePage(groupForContact);

        assertThat(app.contact().count(), equalTo(groupListBefore.size() + 1));

        Contacts groupListAfter = app.db().getGroup(groupForContact).getContacts();
        assertThat(groupListAfter, equalTo(groupListBefore.withAdded(contactToBeAdded)));

        verifyContactListInGroupOnUI(groupForContact);
    }

}
