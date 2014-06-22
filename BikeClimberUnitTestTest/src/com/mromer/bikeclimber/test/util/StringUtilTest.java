package com.mromer.bikeclimber.test.util;

import com.mromer.bikeclimber.utils.StringUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

public class StringUtilTest extends TestCase {
	
	public void testOneDecimal () {
		double origin = 2;
		double result = StringUtil.oneDecimal(origin);		
		Assert.assertEquals("2.0", Double.toString(result));
		
		origin = 2.3;
		result = StringUtil.oneDecimal(origin);		
		Assert.assertEquals("2.3", Double.toString(result));
		
		origin = 0;
		result = StringUtil.oneDecimal(origin);		
		Assert.assertEquals("0.0", Double.toString(result));
		
		origin = 2.36;
		result = StringUtil.oneDecimal(origin);		
		Assert.assertEquals("2.4", Double.toString(result));
		
		origin = 2.34;
		result = StringUtil.oneDecimal(origin);		
		Assert.assertEquals("2.3", Double.toString(result));
		
		origin = 2.35;
		result = StringUtil.oneDecimal(origin);		
		Assert.assertEquals("2.4", Double.toString(result));
		
	}

}
