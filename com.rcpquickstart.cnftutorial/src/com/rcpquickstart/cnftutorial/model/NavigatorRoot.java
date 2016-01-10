package com.rcpquickstart.cnftutorial.model;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.PlatformObject;

public class NavigatorRoot extends PlatformObject {

	public List<ParentBean> getParentBeans() {
		List<ParentBean> parents = new LinkedList<ParentBean>();
		parents.add(new ParentBean("Parent 1"));
		parents.add(new ParentBean("Parent 2"));
		return parents;
	}
}
