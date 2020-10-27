package com.cqi.hr.vo;

import java.util.List;

public class WeatherLocation {
	private String locationName;
	private List<WeatherType> weatherTypeList;
	
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public List<WeatherType> getWeatherTypeList() {
		return weatherTypeList;
	}
	public void setWeatherTypeList(List<WeatherType> weatherTypeList) {
		this.weatherTypeList = weatherTypeList;
	}

	
}
