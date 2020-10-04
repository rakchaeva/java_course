package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.*;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.getNavigationHelper().goToContactForm();
    app.getContactHelper().fillContactForm(new ContactData("Ivan", "Ivanov", "Saint Petersburg", "+79111234567", "ivanov@test.com", "TestGroup1"), true);
    app.getContactHelper().submitContactForm();
    app.getNavigationHelper().goToHomePage();
  }

}
