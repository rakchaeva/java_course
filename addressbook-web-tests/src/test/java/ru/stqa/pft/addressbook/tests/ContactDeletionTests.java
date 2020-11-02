package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.List;

public class ContactDeletionTests extends TestBase {

    @Test
    public void testContactDeletion() {
        app.goTo().goToHomePage();
        if (!app.getContactHelper().isThereAContact()) {
            app.getContactHelper().createContact(new ContactData("Ivan", "Ivanov", "Saint Petersburg", "+79111234567", "ivanov@test.com", "TestGroup1"));
        }

        app.goTo().goToHomePage();
        List<ContactData> before = app.getContactHelper().getContactList();

        app.getContactHelper().selectContactCheckbox(before.size() - 1);
        app.getContactHelper().deleteSelectedContacts();
        app.getContactHelper().confirmContactsDeletion();

        app.goTo().goToHomePage();
        List<ContactData> after = app.getContactHelper().getContactList();

        Assert.assertEquals(after.size(), before.size() - 1);

        before.remove(before.size() - 1);
        Assert.assertEquals(before, after);
    }
}
