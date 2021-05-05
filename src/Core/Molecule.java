package Core;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.descriptors.molecular.*;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import uk.ac.cam.ch.wwmm.opsin.NameToInchi;
import uk.ac.cam.ch.wwmm.opsin.NameToStructure;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

//TODO if no info could be retrieved, implement not found mechanism
public final class Molecule {

    private static final SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
    private static final NameToStructure nts = NameToStructure.getInstance();
    private static final WeightDescriptor weightDescriptor = new WeightDescriptor();
    private static final XLogPDescriptor xlogPDescritpor = new XLogPDescriptor();
    private static final AtomCountDescriptor atomCountDescriptor = new AtomCountDescriptor();
    private static final HBondAcceptorCountDescriptor hBondAcceptorDescriptor = new HBondAcceptorCountDescriptor();
    private static final HBondDonorCountDescriptor hBondDonorDescriptor = new HBondDonorCountDescriptor();
    private static final RotatableBondsCountDescriptor rotatableBondsCountDescriptor = new RotatableBondsCountDescriptor();
    private static final RuleOfFiveDescriptor ruleOfFiveDescriptor = new RuleOfFiveDescriptor();
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
    public String ruleOfFive;
    public String stdinchi;
    public String stdinchikey;
    public String inchi;
    public IDescriptorResult aLogP;
    public Double logS;
    public Double logSp;
    public BufferedImage molImage;


    private short foo1() {

        //short[] LipinskiLimits = {500, 10, 5, 5, 10};
        short violationsCount = 0;

        if (Double.parseDouble(weight) > 500) {
            System.out.print("Weight > 500, ");
            violationsCount++;
        }
        if (Integer.parseInt(hBondAcceptorCount) > 10) {
            System.out.print("Acceptors > 10, ");
            violationsCount++;
        }
        if (Integer.parseInt(hBondDonorCount) > 5) {
            System.out.print("Donors > 5, ");
            violationsCount++;
        }
        if (Double.parseDouble(logP) > 5) {
            System.out.print("LogP > 5, ");
            violationsCount++;
        }

        return violationsCount;
    }

    private short foo2() {

        Double[] GhoseVeberLimits = {180.0, 480.0, 20.0, 70.0, 10.0, -0.4, 5.6, 140.0};
        short violations2Count = 0;

        if (Double.parseDouble(weight) < GhoseVeberLimits[0] || Double.parseDouble(weight) > GhoseVeberLimits[1]) {
            System.out.print("Weight > 480 or < 180, ");
            violations2Count++;
        }
        if (Integer.parseInt(atomCount) < GhoseVeberLimits[2] || Integer.parseInt(atomCount) > GhoseVeberLimits[3]) {
            System.out.print("Number of atoms > 70 or < 20, ");
            violations2Count++;
        }
        if (Integer.parseInt(rotatableBondsCount) > GhoseVeberLimits[4]) {
            System.out.print("Number of Rotatable bonds > 10, ");
            violations2Count++;
        }
        if (Double.parseDouble(logP) < GhoseVeberLimits[5] || Double.parseDouble(logP) > GhoseVeberLimits[6]) {
            System.out.print("LogP > 5.6 or < (-0.4), ");
            violations2Count++;
        }
        if (Double.parseDouble(tpsa) > GhoseVeberLimits[7]) {
            System.out.println("TPSA > 140");
            violations2Count++;
        }

        return violations2Count;
    }

    private short foo3() {

        short violationsLead = 0;
        if (Double.parseDouble(weight) > 300) {
            System.out.println("MW > 300");
            violationsLead++;
        }
        if (Integer.parseInt(rotatableBondsCount) > 3) {
            System.out.println("Rotatable Bonds > 3");
            violationsLead++;
        }
        if (Integer.parseInt(hBondDonorCount) > 3) {
            System.out.println("H-Bond Donors > 3");
            violationsLead++;
        }
        if (Integer.parseInt(hBondAcceptorCount) > 3) {
            System.out.println("H-Bond Acceptors > 3");
            violationsLead++;
        }
        if (Double.parseDouble(logP) > 3) {
            System.out.println("LogP > 3");
            violationsLead++;
        }

        return violationsLead;
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

        System.out.println("Rule of Five Violations :  " + foo1());
        System.out.println("Ghose-Veber Violations : " + foo2());
        System.out.println("Leadlike violations : " + foo3());

        /*System.out.println("stdinchi : " + stdinchi);
        System.out.println("stdinchikey : " + stdinchikey);
        System.out.println("inchi : " + inchi);
        System.out.println("cml : " + cml);*/

        System.out.println(" -----------------------" + name + " -----------------------");
    }

    public Molecule(String molecule) {

        this.name = molecule;
        parse(molecule);

        if (smiles != null) {

            try {
                this.makeImageFromSmiles(this.smiles, this.name);
                this.generateInfoFromSmiles();
                //this.printInfo();
            } catch (CDKException e) {
                e.printStackTrace();
            }

        }
    }

    //TODO add molecular weight
    private void makeImageFromSmiles(String smiles, String molecule) throws CDKException {

        IAtomContainer mol = smilesParser.parseSmiles(smiles);
        mol.setProperty(CDKConstants.TITLE, this.weight);

        DepictionGenerator depictionGenerator = new DepictionGenerator();
        // size in px (raster) or mm (vector)
        // annotations are red by default
        depictionGenerator.withAtomColors();
        depictionGenerator.withMolTitle();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            depictionGenerator.depict(mol).writeTo("png", out);
            byte[] data = out.toByteArray();
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            molImage = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
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

        IAtomContainer atomContainer = smilesParser.parseSmiles(this.smiles);
        this.weight = weightDescriptor.calculate(atomContainer).getValue().toString();
        this.xlogP = xlogPDescritpor.calculate(atomContainer).getValue().toString();
        this.logP = logPDescriptor.calculate(atomContainer).getValue().toString();
        this.aLogP = aLogPDescriptor.calculate(atomContainer).getValue();
        this.tpsa = tpsaDescriptor.calculate(atomContainer).getValue().toString();
        this.atomCount = atomCountDescriptor.calculate(atomContainer).getValue().toString();
        this.hBondAcceptorCount = hBondAcceptorDescriptor.calculate(atomContainer).getValue().toString();
        this.hBondDonorCount = hBondDonorDescriptor.calculate(atomContainer).getValue().toString();
        this.rotatableBondsCount = rotatableBondsCountDescriptor.calculate(atomContainer).getValue().toString();
        this.ruleOfFive = ruleOfFiveDescriptor.calculate(atomContainer).getValue().toString();
        this.aromaticAtomCount = aromaticAtomCountDescriptor.calculate(atomContainer).getValue().toString();
        this.logS = (-1.0377 * 12.19) - (0.0210 * Double.parseDouble(this.tpsa)) + 0.4488;
        this.logSp = (-0.7897 * 12.19) - 1.3674;
    }

}
