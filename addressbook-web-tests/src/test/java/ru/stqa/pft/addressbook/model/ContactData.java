package ru.stqa.pft.addressbook.model;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;
import java.security.acl.Group;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@XStreamAlias("contact")
@Entity
@Table(name = "addressbook")
public class ContactData {
    @XStreamOmitField
    @Id
    @Column(name = "id")
    private int id = Integer.MAX_VALUE;

    @Expose
    @Column(name = "lastname")
    private String lastName;

    @Expose
    @Column(name = "firstname")
    private String firstName;

    @Expose
    @Column(name = "address")
    @Type(type = "text")
    private String address;

    @Expose
    @Column(name = "mobile")
    @Type(type = "text")
    private String mobilePhone;

    @XStreamOmitField
    @Column(name = "home")
    @Type(type = "text")
    private String homePhone;

    @XStreamOmitField
    @Column(name = "work")
    @Type(type = "text")
    private String workPhone;

    @XStreamOmitField
    @Transient
    private String allPhones;

    @Expose
    @Column(name = "email")
    @Type(type = "text")
    private String emailOne;

    @XStreamOmitField
    @Column(name = "email2")
    @Type(type = "text")
    private String emailTwo;

    @XStreamOmitField
    @Column(name = "email3")
    @Type(type = "text")
    private String emailThree;

    @XStreamOmitField
    @Transient
    private String allEmails;

    @Expose
    @Column(name = "photo")
    @Type(type = "text")
    private String photo;

    @XStreamOmitField
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "address_in_groups",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<GroupData> groups = new HashSet<>();


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

    public ContactData withPhoto(File photo) {
        this.photo = photo.getPath();
        return this;
    }

    public ContactData inGroup(GroupData group) {
        if (this.groups == null) {
            this.groups = new HashSet<>();
        }
        this.groups.add(group);
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

    public File getPhoto() {
        return photo == null ? null : new File(photo);
    }

    public Groups getGroups() {
        return new Groups(groups);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactData that = (ContactData) o;
        return id == that.id &&
                Objects.equals(replacedNullToEmpty(lastName), replacedNullToEmpty(that.lastName)) &&
                Objects.equals(replacedNullToEmpty(firstName), replacedNullToEmpty(that.firstName)) &&
                Objects.equals(replacedNullToEmpty(address), replacedNullToEmpty(that.address)) &&
                Objects.equals(replacedNullToEmpty(mobilePhone), replacedNullToEmpty(that.mobilePhone)) &&
                Objects.equals(replacedNullToEmpty(homePhone), replacedNullToEmpty(that.homePhone)) &&
                Objects.equals(replacedNullToEmpty(workPhone), replacedNullToEmpty(that.workPhone)) &&
                Objects.equals(replacedNullToEmpty(emailOne), replacedNullToEmpty(that.emailOne)) &&
                Objects.equals(replacedNullToEmpty(emailTwo), replacedNullToEmpty(that.emailTwo)) &&
                Objects.equals(replacedNullToEmpty(emailThree), replacedNullToEmpty(that.emailThree));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, address, mobilePhone, homePhone, workPhone, emailOne, emailTwo, emailThree);
    }

    @Override
    public String toString() {
        return "ContactData{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }

    private String replacedNullToEmpty(String str) {
        return str == null ? "" : str;
    }

}
