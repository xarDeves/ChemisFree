/* 
 * Copyright (C) 2009  Mark Rijnbeek <mark_rynbeek@users.sf.net>
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.jchempaint.inchi;

/**
 * Bean to capture InChI information
 * @author markr
 *
 */
public class InChI {

    private String inChI;
    private String auxInfo;
    private String key;

    public String getInChI() {
        return inChI;
    }
    public void setInChI(String inChI) {
        this.inChI = inChI;
    }
    public String getAuxInfo() {
        return auxInfo;
    }
    public void setAuxInfo(String auxInfo) {
        this.auxInfo = auxInfo;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    
    public static InChI create(String str) {
        InChI inchi = new InChI();
        inchi.setInChI(str);
        return inchi;
    }    
}
