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

    private static String smiles;
    private static String name;

    private SmileNameConverter() {
    }

    private static String getDataFromURL(URL url) throws IOException {

        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine();
    }

    private static void parseSmilesFromName(String nameIn) throws InvalidSmilesException, IOException {

        smiles = nts.parseToSmiles(nameIn);

        if (smiles == null) {

            smiles = getDataFromURL(
                    new URL(
                            "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/"
                                    + nameIn +
                                    "/property/CanonicalSMILES/TXT"
                    )
            );
        }
    }

    private static void parseNameFromSmiles(String smilesIn) throws InvalidSmilesException, IOException {

        smiles = smilesIn;

        name = getDataFromURL(
                new URL(
                        "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/smiles/"
                                + smiles +
                                "/property/IUPACName/TXT"
                )
        );
    }

    public static String parse(String data) {

        data = data.toUpperCase();
        data = data.trim();
        data = data.replaceAll("\n", "");

        try {
            parseNameFromSmiles(data);
        } catch (InvalidSmilesException | IOException e) {
            //invalid smiles
            try {
                parseSmilesFromName(data);
            } catch (InvalidSmilesException | IOException invalidSmilesException) {
                //invalid name
                invalidSmilesException.printStackTrace();
            }
        }

        return smiles;
    }

}
