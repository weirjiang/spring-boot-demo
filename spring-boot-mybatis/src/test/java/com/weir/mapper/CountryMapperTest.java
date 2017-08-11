package com.weir.mapper;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weir.model.Country;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryMapperTest {
	@Autowired
	CountryMapper countryMapper;
	@Test
	public void testInsert(){
		System.out.println("the size is:"+countryMapper.selectAll().size());
//		countryMapper.insert(new Country("china","china"));
//		countryMapper.insert(new Country("japan","japan"));
		Assert.assertEquals(197, countryMapper.selectAll().size());
		
	}
	
	@Test
	public void testSize(){
		Assert.assertEquals(197, countryMapper.selectAll().size());
	}
}
