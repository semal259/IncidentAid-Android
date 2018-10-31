package com.cmusv.ias.test;

import org.junit.Test;
import com.cmusv.ias.SharedData;
import android.test.AndroidTestCase;

public class EngineParsing extends AndroidTestCase {

	@Test
	public void testEngineParsing() {
		assertEquals(3, SharedData.parseEngineNumber("Engine 3"));
		assertEquals(-1, SharedData.parseEngineNumber("Engine BAD"));
		assertEquals(-1, SharedData.parseEngineNumber("Enne 3"));
		assertEquals(3, SharedData.parseEngineNumber("Engine 3 or 4"));
		assertEquals(3, SharedData.parseEngineNumber("Engine 3.6"));
		assertEquals(-1, SharedData.parseEngineNumber(""));
		assertEquals(-1, SharedData.parseEngineNumber(4));
		
	}

}
