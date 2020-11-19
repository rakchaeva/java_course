package ru.stqa.pft.soap;

import com.lavasoft.GeoIPService;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.StringReader;

import static org.testng.Assert.assertEquals;

public class GeoIpServiceTests {

    @Test
    public void testMyIp() throws ParserConfigurationException, IOException, SAXException {
        String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("195.191.146.45");
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(ipLocation)));
        String country = doc.getChildNodes().item(0).getFirstChild().getTextContent();

        assertEquals(country, "RU");
    }

    @Test
    public void testInvalidIp() throws ParserConfigurationException, IOException, SAXException {
        String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("aaa");
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(ipLocation)));
        String country = doc.getChildNodes().item(0).getFirstChild().getTextContent();

        assertEquals(country, "US");
    }
}
