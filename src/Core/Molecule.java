package Core;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.descriptors.molecular.*;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.renderer.color.IAtomColorer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import uk.ac.cam.ch.wwmm.opsin.NameToInchi;
import uk.ac.cam.ch.wwmm.opsin.NameToStructure;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;

//TODO if no info could be retrieved, implement not found mechanism
//TODO inherit from "SmileNameConverter"
public final class Molecule {

    private static final SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
    private static final NameToStructure nts = NameToStructure.getInstance();
    private static final WeightDescriptor weightDescriptor = new WeightDescriptor();
    private static final XLogPDescriptor xlogPDescritpor = new XLogPDescriptor();
    private static final AtomCountDescriptor atomCountDescriptor = new AtomCountDescriptor();
    private static final HBondAcceptorCountDescriptor hBondAcceptorDescriptor = new HBondAcceptorCountDescriptor();
    private static final HBondDonorCountDescriptor hBondDonorDescriptor = new HBondDonorCountDescriptor();
    private static final RotatableBondsCountDescriptor rotatableBondsCountDescriptor = new RotatableBondsCountDescriptor();
    private static final MannholdLogPDescriptor logPDescriptor = new MannholdLogPDescriptor();
    private static final TPSADescriptor tpsaDescriptor = new TPSADescriptor();
    private static final AromaticAtomsCountDescriptor aromaticAtomCountDescriptor = new AromaticAtomsCountDescriptor();
    private static final NameToInchi nti = new NameToInchi();
    private static ALOGPDescriptor aLogPDescriptor;

    static {
        try {
            aLogPDescriptor = new ALOGPDescriptor();
        } catch (CDKException e) {
            e.printStackTrace();
        }
    }

    public IAtomContainer atomContainer;
    public String name;
    public String smiles;
    public String weight;
    public String xlogP;
    public String logP;
    public String tpsa;
    public String atomCount;
    public String aromaticAtomCount;
    public String hBondAcceptorCount;
    public String hBondDonorCount;
    public String rotatableBondsCount;
    public String stdinchi;
    public String stdinchikey;
    public String inchi;
    public IDescriptorResult aLogP;
    public Double logS;
    public Double logSp;


    public ArrayList<String> ruleOf5;
    public ArrayList<String> ghoseVeber;
    public ArrayList<String> leadLikeness;

    private void determineRuleOfFive() {

        this.ruleOf5 = new ArrayList<>();

        if (Double.parseDouble(weight) > 500) {
            this.ruleOf5.add("Weight > 500");
        }
        if (Integer.parseInt(hBondAcceptorCount) > 10) {
            this.ruleOf5.add("Acceptors > 10");
        }
        if (Integer.parseInt(hBondDonorCount) > 5) {
            this.ruleOf5.add("Donors > 5");
        }
        if (Double.parseDouble(logP) > 5) {
            this.ruleOf5.add("LogP > 5");
        }
    }

    private void determineGhoseVeber() {

        this.ghoseVeber = new ArrayList<>();

        if (Double.parseDouble(weight) > 480.0) {
            this.ghoseVeber.add("Weight > 480 ");
        }
        if (Double.parseDouble(weight) < 180.0) {
            this.ghoseVeber.add("Weight < 180 ");
        }
        if (Integer.parseInt(atomCount) > 70.0) {
            this.ghoseVeber.add("Number of atoms > 70 ");
        }
        if (Integer.parseInt(atomCount) < 20.0) {
            this.ghoseVeber.add("Number of atoms < 20 ");
        }
        if (Integer.parseInt(rotatableBondsCount) > 10.0) {
            this.ghoseVeber.add("Number of Rotatable bonds > 10 ");
        }
        if (Double.parseDouble(logP) > 5.6) {
            System.out.print("LogP > 5.6 ");
        }
        if (Double.parseDouble(logP) < -0.4) {
            this.ghoseVeber.add("LogP < -0.4 ");
        }
        if (Double.parseDouble(tpsa) > 140.0) {
            this.ghoseVeber.add("TPSA > 140 ");
        }

    }

    private void determineleadLikeness() {

        this.leadLikeness = new ArrayList<>();

        if (Double.parseDouble(weight) > 300) {
            this.leadLikeness.add("MW > 300");
        }
        if (Integer.parseInt(rotatableBondsCount) > 3) {
            this.leadLikeness.add("Rotatable Bonds > 3");
        }
        if (Integer.parseInt(hBondDonorCount) > 3) {
            this.leadLikeness.add("H-Bond Donors > 3");
        }
        if (Integer.parseInt(hBondAcceptorCount) > 3) {
            this.leadLikeness.add("H-Bond Acceptors > 3");
        }
        if (Double.parseDouble(logP) > 3) {
            this.leadLikeness.add("LogP > 3");
        }
    }

    private void printInfo() {

        System.out.println(" -----------------------" + name + " -----------------------");
        System.out.println("smiles : " + smiles);
        System.out.println("Total Polar Surface Area : " + tpsa);
        System.out.println("weight : " + weight);
        System.out.println("XLogP : " + xlogP);
        System.out.println("LogP : " + logP);
        System.out.println("AlogP : " + aLogP);
        System.out.println("LogS : " + logS);
        System.out.println("LogS (for polar compounds) : " + logSp);
        System.out.println("Number of Atoms : " + atomCount);
        System.out.println("H-Bond Acceptors : " + hBondAcceptorCount);
        System.out.println("H-Bond Donors : " + hBondDonorCount);
        System.out.println("Number of Rotatable Bonds : " + rotatableBondsCount);
        System.out.println("No of Aromatic Atoms : " + aromaticAtomCount);
        //System.out.println("Rule of Five Violations : " + ruleOfFive);

        determineRuleOfFive();
        System.out.println("Rule of Five Violations : " + this.ruleOf5.size());
        for (String s : this.ruleOf5) {
            System.out.println(s);
        }

        determineGhoseVeber();
        System.out.println("Ghose-Veber Violations : " + this.ghoseVeber.size());
        for (String s : this.ghoseVeber) {
            System.out.println(s);
        }

        determineleadLikeness();
        System.out.println("Leadlike violations : " + this.leadLikeness.size());
        for (String s : this.leadLikeness) {
            System.out.println(s);
        }

        /*System.out.println("stdinchi : " + stdinchi);
        System.out.println("stdinchikey : " + stdinchikey);
        System.out.println("inchi : " + inchi);
        System.out.println("cml : " + cml);*/

        System.out.println(" -----------------------" + name + " -----------------------");
    }

    public Molecule(String molecule) {

        //FIXME this must work
        //this.name = "N\\A";
        this.name = molecule;
        parse(molecule);

        if (this.smiles != null) {

            try {
                this.generateInfoFromSmiles();
                this.printInfo();
            } catch (CDKException e) {
                e.printStackTrace();
            }

        }
    }

    private void parse(String data) {

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

    private void generateInfoFromSmiles() throws CDKException {

        this.stdinchi = nti.parseToStdInchi(this.smiles);
        this.stdinchikey = nti.parseToStdInchiKey(this.smiles);
        this.inchi = nti.parseToInchi(this.smiles);

        this.atomContainer = smilesParser.parseSmiles(this.smiles);
        //TODO truncate string directly
        this.weight = String.format("%.3f", Float.valueOf(weightDescriptor.calculate(atomContainer).getValue().toString()));
        this.xlogP = xlogPDescritpor.calculate(atomContainer).getValue().toString();
        this.logP = logPDescriptor.calculate(atomContainer).getValue().toString();
        this.aLogP = aLogPDescriptor.calculate(atomContainer).getValue();
        this.tpsa = tpsaDescriptor.calculate(atomContainer).getValue().toString();
        this.atomCount = atomCountDescriptor.calculate(atomContainer).getValue().toString();
        this.hBondAcceptorCount = hBondAcceptorDescriptor.calculate(atomContainer).getValue().toString();
        this.hBondDonorCount = hBondDonorDescriptor.calculate(atomContainer).getValue().toString();
        this.rotatableBondsCount = rotatableBondsCountDescriptor.calculate(atomContainer).getValue().toString();
        //this.ruleOfFive = ruleOfFiveDescriptor.calculate(atomContainer).getValue().toString();
        this.aromaticAtomCount = aromaticAtomCountDescriptor.calculate(atomContainer).getValue().toString();
        DecimalFormat df = new DecimalFormat("#.##");
        this.logS = Double.parseDouble(df.format((-1.0377 * Double.parseDouble(xlogP)) - (0.0210 * Double.parseDouble(this.tpsa)) + 0.4488));
        this.logSp = (-0.7897 * Double.parseDouble(xlogP)) - 1.3674;


    }

}
