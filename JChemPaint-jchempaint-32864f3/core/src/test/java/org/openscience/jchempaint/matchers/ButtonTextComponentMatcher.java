package org.openscience.jchempaint.matchers;

import org.fest.swing.core.GenericTypeMatcher;

import javax.swing.*;

public class ButtonTextComponentMatcher extends GenericTypeMatcher<JButton> {

	private String text;
	
	public ButtonTextComponentMatcher(String text){
		super(JButton.class);
		this.text=text;
	}
	@Override
	protected boolean isMatching(JButton arg0) {
		if(arg0.getText()!=null && arg0.isShowing() && arg0.getText().equals(text))
			return true;
		else
			return false;
	}

}
