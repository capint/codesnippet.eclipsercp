package com.rcpquickstart.cnftutorial.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.navigator.CommonNavigator;

import com.rcpquickstart.cnftutorial.model.NavigatorRoot;


public class TutorialNavigator extends CommonNavigator {

	@Override
	protected IAdaptable getInitialInput() {
		this.getCommonViewer().refresh();
		return new NavigatorRoot();
	}
}
