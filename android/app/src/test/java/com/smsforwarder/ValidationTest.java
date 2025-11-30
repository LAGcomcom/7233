package com.smsforwarder;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class ValidationTest {
    @Test
    public void testUrlPattern() {
        Pattern p = Pattern.compile("^(https?://)?([\\w.-]+)(:\\d+)?(/.*)?$");
        assertTrue(p.matcher("https://example.com").matches());
        assertTrue(p.matcher("http://1.2.3.4:8080/api").matches());
        assertFalse(p.matcher("htp:/bad").matches());
    }
    @Test
    public void testPhonePattern() {
        Pattern p = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
        assertTrue(p.matcher("+8613912345678").matches());
        assertFalse(p.matcher("001234").matches());
        assertFalse(p.matcher("+00").matches());
    }
}
