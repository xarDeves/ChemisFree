package Helpers;

import Core.Views.TextEditor.Article;
import Core.Views.TextEditor.TextPanel;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public final class XmlIoManager {

    private static DocumentBuilder saveFileBuilder;


    private XmlIoManager() {
    }

    static {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //factory.setValidating(true);
        factory.setNamespaceAware(true);

        File schemaFile = new File("ma.xsd");

        // create schema
        String constant = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory xsdFactory = SchemaFactory.newInstance(constant);
        Schema schema = null;
        try {
            schema = xsdFactory.newSchema(schemaFile);
        } catch (SAXException e) {
            e.printStackTrace();
        }
        factory.setSchema(schema);

        try {
            saveFileBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        saveFileBuilder.setErrorHandler(
                new ErrorHandler() {

                    @Override
                    public void warning(SAXParseException e) {
                        System.out.println("WARNING: " + e.getMessage());
                    }

                    @Override
                    public void error(SAXParseException e) throws SAXException {
                        JOptionPane.showMessageDialog(null, "ERROR! File appears to be corrupted");
                        throw e;
                    }

                    @Override
                    public void fatalError(SAXParseException e) throws SAXException {
                        JOptionPane.showMessageDialog(null, "ERROR! File appears to be corrupted");
                        throw e;
                    }
                }
        );
    }

    //TODO save binary
    public static void saveXml(String path, LinkedList<Article> articles) {

        org.w3c.dom.Document document = saveFileBuilder.newDocument();

        // root element
        Element root = document.createElement("articles");
        document.appendChild(root);

        for (Article article : articles) {

            String title = article.getTitleTextPane();
            String data = article.getDataTextPane();

            Element articleElement = document.createElement("article");
            root.appendChild(articleElement);

            Attr attr = document.createAttribute("title");
            attr.setValue(title);
            articleElement.setAttributeNode(attr);

            Element dataElement = document.createElement("data");
            dataElement.appendChild(document.createTextNode(data));
            articleElement.appendChild(dataElement);
        }

        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(path));
        try {
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    //TODO load binary
    //way too tightly coupled, not of much importance tho, this will only get used by a TextPanel
    public static void loadAndDisplay(String path, LinkedList<Article> articles) {

        File xmlFile = new File(path);

        org.w3c.dom.Document doc = null;
        try {
            doc = saveFileBuilder.parse(xmlFile);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("article");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                String uid = elem.getAttribute("title");

                Node node = elem.getElementsByTagName("data").item(0);
                String text = node.getTextContent();

                articles.add(new Article(uid, text));
            }
        }

    }

}