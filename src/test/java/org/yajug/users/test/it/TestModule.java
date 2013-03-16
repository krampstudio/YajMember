package org.yajug.users.test.it;

import org.yajug.users.config.ModuleHelper;

import com.google.inject.Binder;
import com.google.inject.Module;

public class TestModule implements Module {

	@Override
	public void configure(Binder binder) {
		ModuleHelper.bindProperties(binder);
		ModuleHelper.bindApis(binder);
	}
}
