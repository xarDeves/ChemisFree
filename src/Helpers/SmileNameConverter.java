package Helpers;

import org.openscience.cdk.exception.InvalidSmilesException;
import uk.ac.cam.ch.wwmm.opsin.NameToStructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public final class SmileNameConverter {

    private static final NameToStructure nts = NameToStructure.getInstance();
    /*private static IAtomContainer atomContainer;
    private static final WeightDescriptor weightDescriptor = new WeightDescriptor();
    private static final SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());*/


    private static String smiles;
    private static String name;
    /*private static String weight;
    private static String stdinchi;
    private static String stdinchikey;
    private static String inchi;
    private static Element cml;*/


    private SmileNameConverter() {
    }

    private static String getDataFromURL(URL url) throws IOException {

        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine();

    }

    /*private static void generateInfoFromSmiles() throws InvalidSmilesException {

        cml = nts.parseToCML(smiles);

        NameToInchi nti = new NameToInchi();
        stdinchi = nti.parseToStdInchi(smiles);
        stdinchikey = nti.parseToStdInchiKey(smiles);
        inchi = nti.parseToInchi(smiles);

        //calc weight
        atomContainer = smilesParser.parseSmiles(SmileNameConverter.smiles);
        weight = weightDescriptor.calculate(atomContainer).getValue().toString();

    }*/

    private static void parseSmilesFromName(String nameIn) throws InvalidSmilesException, IOException {

        smiles = nts.parseToSmiles(nameIn);

        if (smiles == null) {

            smiles = getDataFromURL(
                    new URL("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/" + nameIn + "/property/CanonicalSMILES/TXT")
            );

        }

    }

    private static void parseNameFromSmiles(String smilesIn) throws InvalidSmilesException, IOException {

        smiles = smilesIn;

        name = getDataFromURL(
                new URL("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/smiles/" + smiles + "/property/IUPACName/TXT")
        );

    }

    public static String parse(String data) {

        data = data.toUpperCase();
        //TODO replace with trim ?
        data = data.replaceAll(" ", "");
        data = data.replaceAll("\n", "");

        try {
            parseNameFromSmiles(data);
        } catch (InvalidSmilesException | IOException e) {
            //System.out.println("invalid smiles");
            try {
                parseSmilesFromName(data);
            } catch (InvalidSmilesException | IOException invalidSmilesException) {
                //System.out.println("invalid name");
                invalidSmilesException.printStackTrace();
            }
        }

        return smiles;

    }

    /*public static void main(String[] args) throws InvalidSmilesException {

        //name = "(1R,6S)-2,6,8,8-tetramethyltricyclo[5.2.2.01,6]undec-2-ene";
        //name = "caffeine";
        //smiles = "CC1=CCC[C@@]2(C)C3CC[C@@]12CC3(C)C";
        smiles = "CCC1C(C(C(N(CC(CC(C(C(C(C(C(=O)O1)C)OC2CC(C(C(O2)C)O)(C)OC)C)OC3C(C(CC(O3)C)N(C)C)O)(C)O)C)C)C)O)(C)O";
        smiles = smiles.replaceAll(" ", "");

        //parseFromName();
        try {
            parseFromSmiles(smiles);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("name : " + name);
        System.out.println("smiles : " + smiles);
        System.out.println("weight : " + weight);
        System.out.println("stdinchi : " + stdinchi);
        System.out.println("stdinchikey : " + stdinchikey);
        System.out.println("inchi : " + inchi);
        System.out.println("cml : " + cml);

    }*/
}
