package me.deepak.spy.log;

enum Constants {

	ZIPPED_FILE_NAME("logs.zip");

	private String name;

	private Constants(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}