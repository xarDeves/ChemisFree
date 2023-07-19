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

    private final NameToStructure nts = NameToStructure.getInstance();

    private String smiles;
    private String name;

    public SmileNameConverter(String molNameOrSmile) {
        parse(molNameOrSmile);
    }

    private String getDataFromURL(URL url) throws IOException {
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine();
    }

    private void parseSmilesFromName(String nameIn) throws InvalidSmilesException, IOException {
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

    private void parseNameFromSmiles(String smilesIn) throws InvalidSmilesException, IOException {
        smiles = smilesIn;
        name = getDataFromURL(
                new URL(
                        "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/smiles/"
                                + smiles +
                                "/property/IUPACName/TXT"
                )
        );
    }

    private void parse(String data) {
        data = data.toUpperCase();
        data = data.trim();
        data = data.replaceAll("\n", "");

        try {
            parseNameFromSmiles(data);
        } catch (InvalidSmilesException | IOException e) {
            // Invalid smiles, try parsing using name
            try {
                parseSmilesFromName(data);
            } catch (InvalidSmilesException | IOException invalidSmilesException) {
                // Invalid name
                invalidSmilesException.printStackTrace();
            }
        }
    }

    public String getMolName() {
        return name;
    }

    public String getMolSmiles() {
        return smiles;
    }
}
