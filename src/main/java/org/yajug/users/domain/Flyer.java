package org.yajug.users.domain;

import java.io.File;
import java.text.SimpleDateFormat;

public class Flyer {

	public final static String TYPE = "png";
	private final static String THUMB_SUFFIX = "small";
	
	private String basePath; 
	private String name;
	private File file;
	private Flyer thumbnail;
	private boolean isThumb;
	
	public Flyer(){
	}
	
	public Flyer(String basePath, Event event){
		this(basePath, "event-" + new SimpleDateFormat("yyyyMMdd").format(event.getDate()), false);
	}
	
	public Flyer(String basePath, String name, boolean isThumb){
		this.basePath = basePath;
		if(!basePath.endsWith(String.valueOf(File.separatorChar))){
			this.basePath += File.separatorChar;
		}
		this.name = name;
		this.file = new File(this.basePath + this.name + "." + TYPE);
		if(!isThumb){
			this.thumbnail = new Flyer(basePath, name + "-" + THUMB_SUFFIX, true);
		}
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Flyer getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Flyer thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public boolean isThumb() {
		return isThumb;
	}
}
