package org.openscience.jchempaint.inchi;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

public class StdInChIGeneratorTest {
    
    protected StdInChIGenerator gen = new StdInChIGenerator();

    
    /**
     * Tests element name is correctly passed to InChI.
     * 
     * @throws Exception
     */
    @Test public void testGetInchiFromChlorineAtom() throws Exception {
        IAtomContainer ac = new AtomContainer();
        ac.addAtom(new Atom("Cl"));
        gen.setHydrogensNotAdded(true);
        Assert.assertEquals(gen.generateInchi(ac).getInChI(), "InChI=1S/Cl");
        gen.setHydrogensNotAdded(false);

    }
    
    /**
     * Tests charge is correctly passed to InChI.
     * 
     * @throws Exception
     */
    @Test public void testGetInchiFromLithiumIon() throws Exception {
        IAtomContainer ac = new AtomContainer();
        IAtom a = new Atom("Li");
        a.setFormalCharge(+1);
        ac.addAtom(a);
        gen.setHydrogensNotAdded(true);
        Assert.assertEquals(gen.generateInchi(ac).getInChI(), "InChI=1S/Li/q+1");
        gen.setHydrogensNotAdded(false);

    }
    
    /**
    * Tests isotopic mass is correctly passed to InChI.
    * 
    * @throws Exception
    */
    @Test public void testGetInchiFromChlorine37Atom() throws Exception {
        IAtomContainer ac = new AtomContainer();
        IAtom a = new Atom("Cl");
        a.setMassNumber(37);
        ac.addAtom(a);
        gen.setHydrogensNotAdded(true);
        Assert.assertEquals(gen.generateInchi(ac).getInChI(), "InChI=1S/Cl/i1+2");
        gen.setHydrogensNotAdded(false);
    }
    
    /**
     * Tests implicit hydrogen count is correctly passed to InChI.
     * 
     * @throws Exception
     */
    @Test public void testGetInchiFromHydrogenChlorideImplicitH() throws Exception {
        IAtomContainer ac = new AtomContainer();
        IAtom a = new Atom("Cl");
        a.setImplicitHydrogenCount(1);
        ac.addAtom(a);
        Assert.assertEquals(gen.generateInchi(ac).getInChI(), "InChI=1S/ClH/h1H");
    }
    
    /**
     * Tests radical state is correctly passed to InChI.
     * 
     * @throws Exception
     */
    //@Test public void testGetInchiFromMethylRadical() throws Exception {
    //    IAtomContainer ac = new AtomContainer();
    //    IAtom a = new Atom("C");
    //    a.setImplicitHydrogenCount(3);
    //    ac.addAtom(a);
    //    ac.addSingleElectron(new SingleElectron(a));
    //    Assert.assertEquals(gen.generateInchi(ac).getInChI(), "InChI=1S/CH3/h1H3");
    //
    //}
    
    /**
     * Tests single bond is correctly passed to InChI.
     * 
     * @throws Exception
     */
    @Test public void testGetInchiFromEthane() throws Exception {
        IAtomContainer ac = new AtomContainer();
        IAtom a1 = new Atom("C");
        IAtom a2 = new Atom("C");
        a1.setImplicitHydrogenCount(3);
        a2.setImplicitHydrogenCount(3);
        ac.addAtom(a1);
        ac.addAtom(a2);
        ac.addBond(new Bond(a1, a2, IBond.Order.SINGLE));
        InChI inchi = gen.generateInchi(ac);
        Assert.assertEquals(inchi.getInChI(), "InChI=1S/C2H6/c1-2/h1-2H3");
        Assert.assertEquals("InChIKey=OTMSDBZUPAUEDD-UHFFFAOYSA-N", inchi.getKey());
    }
    
    /**
     * Tests double bond is correctly passed to InChI.
     * 
     * @throws Exception
     */
    @Test public void testGetInchiFromEthene() throws Exception {
        IAtomContainer ac = new AtomContainer();
        IAtom a1 = new Atom("C");
        IAtom a2 = new Atom("C");
        a1.setImplicitHydrogenCount(2);
        a2.setImplicitHydrogenCount(2);
        ac.addAtom(a1);
        ac.addAtom(a2);
        ac.addBond(new Bond(a1, a2, IBond.Order.DOUBLE));
        Assert.assertEquals(gen.generateInchi(ac).getInChI(), "InChI=1S/C2H4/c1-2/h1-2H2");
    }
    
    /**
     * Tests triple bond is correctly passed to InChI.
     * 
     * @throws Exception
     */
    @Test public void testGetInchiFromEthyne() throws Exception {
        IAtomContainer ac = new AtomContainer();
        IAtom a1 = new Atom("C");
        IAtom a2 = new Atom("C");
        a1.setImplicitHydrogenCount(1);
        a2.setImplicitHydrogenCount(1);
        ac.addAtom(a1);
        ac.addAtom(a2);
        ac.addBond(new Bond(a1, a2, IBond.Order.TRIPLE));
        Assert.assertEquals(gen.generateInchi(ac).getInChI(), "InChI=1S/C2H2/c1-2/h1-2H");
    }
    
    /**
     * Tests 2D coordinates are correctly passed to InChI.
     * 
     * @throws Exception
     */
    @Test public void testGetInchiEandZ12Dichloroethene2D() throws Exception {

        // (E)-1,2-dichloroethene
        IAtomContainer acE = new AtomContainer();
        IAtom a1E = new Atom("C", new Point2d(2.866, -0.250));
        IAtom a2E = new Atom("C", new Point2d(3.732, 0.250));
        IAtom a3E = new Atom("Cl", new Point2d(2.000, 2.500));
        IAtom a4E = new Atom("Cl", new Point2d(4.598, -0.250));
        a1E.setImplicitHydrogenCount(1);
        a2E.setImplicitHydrogenCount(1);
        acE.addAtom(a1E);
        acE.addAtom(a2E);
        acE.addAtom(a3E);
        acE.addAtom(a4E);

        acE.addBond(new Bond(a1E, a2E, IBond.Order.DOUBLE));
        acE.addBond(new Bond(a1E, a3E, IBond.Order.SINGLE));
        acE.addBond(new Bond(a2E, a4E, IBond.Order.SINGLE));
        
        Assert.assertEquals(gen.generateInchi(acE).getInChI(), "InChI=1S/C2H2Cl2/c3-1-2-4/h1-2H/b2-1+");
        
        // (Z)-1,2-dichloroethene
        IAtomContainer acZ = new AtomContainer();
        IAtom a1Z = new Atom("C", new Point2d(2.866, -0.440));
        IAtom a2Z = new Atom("C", new Point2d(3.732, 0.060));
        IAtom a3Z = new Atom("Cl", new Point2d(2.000, 0.060));
        IAtom a4Z = new Atom("Cl", new Point2d(3.732, 1.060));
        a1Z.setImplicitHydrogenCount(1);
        a2Z.setImplicitHydrogenCount(1);
        acZ.addAtom(a1Z);
        acZ.addAtom(a2Z);
        acZ.addAtom(a3Z);
        acZ.addAtom(a4Z);

        acZ.addBond(new Bond(a1Z, a2Z, IBond.Order.DOUBLE));
        acZ.addBond(new Bond(a1Z, a3Z, IBond.Order.SINGLE));
        acZ.addBond(new Bond(a2Z, a4Z, IBond.Order.SINGLE));
        
        Assert.assertEquals(gen.generateInchi(acZ).getInChI(), "InChI=1S/C2H2Cl2/c3-1-2-4/h1-2H/b2-1-");

    }
    
    
    /**
     * Tests 3D coordinates are correctly passed to InChI.
     * 
     * @throws Exception
     */
    @Test
    public void testGetInchiFromLandDAlanine3D() throws Exception {
        
        // L-Alanine
        IAtomContainer acL = new AtomContainer();
        IAtom a1L = new Atom("C", new Point3d(-0.358, 0.819, 20.655));
        IAtom a2L = new Atom("C", new Point3d(-1.598, -0.032, 20.905));
        IAtom a3L = new Atom("N", new Point3d(-0.275, 2.014, 21.574));
        IAtom a4L = new Atom("C", new Point3d(0.952, 0.043, 20.838));
        IAtom a5L = new Atom("O", new Point3d(-2.678, 0.479, 21.093));
        IAtom a6L = new Atom("O", new Point3d(-1.596, -1.239, 20.958));
        a1L.setImplicitHydrogenCount(1);
        a3L.setImplicitHydrogenCount(2);
        a4L.setImplicitHydrogenCount(3);
        a5L.setImplicitHydrogenCount(1);
        acL.addAtom(a1L);
        acL.addAtom(a2L);
        acL.addAtom(a3L);
        acL.addAtom(a4L);
        acL.addAtom(a5L);
        acL.addAtom(a6L);
        
        acL.addBond(new Bond(a1L, a2L, IBond.Order.SINGLE));
        acL.addBond(new Bond(a1L, a3L, IBond.Order.SINGLE));
        acL.addBond(new Bond(a1L, a4L, IBond.Order.SINGLE));
        acL.addBond(new Bond(a2L, a5L, IBond.Order.SINGLE));
        acL.addBond(new Bond(a2L, a6L, IBond.Order.DOUBLE));
        
        Assert.assertEquals(gen.generateInchi(acL).getInChI(), "InChI=1S/C3H7NO2/c1-2(4)3(5)6/h2H,4H2,1H3,(H,5,6)/t2-/m0/s1");
        
        
        // D-Alanine
        IAtomContainer acD = new AtomContainer();
        IAtom a1D = new Atom("C", new Point3d(0.358, 0.819, 20.655));
        IAtom a2D = new Atom("C", new Point3d(1.598, -0.032, 20.905));
        IAtom a3D = new Atom("N", new Point3d(0.275, 2.014, 21.574));
        IAtom a4D = new Atom("C", new Point3d(-0.952, 0.043, 20.838));
        IAtom a5D = new Atom("O", new Point3d(2.678, 0.479, 21.093));
        IAtom a6D = new Atom("O", new Point3d(1.596, -1.239, 20.958));
        a1D.setImplicitHydrogenCount(1);
        a3D.setImplicitHydrogenCount(2);
        a4D.setImplicitHydrogenCount(3);
        a5D.setImplicitHydrogenCount(1);
        acD.addAtom(a1D);
        acD.addAtom(a2D);
        acD.addAtom(a3D);
        acD.addAtom(a4D);
        acD.addAtom(a5D);
        acD.addAtom(a6D);
        
        acD.addBond(new Bond(a1D, a2D, IBond.Order.SINGLE));
        acD.addBond(new Bond(a1D, a3D, IBond.Order.SINGLE));
        acD.addBond(new Bond(a1D, a4D, IBond.Order.SINGLE));
        acD.addBond(new Bond(a2D, a5D, IBond.Order.SINGLE));
        acD.addBond(new Bond(a2D, a6D, IBond.Order.DOUBLE));
        
                                                             //"InChI=1S/C3H7NO2/c1-2(4)3(5)6/h2H,4H2,1H3,(H,5,6)/t2-/m1/s1");
        Assert.assertEquals(gen.generateInchi(acL).getInChI(), "InChI=1S/C3H7NO2/c1-2(4)3(5)6/h2H,4H2,1H3,(H,5,6)/t2-/m0/s1");

    }
}