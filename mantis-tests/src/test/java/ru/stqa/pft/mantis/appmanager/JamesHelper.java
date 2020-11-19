package ru.stqa.pft.mantis.appmanager;

import org.apache.commons.net.telnet.TelnetClient;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JamesHelper {

    private ApplicationManager app;
    private TelnetClient telnet;
    private InputStream in;
    private PrintStream out;
    private Session mailSession;
    private Store store;
    private String mailServer;

    public JamesHelper(ApplicationManager app) {
        this.app = app;
        this.telnet = new TelnetClient();
        this.mailSession = Session.getDefaultInstance(System.getProperties());
    }

    public void createUser(UserData user) {
        initTelnetSession();
        write("adduser " + user.getUsername() + " " + user.getPassword());
        String result = readUntil("User " + user.getUsername() + " added");
        closeTelnetSession();
    }

    public void deleteUser(UserData user) {
        initTelnetSession();
        write("deluser " + user.getUsername());
        String result = readUntil("User " + user.getUsername() + " deleted");
        closeTelnetSession();
    }

    public boolean doesUserExist(UserData user) {
        initTelnetSession();
        write("verify " + user.getUsername());
        String result = readUntil("exist");
        closeTelnetSession();
        return result.trim().equals("User " + user.getUsername() + " exists");
    }

    public List<MailMessage> waitForMail(UserData user, int timeout) throws MessagingException {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() < start + timeout) {
            List<MailMessage> allMail = getAllMail(user);
            if (allMail.size() > 0) {
                return allMail;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        throw new Error("No mail :(");
    }

    private void initTelnetSession() {
        mailServer = app.getProperty("mailserver.host");
        int port = Integer.parseInt(app.getProperty("mailserver.port"));
        String login = app.getProperty("mailserver.adminlogin");
        String password = app.getProperty("mailserver.adminpassword");

        try {
            telnet.connect(mailServer, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        readUntil("Login id:");
        write("");
        readUntil("Password:");
        write("");

        readUntil("Login id:");
        write(login);
        readUntil("Password:");
        write(password);

        readUntil("Welcome " + login + ". HELP for a list of commands");
    }

    private void closeTelnetSession() {
        write("quit");
    }

    private String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                System.out.print(ch);
                sb.append(ch);
                if (ch == lastChar && sb.toString().endsWith(pattern)) {
                    return sb.toString();
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void write(String value) {
        try {
            out.println(value);
            out.flush();
            System.out.println(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<MailMessage> getAllMail(UserData user) throws MessagingException {
        Folder inbox = openInbox(user);
        List<MailMessage> messages = Arrays.asList(inbox.getMessages()).stream()
                .map((m) -> toModelMail(m)).collect(Collectors.toList());
        closeFolder(inbox);
        return messages;
    }

    private void closeFolder(Folder folder) throws MessagingException {
        folder.close(true);
        store.close();
    }

    private Folder openInbox(UserData user) throws MessagingException {
        store = mailSession.getStore("pop3");
        store.connect(mailServer, user.getUsername(), user.getPassword());
        Folder folder = store.getDefaultFolder().getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        return folder;
    }

    private MailMessage toModelMail(Message m) {
        try {
            return new MailMessage(m.getAllRecipients()[0].toString(), (String) m.getContent());
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void drainEmail(UserData user) throws MessagingException {
        Folder inbox = openInbox(user);
        for (Message message : inbox.getMessages()) {
            message.setFlag(Flags.Flag.DELETED, true);
        }
        closeFolder(inbox);
    }
}