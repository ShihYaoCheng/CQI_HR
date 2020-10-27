package com.cqi.hr.vo;

import java.util.List;

public class WeatherType {
	private String weatherType;
	private List<WeatherTime> weatherTimeList;
	
	public String getWeatherType() {
		return weatherType;
	}
	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}
	public List<WeatherTime> getWeatherTimeList() {
		return weatherTimeList;
	}
	public void setWeatherTimeList(List<WeatherTime> weatherTimeList) {
		this.weatherTimeList = weatherTimeList;
	}
	
	
}
