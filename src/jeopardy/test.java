package jeopardy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;


class test {

    @Test void test() {
        Calendar testCalendar = new GregorianCalendar(1998, 0, 20);
        Calendar pointChange = new GregorianCalendar(2001, 10, 26);
        Date testDate = testCalendar.getTime();
        Date otherDate = pointChange.getTime();
        Boolean test = testDate.before(otherDate);
        System.out.println(otherDate.before(testDate));
    }

}
