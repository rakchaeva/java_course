package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.goTo().goToHomePage();
    List<ContactData> before = app.getContactHelper().getContactList();

    app.goTo().goToContactForm();
    ContactData contact = new ContactData("Ivan", "Ivanov", "Saint Petersburg", "+79111234567", "ivanov@test.com", "TestGroup1");
    app.getContactHelper().createContact(contact);

    app.goTo().goToHomePage();
    List<ContactData> after = app.getContactHelper().getContactList();

    Assert.assertEquals(after.size(), before.size() + 1);

    before.add(contact);
    Comparator<? super ContactData> byLastAndFirstName =
            Comparator.comparing(ContactData::getLastName).thenComparing(ContactData::getFirstName);
    before.sort(byLastAndFirstName);
    after.sort(byLastAndFirstName);
    Assert.assertEquals(before, after);
  }

}
