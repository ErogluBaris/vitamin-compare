package com.vitamin_market.vitamin_compare.regex;

import io.micrometer.common.util.StringUtils;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ContentMatcher {

    private static final Pattern pattern = Pattern.compile("([\\d,.]+)");

    public static BigDecimal findNumberFromString(String string) {
        if (string == null || StringUtils.isBlank(string)) {
            return BigDecimal.ZERO;
        }

        if (string.contains(".")) {
            string = string.replace(".", "");
        }

        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            String numberStr = matcher.group(1).replace(",", ".");
            return new BigDecimal(numberStr);
        }

        return BigDecimal.ZERO;
    }
}

