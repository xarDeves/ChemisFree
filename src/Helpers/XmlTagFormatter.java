package Helpers;

import nu.xom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.cam.ch.wwmm.chemicaltagger.ChemistryPOSTagger;
import uk.ac.cam.ch.wwmm.chemicaltagger.ChemistrySentenceParser;
import uk.ac.cam.ch.wwmm.chemicaltagger.POSContainer;
import uk.ac.cam.ch.wwmm.chemicaltagger.Utils;

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

    private void saveXml(ChemistrySentenceParser chemistrySentenceParser) {
        Document docForXmlOut = chemistrySentenceParser.makeXMLDocument();
        Utils.writeXMLToFile(docForXmlOut, "ParserXmlOut/taggerOut.xml");
    }

    public void filterTags(String data) {

        POSContainer posContainer = ChemistryPOSTagger.getDefaultInstance().runTaggers(data);
        ChemistrySentenceParser chemistrySentenceParser = new ChemistrySentenceParser(posContainer);

        chemistrySentenceParser.parseTags();
        InputSource is = new InputSource(new StringReader(chemistrySentenceParser.makeXMLDocument().toXML()));

        //saveXml(chemistrySentenceParser);

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
        int childLen, count;
        Node node;

        for (count = 0; count < allTags.getLength(); count++) {
            node = allTags.item(count);

            if (node.getNodeName().equals("OSCARCM")) {

                //this mess is necessary since "OSCARCM" tags contain molecules as well as characters like "/"
                childLen = node.getChildNodes().getLength();
                for (int i = 0; i < childLen; i++) {
                    node = allTags.item(++count);

                    if (node.getNodeName().equals("OSCAR-CM"))
                        this.molStringBuilder.append(node.getTextContent()).append(" ");
                    else {
                        this.molTags.add(this.molStringBuilder.toString());
                        this.rawTags.add(this.molStringBuilder.toString());
                        this.rawTags.add(node.getTextContent());
                        this.molStringBuilder.delete(0, this.molStringBuilder.length());
                    }
                }

                if (!molStringBuilder.isEmpty()) {
                    this.molTags.add(this.molStringBuilder.toString());
                    this.rawTags.add(this.molStringBuilder.toString());
                    this.molStringBuilder.delete(0, this.molStringBuilder.length());
                }

            } else if (node.getFirstChild().getNodeValue() != null) {
                this.rawTags.add(node.getTextContent());
            }
        }

    }

    public LinkedList<String> getMolTags() {
        return this.molTags;
    }

    public LinkedList<String> getRawTags() {
        return this.rawTags;
    }
}
