package org.yajug.users.domain.utils;

import java.lang.reflect.Field;
import java.util.Comparator;

import org.yajug.users.domain.DomainObject;
import org.yajug.users.domain.Member;

public class MemberComparator implements Comparator<Member>{

	private Field field;
	
	public MemberComparator(String fieldName) {
		try {
			if("key".equals(fieldName)){
				this.field = DomainObject.class.getDeclaredField(fieldName);
			} else {
				this.field = Member.class.getDeclaredField(fieldName);
			}
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int compare(Member o1, Member o2) {
		int comparison = 0;
		if(o1 != null && o2 != null){
			switch(field.getName()){
				case "company":	
					if(o1.getCompany() == null){
						comparison = (o2.getCompany() != null) ?  -1 : 1;
					} else {
						comparison = o1.getCompany().compareTo(o2.getCompany());
					}
					break;
				case "email":
					if(o1.getEmail() == null){
						comparison = (o2.getEmail() != null) ?  -1 : 1;
					} else {
						comparison = o1.getEmail().compareTo(o2.getEmail());
					}
					break;
				case "firstName":
					if(o1.getFirstName() == null){
						comparison = (o2.getFirstName() != null) ?  -1 : 1;
					} else {
						comparison = o1.getFirstName().compareTo(o2.getFirstName());
					}
					break;
				case "lastName":
					if(o1.getLastName() == null){
						comparison = (o2.getLastName() != null) ?  -1 : 1;
					} else {
						comparison = o1.getLastName().compareTo(o2.getLastName());
					}
					break;
				case "key":
				default:
					comparison = o1.compareTo(o2);
					break;
			}
		}
		return comparison;
	}

}
