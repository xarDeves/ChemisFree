package Core;

import Helpers.SmileNameConverter;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.descriptors.molecular.*;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import uk.ac.cam.ch.wwmm.opsin.NameToInchi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

//TODO if no info could be retrieved, implement not found mechanism
public final class Molecule {

    NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
    private static final SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
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

    private IAtomContainer atomContainer;
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

    public Molecule(String molNameOrSmile) {
        SmileNameConverter converter = new SmileNameConverter(molNameOrSmile);
        this.name = converter.getMolName();
        this.smiles = converter.getMolSmiles();

        if (this.smiles != null) {
            try {
                this.generateInfoFromSmiles();
                this.printInfo();
            } catch (CDKException | ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private void determineRuleOfFive() throws ParseException {
        this.ruleOf5 = new ArrayList<>();

        if (format.parse(weight).doubleValue() > 500) {
            this.ruleOf5.add("Weight > 500");
        }
        if (Integer.parseInt(hBondAcceptorCount) > 10) {
            this.ruleOf5.add("Acceptors > 10");
        }
        if (Integer.parseInt(hBondDonorCount) > 5) {
            this.ruleOf5.add("Donors > 5");
        }
        if (format.parse(logP).doubleValue() > 5) {
            this.ruleOf5.add("LogP > 5");
        }
    }

    private void determineGhoseVeber() throws ParseException {
        this.ghoseVeber = new ArrayList<>();

        if (format.parse(weight).doubleValue() > 480.0) {
            this.ghoseVeber.add("Weight > 480 ");
        }
        if (format.parse(weight).doubleValue() < 180.0) {
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
        if (format.parse(logP).doubleValue() > 5.6) {
            System.out.print("LogP > 5.6 ");
        }
        if (format.parse(logP).doubleValue() < -0.4) {
            this.ghoseVeber.add("LogP < -0.4 ");
        }
        if (format.parse(tpsa).doubleValue() > 140.0) {
            this.ghoseVeber.add("TPSA > 140 ");
        }
    }

    private void determineLeadLikeness() throws ParseException {
        this.leadLikeness = new ArrayList<>();

        if (format.parse(weight).doubleValue() > 300) {
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
        if (format.parse(logP).doubleValue() > 3) {
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

        System.out.println("Rule of Five Violations : " + this.ruleOf5.size());
        for (String s : this.ruleOf5) {
            System.out.println(s);
        }
        System.out.println("Ghose-Veber Violations : " + this.ghoseVeber.size());
        for (String s : this.ghoseVeber) {
            System.out.println(s);
        }
        System.out.println("Leadlike violations : " + this.leadLikeness.size());
        for (String s : this.leadLikeness) {
            System.out.println(s);
        }

        //System.out.println("Rule of Five Violations : " + ruleOfFive);

        /*System.out.println("stdinchi : " + stdinchi);
        System.out.println("stdinchikey : " + stdinchikey);
        System.out.println("inchi : " + inchi);
        System.out.println("cml : " + cml);*/

        System.out.println(" -----------------------" + name + " -----------------------");
    }

    public static double calculateLogS(double xlogP, double tpsa) {
        // Define constants for the coefficients
        final double COEFFICIENT_XLOGP = -1.0377;
        final double COEFFICIENT_TPSA = -0.0210;
        final double CONSTANT_TERM = 0.4488;

        // Calculate the result using the coefficients and constants
        double result = COEFFICIENT_XLOGP * xlogP + COEFFICIENT_TPSA * tpsa + CONSTANT_TERM;

        // Round the result to the desired precision
        double roundedResult = Math.round(result * 10000.0) / 10000.0;

        return roundedResult;
    }

    private void generateInfoFromSmiles() throws CDKException, ParseException {
        this.stdinchi = nti.parseToStdInchi(this.smiles);
        this.stdinchikey = nti.parseToStdInchiKey(this.smiles);
        this.inchi = nti.parseToInchi(this.smiles);

        this.atomContainer = smilesParser.parseSmiles(this.smiles);
        //TODO truncate string directly
        DecimalFormat df = new DecimalFormat("#.##");
        this.weight = String.format("%.3f", Float.valueOf(weightDescriptor.calculate(atomContainer).getValue().toString()));
        this.xlogP = xlogPDescritpor.calculate(atomContainer).getValue().toString();
        this.logP = String.format("%.3f", Float.valueOf((logPDescriptor.calculate(atomContainer).getValue().toString())));
        this.aLogP = aLogPDescriptor.calculate(atomContainer).getValue();
        this.tpsa = tpsaDescriptor.calculate(atomContainer).getValue().toString();
        this.atomCount = atomCountDescriptor.calculate(atomContainer).getValue().toString();
        this.hBondAcceptorCount = hBondAcceptorDescriptor.calculate(atomContainer).getValue().toString();
        this.hBondDonorCount = hBondDonorDescriptor.calculate(atomContainer).getValue().toString();
        this.rotatableBondsCount = rotatableBondsCountDescriptor.calculate(atomContainer).getValue().toString();
        //this.ruleOfFive = ruleOfFiveDescriptor.calculate(atomContainer).getValue().toString();
        this.aromaticAtomCount = aromaticAtomCountDescriptor.calculate(atomContainer).getValue().toString();
        this.logS = calculateLogS(format.parse(xlogP).doubleValue(), format.parse(tpsa).doubleValue());
        this.logSp = (-0.7897 * format.parse(xlogP).doubleValue()) - 1.3674;

        determineRuleOfFive();
        determineGhoseVeber();
        determineLeadLikeness();
    }
}
