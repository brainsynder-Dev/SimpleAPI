package simple.brainsynder.utils;

import java.util.ArrayList;

public class PageMaker {
    private ArrayList<String> raw = new ArrayList<> ();
	private int perPage;

	public PageMaker (ArrayList< String > raw, int perPage) {
		this.perPage = perPage;
		this.setRaw (raw);
	}

	public void setRaw(ArrayList<String> newRaw) {
		this.raw = newRaw;
	}

	public ArrayList<String> getRaw() {
		return this.raw;
	}

	public int getMaxPages() {
		return (int)Math.ceil((double)this.raw.size() / (double)this.perPage);
	}

	private double getDoubleIndex() {
		return Math.ceil((double)this.raw.size() / (double)this.perPage);
	}

	public String[] getPage(int pageNumber) {
		int index = this.perPage * (Math.abs(pageNumber) - 1);
		ArrayList<String> list = new ArrayList<> ();
		if((double)pageNumber <= this.getDoubleIndex ()) {
			for(int i = index; i < index + this.perPage; ++i) {
				if(this.raw.size() > i) {
					list.add(this.raw.get(i));
				}
			}

			return list.toArray(new String[list.size()]);
		} else {
			return null;
		}
	}
}
