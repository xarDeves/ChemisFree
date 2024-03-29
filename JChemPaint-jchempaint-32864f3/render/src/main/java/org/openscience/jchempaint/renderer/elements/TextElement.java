/* $Revision$ $Author$ $Date$
 *
 *  Copyright (C) 2008  Arvid Berg <goglepox@users.sf.net>
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
package org.openscience.jchempaint.renderer.elements;

import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.elements.IRenderingVisitor;

import java.awt.*;

/**
 * @cdk.module render
 */
public class TextElement implements IRenderingElement {

	public final double x;
	public final double y;
	public final String text;
	public final Color color;
	public final Color backColor;
	public final Double extraZoom;

    public TextElement(double x, double y, String text, Color color, Color backColor) {
        this.backColor = backColor;
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.extraZoom=null;
    }

    public TextElement(double x, double y, String text, Color color) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		this.backColor = null;
        this.extraZoom=null;
    }

    public TextElement(double x, double y, String text, Color color, double extraZoom) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		this.backColor = null;
        this.extraZoom=extraZoom;
    }

	public void accept(IRenderingVisitor v) {
		v.visit(this);
	}

}
