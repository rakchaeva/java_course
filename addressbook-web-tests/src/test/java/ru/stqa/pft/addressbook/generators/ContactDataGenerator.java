package ru.stqa.pft.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.stqa.pft.addressbook.model.ContactData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

    @Parameter(names = "-c", description = "Contact count")
    public int count;

    @Parameter(names = "-f", description = "Target file")
    public String file;

    @Parameter(names = "-d", description = "Data format")
    public String dataFormat;

    public static void main(String[] args) throws IOException {
        ContactDataGenerator generator = new ContactDataGenerator();
        JCommander jCommander = new JCommander(generator);
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            jCommander.usage();
            return;
        }
        generator.run();
    }

    private void run() throws IOException {
        List<ContactData> contacts = generateContacts(count);
        if (dataFormat.equals("xml")) {
            saveAsXml(contacts, new File(file));
        } else if (dataFormat.equals("json")) {
            saveAsJson(contacts, new File(file));
        } else {
            System.out.println("Unrecognized format " + dataFormat);
        }
    }

    private List<ContactData> generateContacts(int count) {
        List<ContactData> contacts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            contacts.add(new ContactData()
                    .withFirstName(String.format("first name %s", i))
                    .withLastName(String.format("last name %s", i))
                    .withEmailOne(String.format("email%s@email.com", i))
                    .withMobilePhone(String.format("%s%s%s", i, i + 1, i + 2))
                    .withAddress(String.format("address %s", i)));
        }
        return contacts;
    }

    private void saveAsXml(List<ContactData> contacts, File file) throws IOException {
        XStream xstream = new XStream();
        xstream.processAnnotations(ContactData.class);
        String xml = xstream.toXML(contacts);
        Writer writer = new FileWriter(file);
        writer.write(xml);
        writer.close();
    }

    private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        String json = gson.toJson(contacts);
        Writer writer = new FileWriter(file);
        writer.write(json);
        writer.close();
    }
}
