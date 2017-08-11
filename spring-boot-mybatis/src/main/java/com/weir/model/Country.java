package com.weir.model;


public class Country extends BaseEntity{

    public Country() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
     * 名称
     */
    private String countryname;

    /**
     * 代码
     */
    private String countrycode;


    /**
     * 获取名称
     *
     * @return countryname - 名称
     */
    public String getCountryname() {
        return countryname;
    }

    public Country(String countryname, String countrycode) {
		super();
		this.countryname = countryname;
		this.countrycode = countrycode;
	}

	/**
     * 设置名称
     *
     * @param countryname 名称
     */
    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    /**
     * 获取代码
     *
     * @return countrycode - 代码
     */
    public String getCountrycode() {
        return countrycode;
    }

    /**
     * 设置代码
     *
     * @param countrycode 代码
     */
    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
}