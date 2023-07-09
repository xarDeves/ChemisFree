/* Copyright (C) 2009  Stefan Kuhn <shk3@users.sf.net>
 *
 * Contact: cdk-jchempaint@list.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
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
package org.openscience.jchempaint.renderer.generators;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.jchempaint.renderer.JChemPaintRendererModel;
import org.openscience.jchempaint.renderer.elements.TextGroupElement;

import javax.vecmath.Point2d;
import java.awt.*;

/**
 * A generator for tooltips shown when hovering over an atom
 * 
 */
public class TooltipGenerator extends BasicAtomGenerator {
    
    @Override
    public IRenderingElement generate(
            IAtomContainer ac, IAtom atom, RendererModel model) {
        
        JChemPaintRendererModel jcpModel = (JChemPaintRendererModel) model;
        
        if (jcpModel.getShowTooltip() && (atom == model.getHighlightedAtom() ||( model.getExternalSelectedPart()!=null &&  model.getExternalSelectedPart().contains(atom))) && model.getToolTipText(atom) != null)
        {
            String text = model.getToolTipText(atom);
            String[] result = text.split("\\n");
            Point2d p = atom.getPoint2d();
            Color c = Color.black;
            TextGroupElement textGroup;
            if(result.length>1){
                textGroup = new TextGroupElement(p.x, p.y, result[1], c, Color.yellow);
                textGroup.addChild(result[0], TextGroupElement.Position.N);
            }else{
                textGroup = new TextGroupElement(p.x, p.y, result[0], c, Color.yellow);
            }
            if(result.length>2)
                textGroup.addChild(result[0], TextGroupElement.Position.S);
            return textGroup;
        }else{
            return null;
        }
    }    
}
