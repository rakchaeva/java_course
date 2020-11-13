package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

public class ContactHelper extends BaseHelper {

    private Contacts contactCache = null;

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
        type(By.name("home"), contactData.getHomePhone());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("work"), contactData.getWorkPhone());
        type(By.name("email"), contactData.getEmailOne());
        attach(By.name("photo"), contactData.getPhoto());

        if (creation) {
            if (contactData.getGroups().size() > 0) {
                Assert.assertTrue(contactData.getGroups().size() == 1);
                new Select(wd.findElement(By.name("new_group"))).selectByVisibleText
                        (contactData.getGroups().iterator().next().getName());
            }
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
        contactCache = null;
    }

    public void modify(ContactData contact) {
        selectContactToEditById(contact.getId());
        fillContactForm(contact, false);
        submitContactModification();
        contactCache = null;
    }

    public void delete(ContactData contact) {
        selectContactCheckboxById(contact.getId());
        deleteSelectedContacts();
        confirmContactsDeletion();
        contactCache = null;
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

    public int count() {
        return wd.findElements(By.name("entry")).size();
    }

    public Contacts allOrCurrent(boolean allContacts) {
        //if getting all contacts, then return cache if it exists
        if (allContacts && contactCache != null) {
            return new Contacts(contactCache);
        }

        //if getting not all contacts or cache doesn't exist, then create new list
        Contacts currentContacts = new Contacts();
        List<WebElement> tableRows = wd.findElements(By.xpath("//table[@id='maintable']/tbody/tr[@name='entry']"));
        for (WebElement row : tableRows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            int id = Integer.parseInt(columns.get(0).findElement(By.tagName("input")).getAttribute("id"));
            String lastName = columns.get(1).getText();
            String firstName = columns.get(2).getText();
            String address = columns.get(3).getText();
            String allEmails = columns.get(4).getText();
            String allPhones = columns.get(5).getText();

            currentContacts.add(new ContactData()
                    .withId(id)
                    .withLastName(lastName)
                    .withFirstName(firstName)
                    .withAddress(address)
                    .withAllEmails(allEmails)
                    .withAllPhones(allPhones));
        }

        //cache for all contacts is updated
        if (allContacts) {
            contactCache = currentContacts;
        }

        return currentContacts;
    }

    public ContactData infoFromEditForm(ContactData contact) {
        selectContactToEditById(contact.getId());
        String firstName = wd.findElement(By.name("firstname")).getAttribute("value");
        String lastName = wd.findElement(By.name("lastname")).getAttribute("value");
        String homePhone = wd.findElement(By.name("home")).getAttribute("value");
        String mobilePhone = wd.findElement(By.name("mobile")).getAttribute("value");
        String workPhone = wd.findElement(By.name("work")).getAttribute("value");
        String emailOne = wd.findElement(By.name("email")).getAttribute("value");
        String emailTwo = wd.findElement(By.name("email2")).getAttribute("value");
        String emailThree = wd.findElement(By.name("email3")).getAttribute("value");
        String address = wd.findElement(By.name("address")).getAttribute("value");
        wd.navigate().back();
        return new ContactData()
                .withId(contact.getId())
                .withFirstName(firstName)
                .withLastName(lastName)
                .withHomePhone(homePhone)
                .withMobilePhone(mobilePhone)
                .withWorkPhone(workPhone)
                .withEmailOne(emailOne)
                .withEmailTwo(emailTwo)
                .withEmailThree(emailThree)
                .withAddress(address);
    }

    public void selectGroupOnHomePage(GroupData group) {
        new Select(wd.findElement(By.name("group"))).selectByValue(String.valueOf(group.getId()));
    }

    public void selectAllOnHomePage() {
        new Select(wd.findElement(By.name("group"))).selectByVisibleText("[all]");
    }

    public void addContactToGroup(ContactData contact, GroupData group) {
        selectContactCheckboxById(contact.getId());
        new Select(wd.findElement(By.name("to_group"))).selectByValue(String.valueOf(group.getId()));
        click(By.name("add"));
    }

    public void deleteContactFromGroup(ContactData contact, GroupData group) {
        selectGroupOnHomePage(group);
        selectContactCheckboxById(contact.getId());
        click(By.name("remove"));
    }

}
