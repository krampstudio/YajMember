package org.yajug.users.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yajug.users.domain.Event;
import org.yajug.users.service.DataException;
import org.yajug.users.service.EventService;
import org.yajug.users.service.EventServiceImpl;

@RunWith(Parameterized.class)
public class EventTest {

	private EventService service;
	
	private Event event;
	
	public EventTest(Event event) {
		this.event = event;
	}
	
	@Parameters
 	public static Collection<Object[]> data() {
 		return Arrays.asList(new Object[][] {
 			{ new Event("Scala", "Play2", new Date(), null) },
 			{ new Event("Stress tests", "JMeter and Gatling", new Date(), null) },
 		});
 	}
	
	@Before
	public void setUpService(){
		if(service == null){
			service = new EventServiceImpl();
		}
		assertNotNull(service);
	}
	
	@Test
	public void testSave() {
		
		try {
			assertNotNull(this.event);
			service.save(this.event);
			
		} catch (DataException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

}
