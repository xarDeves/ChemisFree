package Helpers;

import nu.xom.Document;
import org.w3c.dom.Element;
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

    private final LinkedList<String> splitString = new LinkedList<>();
    private NodeList molNodes;

    public XmlTagFormatter() {

        try {
            parserBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void generateAndSplitTags(String data) {

        POSContainer posContainer = ChemistryPOSTagger.getDefaultInstance().runTaggers(data);
        ChemistrySentenceParser chemistrySentenceParser = new ChemistrySentenceParser(posContainer);

        chemistrySentenceParser.parseTags();
        InputSource is = new InputSource(new StringReader(chemistrySentenceParser.makeXMLDocument().toXML()));
        //save xml doc
        Document docForXmlOut = chemistrySentenceParser.makeXMLDocument();
        Utils.writeXMLToFile(docForXmlOut, "ParserXmlOut/taggerOut.xml");

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
            XPathExpression expr = xpath.compile("//OSCARCM//*");

            NodeList resultAll = (NodeList) fullTextExpr.evaluate(x, XPathConstants.NODESET);
            molNodes = (NodeList) expr.evaluate(x, XPathConstants.NODESET);

            splitString.clear();
            for (int i = 0; i < resultAll.getLength(); i++) {

                if (resultAll.item(i).getFirstChild().getNodeValue() != null) {
                    splitString.add(resultAll.item(i).getTextContent());
                }
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public NodeList getMolNodes() {
        return this.molNodes;
    }

    public LinkedList<String> getRawTags() {
        return this.splitString;
    }

}
