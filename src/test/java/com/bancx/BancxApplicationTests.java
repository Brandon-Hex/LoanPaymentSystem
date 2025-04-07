package com.bancx;

import com.bancx.application.ApplicationTests;
import com.bancx.loan.LoanTests;
import com.bancx.payment.PaymentTests;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SelectClasses({
		PaymentTests.class,
		LoanTests.class,
		ApplicationTests.class
})
@RunWith(JUnitPlatform.class)
class BancxApplicationTests {

	@Test
	void contextLoads() {
	}
}
