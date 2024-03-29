/* $Revision$ $Author$ $Date$
*
*  Copyright (C) 2008 Gilleain Torrance <gilleain.torrance@gmail.com>
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

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * @cdk.module renderawt
 */
public abstract class AbstractAWTDrawVisitor implements IDrawVisitor {
	
	/**
	 * This is initially null, and must be set in the setTransform method!
	 */
	protected AffineTransform transform = null;
	protected FontMetrics fm;
	
	public int[] transformPoint(double x, double y) {
        double[] src = new double[] {x, y};
        double[] dest = new double[2];
        this.transform.transform(src, 0, dest, 0, 1);
        return new int[] { (int) dest[0], (int) dest[1] };
    }
    
    public double[] transformPointD(double x, double y) {
        double[] src = new double[] {x, y};
        double[] dest = new double[2];
        this.transform.transform(src, 0, dest, 0, 1);
        return new double[] { dest[0], dest[1] };
    }

    protected Rectangle2D getTextBounds(String text, double x, double y,
            Graphics2D g, double zoomfactor) {
        //it seems the font metrics do not take font size into account
        //the zoom for text is limited (see AWTFontManager)
        if(zoomfactor<0.5)
            zoomfactor=.5;
        if(zoomfactor>2.5)
            zoomfactor=2.5;
        Rectangle2D bounds = fm.getStringBounds(text, g);
        
        double widthPad = 3;
        double heightPad = 1;
        
        double w = bounds.getWidth()*zoomfactor + widthPad;
        double h = bounds.getHeight()*zoomfactor + heightPad;
        int[] p = this.transformPoint(x, y);
        return new Rectangle2D.Double(p[0] - w / 2, p[1] - h / 2, w, h);
    }
    
    protected Point getTextBasePoint(String text, double x, double y, 
            Graphics2D g) {
        Rectangle2D stringBounds = fm.getStringBounds(text, g);
        int[] p = this.transformPoint(x, y);
        int baseX = (int) (p[0] - (stringBounds.getWidth() / 2));
        
        // correct the baseline by the ascent
        int baseY = (int) (p[1] + 
                (fm.getAscent() - stringBounds.getHeight() / 2));
        return new Point(baseX, baseY);
    }
    
    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }
}
