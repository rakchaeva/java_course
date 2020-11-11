package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.File;
import java.util.Objects;

@XStreamAlias("contact")
public class ContactData {
    @XStreamOmitField
    private int id = Integer.MAX_VALUE;
    @Expose
    private String lastName;
    @Expose
    private String firstName;
    @Expose
    private String address;
    @Expose
    private String mobilePhone;
    @XStreamOmitField
    private String homePhone;
    @XStreamOmitField
    private String workPhone;
    @XStreamOmitField
    private String allPhones;
    @Expose
    private String emailOne;
    @XStreamOmitField
    private String emailTwo;
    @XStreamOmitField
    private String emailThree;
    @XStreamOmitField
    private String allEmails;
    @XStreamOmitField
    private String group;
    @XStreamOmitField
    private File photo;


    public ContactData withId(int id) {
        this.id = id;
        return this;
    }

    public ContactData withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ContactData withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ContactData withAddress(String address) {
        this.address = address;
        return this;
    }

    public ContactData withMobilePhone(String phone) {
        this.mobilePhone = phone;
        return this;
    }

    public ContactData withHomePhone(String homePhone) {
        this.homePhone = homePhone;
        return this;
    }

    public ContactData withWorkPhone(String workPhone) {
        this.workPhone = workPhone;
        return this;
    }

    public ContactData withAllPhones(String allPhones) {
        this.allPhones = allPhones;
        return this;
    }

    public ContactData withEmailOne(String emailOne) {
        this.emailOne = emailOne;
        return this;
    }

    public ContactData withEmailTwo(String emailTwo) {
        this.emailTwo = emailTwo;
        return this;
    }

    public ContactData withEmailThree(String emailThree) {
        this.emailThree = emailThree;
        return this;
    }

    public ContactData withAllEmails(String allEmails) {
        this.allEmails = allEmails;
        return this;
    }

    public ContactData withGroup(String group) {
        this.group = group;
        return this;
    }

    public ContactData withPhoto(File photo) {
        this.photo = photo;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public String getAllPhones() {
        return allPhones;
    }

    public String getEmailOne() {
        return emailOne;
    }

    public String getEmailTwo() {
        return emailTwo;
    }

    public String getEmailThree() {
        return emailThree;
    }

    public String getAllEmails() {
        return allEmails;
    }

    public String getGroup() {
        return group;
    }

    public File getPhoto() {
        return photo;
    }

    @Override
    public String toString() {
        return "ContactData{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactData that = (ContactData) o;
        return id == that.id &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(firstName, that.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName);
    }

}
