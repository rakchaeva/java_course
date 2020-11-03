package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.util.List;

public class ContactHelper extends BaseHelper {

    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void submitContactForm() {
        click(By.xpath("(//input[@name='submit'])[2]"));
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstName());
        type(By.name("lastname"), contactData.getLastName());
        type(By.name("address"), contactData.getAddress());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("email"), contactData.getEmail());

        if (creation) {
            new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroup());
        } else {
            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }
    }

    public void selectContactCheckboxById(int id) {
        wd.findElement(By.xpath("//table[@id='maintable']/tbody/tr/td/input[@id='" + id + "']")).click();
    }

    public void deleteSelectedContacts() {
        click(By.xpath("//input[@value='Delete']"));
    }

    public void confirmContactsDeletion() {
        confirmAlert();
    }

    public void selectContactToEditById(int id) {
        wd.findElement(By.xpath(String.format("//tr[.//input[@id='%s']]/td[8]/a", id))).click();
    }

    public void submitContactModification() {
        click(By.xpath("(//input[@name='update'])[2]"));
    }

    public void create(ContactData contact) {
        initContactCreation();
        fillContactForm(contact, true);
        submitContactForm();
    }

    public void modify(ContactData contact) {
        selectContactToEditById(contact.getId());
        fillContactForm(contact, false);
        submitContactModification();
    }

    public void delete(ContactData contact) {
        selectContactCheckboxById(contact.getId());
        deleteSelectedContacts();
        confirmContactsDeletion();
    }

    public void initContactCreation() {
        if (isElementPresent(By.tagName("h1"))
                && wd.findElement(By.tagName("h1")).getText().equals("Edit / add address book entry")) {
            return;
        }
        click(By.linkText("add new"));
    }

    public boolean isThereAContact() {
        return isElementPresent(By.xpath("//tr[2]/td/input"));
    }

    public Contacts all() {
        Contacts contacts = new Contacts();
        List<WebElement> tableRows = wd.findElements(By.xpath("//table[@id='maintable']/tbody/tr[@name='entry']"));
        for (WebElement row : tableRows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            int id = Integer.parseInt(columns.get(0).findElement(By.tagName("input")).getAttribute("id"));
            String lastName = columns.get(1).getText();
            String firstName = columns.get(2).getText();
            contacts.add(new ContactData().withId(id).withLastName(lastName).withFirstName(firstName));
        }
        return contacts;
    }

    public ContactData infoFromEditForm(ContactData contact) {
        selectContactToEditById(contact.getId());
        String firstName = wd.findElement(By.name("firstname")).getAttribute("value");
        String lastName = wd.findElement(By.name("lastname")).getAttribute("value");;
        String homePhone = wd.findElement(By.name("home")).getAttribute("value");;
        String mobilePhone = wd.findElement(By.name("mobile")).getAttribute("value");;
        String workPhone = wd.findElement(By.name("work")).getAttribute("value");;
        wd.navigate().back();
        return new ContactData()
                .withId(contact.getId())
                .withFirstName(firstName)
                .withLastName(lastName)
                .withHomePhone(homePhone)
                .withMobilePhone(mobilePhone)
                .withWorkPhone(workPhone);
    }
}
