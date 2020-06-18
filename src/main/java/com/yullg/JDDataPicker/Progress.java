package com.yullg.JDDataPicker;

public final class Progress {

	private static int index = 0;
	private static int total = 0;

	public static synchronized void addIndex(int i) {
		index += i;
	}

	public static synchronized void addTotal(int i) {
		total += i;
	}

	public static int index() {
		return index;
	}

	public static int total() {
		return total;
	}

	private Progress() {
	}

}