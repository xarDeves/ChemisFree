/* $Revision$ $Author$ $Date$
 *
 *  Copyright (C) 2008  The Chemistry Development Kit (CDK) project
 *
 *  Contact: cdk-devel@list.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscience.jchempaint.renderer.visitor;

import org.openscience.cdk.renderer.elements.*;
import org.openscience.jchempaint.renderer.elements.TextElement;

import java.awt.geom.AffineTransform;


/**
 * @cdk.module renderextra
 */
public class PrintVisitor implements IRenderingVisitor {
	
	private int depth; 
	
	public PrintVisitor() {
		this.depth = 0;
	}

	public void visitElementGroup(ElementGroup elementGroup) {
		this.depth += 1;
		System.out.println("Group at depth " + depth);
		elementGroup.visitChildren(this);
		this.depth -= 1;
	}

	public void visitLine(LineElement lineElement) {
		System.out.println("Line [" 
		        + lineElement.firstPointX 
		        + " " 
		        + lineElement.firstPointY 
		        + "]-["
				+ lineElement.firstPointX 
				+ " " 
				+ lineElement.firstPointY
				+"]");
	}

	public void visitOval(OvalElement ovalElement) {
		System.out.println("Oval ["
		        + ovalElement.xCoord 
		        + ","         
		        + ovalElement.yCoord 
		        + " " 
		        + ovalElement.radius
		        + "]");
	}

	public void visitText(TextElement textElement) {
		System.out.println("Text " + textElement.text);
	}

	public void visitWedge(WedgeLineElement wedgeElement) {
		System.out.println("Wedge");
	}

	public void visit(IRenderingElement element) {
        if (element instanceof ElementGroup) {
           visitElementGroup((ElementGroup) element);
        } else if (element instanceof LineElement) {
            visitLine((LineElement) element);
        } else if (element instanceof OvalElement) {
            visitOval((OvalElement) element);
        } else if (element instanceof TextElement) {
            visitText((TextElement) element);
        } else {
            System.err.println("Visitor method for "
                    + element.getClass().getName() + " is not implemented");
        }
    }
	
	public void setTransform( AffineTransform transform ) {

        // TODO Auto-generated method stub
        
    }
}
