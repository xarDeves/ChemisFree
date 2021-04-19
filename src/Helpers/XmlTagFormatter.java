package Helpers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.cam.ch.wwmm.chemicaltagger.ChemistryPOSTagger;
import uk.ac.cam.ch.wwmm.chemicaltagger.ChemistrySentenceParser;
import uk.ac.cam.ch.wwmm.chemicaltagger.POSContainer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

public final class XmlTagFormatter {

    private DocumentBuilder parserBuilder;

    private final LinkedList<String> rawTags = new LinkedList<>();
    private final LinkedList<String> molTags = new LinkedList<>();
    private final StringBuilder molStringBuilder = new StringBuilder();

    public XmlTagFormatter() {

        try {
            parserBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void filterTags(String data) {

        POSContainer posContainer = ChemistryPOSTagger.getDefaultInstance().runTaggers(data);
        ChemistrySentenceParser chemistrySentenceParser = new ChemistrySentenceParser(posContainer);

        chemistrySentenceParser.parseTags();
        InputSource is = new InputSource(new StringReader(chemistrySentenceParser.makeXMLDocument().toXML()));
        //save xml doc
        //Document docForXmlOut = chemistrySentenceParser.makeXMLDocument();
        //Utils.writeXMLToFile(docForXmlOut, "ParserXmlOut/taggerOut.xml");

        org.w3c.dom.Document docForParse = null;
        try {
            docForParse = parserBuilder.parse(is);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        docForParse.getDocumentElement().normalize();

        Element x = docForParse.getDocumentElement();
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        try {
            XPathExpression fullTextExpr = xpath.compile("//Sentence//*");
            NodeList allTags = (NodeList) fullTextExpr.evaluate(x, XPathConstants.NODESET);

            extract(allTags);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private void extract(NodeList allTags) {

        this.molTags.clear();
        this.rawTags.clear();
        Node node;
        int count = 0;

        while (count < allTags.getLength()) {
            node = allTags.item(count);

            if (node.getNodeName().equals("OSCARCM") || node.getNodeName().equals("OSCAR-CM")) {
                while (node.getNodeName().equals("OSCAR-CM")) {
                    this.molStringBuilder.append(node.getTextContent()).append(" ");
                    count++;
                    node = allTags.item(count);
                }

                if (!this.molStringBuilder.isEmpty()) {
                    this.molTags.add(this.molStringBuilder.toString());
                    this.rawTags.add(this.molStringBuilder.toString());
                }

                this.molStringBuilder.delete(0, this.molStringBuilder.length());
            }

            if (node.getFirstChild().getNodeValue() != null)
                this.rawTags.add(node.getTextContent());

            count++;
        }
    }

    public LinkedList<String> getMolTags() {
        return this.molTags;
    }

    public LinkedList<String> getRawTags() {
        return this.rawTags;
    }

}
