package Helpers;

import Core.Views.TextEditor.Article;
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
import java.util.HashMap;
import java.util.LinkedList;

public final class XmlIoManager {

    private static DocumentBuilder SAVE_FILE_BUILDER;

    static {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // factory.setValidating(true);
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
            SAVE_FILE_BUILDER = factory.newDocumentBuilder();
            SAVE_FILE_BUILDER.setErrorHandler(new ErrorHandler() {
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
            });
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            SAVE_FILE_BUILDER = null;
        }
    }

    private XmlIoManager() {
    }

    public static void saveXml(String path, LinkedList<Article> articles) {
        if (SAVE_FILE_BUILDER == null) {
            System.err.println("DocumentBuilder is not initialized correctly.");
            return;
        }

        org.w3c.dom.Document document = SAVE_FILE_BUILDER.newDocument();

        // root element
        Element root = document.createElement("articles");
        document.appendChild(root);

        for (Article article : articles) {
            String title = article.getTitleText();
            String data = article.getArticleText();

            Element articleElement = document.createElement("article");
            root.appendChild(articleElement);

            Attr attr = document.createAttribute("title");
            attr.setValue(title);
            articleElement.setAttributeNode(attr);

            Element dataElement = document.createElement("data");
            dataElement.appendChild(document.createTextNode(data));
            articleElement.appendChild(dataElement);
        }

        // transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return;
        }
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(path));
        try {
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void loadAndDisplay(String path, HashMap<String, String> articles) {
        if (SAVE_FILE_BUILDER == null) {
            System.err.println("DocumentBuilder is not initialized correctly.");
            return;
        }

        File xmlFile = new File(path);

        org.w3c.dom.Document doc = null;
        try {
            doc = SAVE_FILE_BUILDER.parse(xmlFile);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return;
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

                articles.put(uid, text);
            }
        }
    }
}