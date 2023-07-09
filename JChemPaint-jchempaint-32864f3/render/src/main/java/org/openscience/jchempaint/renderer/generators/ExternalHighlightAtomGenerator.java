/* $Revision$ $Author$ $Date$
 *
 *  Copyright (C) 2010  Stefan Kuhn <shhk3@users.sf.net>
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
package org.openscience.jchempaint.renderer.generators;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.selection.IChemObjectSelection;
import org.openscience.jchempaint.renderer.JChemPaintRendererModel;
import org.openscience.jchempaint.renderer.selection.AtomContainerSelection;

import java.awt.*;

/**
 * A generator to generate the externally selected atoms in ExternalHighlightColor.
 */
public class ExternalHighlightAtomGenerator extends SelectAtomGenerator {

    public ExternalHighlightAtomGenerator() {}

    @Override
    public IRenderingElement generate(IAtomContainer ac, RendererModel model) {
        JChemPaintRendererModel jcpModel = (JChemPaintRendererModel) model;
        if(model.getExternalSelectedPart()==null)
            return new ElementGroup();
        Color selectionColor = jcpModel.getExternalHighlightColor();
        IChemObjectSelection selection = new AtomContainerSelection(model.getExternalSelectedPart());
        return generate(selection, selectionColor, jcpModel);
    }
}
