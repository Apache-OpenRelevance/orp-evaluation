package org.orp.eval.trec;


public class Topic {

	private int id;
	private String title;
	private String desc;
	private String narr;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return desc;
	}
	public void setDescription(String desc) {
		this.desc = desc;
	}
	public String getNarrative() {
		return narr;
	}
	public void setNarrative(String narr) {
		this.narr = narr;
	}
}