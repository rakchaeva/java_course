package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactDeletionFromGroupTests  extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        //checking presence of contacts added to at least one group
        if (app.db().contactsAddedToAnyGroup().size() == 0) {

            logger.info("START creating a contact 'cause no contacts added to at least one group exist");

            // checking presence of groups
            if (app.db().groups().size() == 0) {
                logger.info("START creating a group 'cause no groups exist");
                app.goTo().groupPage();
                app.group().create(new GroupData().withName("TestGroup1"));
            }

            app.goTo().homePage();
            app.contact().create(new ContactData()
                    .withFirstName("Ivan")
                    .withLastName("Ivanov")
                    .withAddress("Saint Petersburg")
                    .withMobilePhone("222")
                    .withEmailOne("ivanov@test.com")
                    .inGroup(app.db().groups().iterator().next()));
        }
        app.goTo().homePage();
    }

    @Test
    public void testDeletionOfContactFromGroup() {
        ContactData contactToBeDeleted = app.db().contactsAddedToAnyGroup().iterator().next();
        GroupData groupOfContact = contactToBeDeleted.getGroups().iterator().next();
        Contacts groupListBefore = groupOfContact.getContacts();
        app.contact().deleteContactFromGroup(contactToBeDeleted, groupOfContact);
        app.goTo().homePage();
        app.contact().selectGroupOnHomePage(groupOfContact);

        assertThat(app.contact().count(), equalTo(groupListBefore.size() - 1));

        Contacts groupListAfter = app.db().getGroup(groupOfContact).getContacts();
        assertThat(groupListAfter, equalTo(groupListBefore.without(contactToBeDeleted)));

        verifyContactListInGroupOnUI(groupOfContact);
    }
}
