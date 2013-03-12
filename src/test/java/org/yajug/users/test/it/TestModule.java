package org.yajug.users.test.it;

import org.yajug.users.config.ModuleHelper;
import org.yajug.users.service.EventService;
import org.yajug.users.service.EventServiceImpl;
import org.yajug.users.service.MemberService;
import org.yajug.users.service.MemberServiceImpl;

import com.google.inject.Binder;
import com.google.inject.Module;

public class TestModule implements Module {

	@Override
	public void configure(Binder binder) {
		ModuleHelper.bindProperties(binder);
		
		binder.bind(MemberService.class).to(MemberServiceImpl.class);
		binder.bind(EventService.class).to(EventServiceImpl.class);
	}

}
