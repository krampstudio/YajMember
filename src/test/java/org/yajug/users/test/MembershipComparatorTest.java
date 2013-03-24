package org.yajug.users.test;

import java.util.Collections;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.yajug.users.domain.Membership;
import org.yajug.users.domain.utils.MembershipCompartor;

import com.google.common.collect.Lists;

import static org.testng.Assert.*;

@Test(groups={"unit"})
public class MembershipComparatorTest {

	@DataProvider(name="membershipsProvider")
	public static Object[][] membershipsProvider(){
		Membership m1 = new Membership("1", 2009);
		Membership m2 = new Membership("2", 2010);
		Membership m3 = new Membership("3", 2011);
		Membership m4 = new Membership("4", 2012);
		Membership m5 = new Membership("5", 2013);
		
		return new Object[][]{
			{Lists.newArrayList(m2, m1, m5, m4, m3) , Lists.newArrayList(m1, m2, m3, m4, m5)},
			{Lists.newArrayList(m3, m5, m2, m4, m1) , Lists.newArrayList(m1, m2, m3, m4, m5)}
		};
	}
	
	@Test(dataProvider="membershipsProvider")
	public void testMembershipComparator(List<Membership> unordered, List<Membership> expected){
		
		assertNotEquals(unordered, expected);
		
		Collections.sort(unordered, new MembershipCompartor());
		
		assertEquals(unordered, expected);
	}
}
