package ru.JSilver.asterisk.reports.tools;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
@Component
public class StatisticCalculator {

    public int getSeconds(LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getMinute() * 60 + time.getSecond();
    }

    public BigDecimal getAVG(Integer value, int div, int scale) {
        if (isZero(div, value)) {
            return new BigDecimal(0);
        }
        return new BigDecimal((float) value / div).setScale(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal getPercent (Integer total, Integer part) {
        if (isZero(total, part)) {
            return new BigDecimal(0);
        }
        return new BigDecimal((float) 100 / total * part).setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isZero (int... values) {
        for (int value : values) {
            if (value == 0) {
                return true;
            }
        }
        return false;
    }

}
