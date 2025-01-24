package org.wrk.date.forecast;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wrk.date.holiday.HolidayEnum;
import org.wrk.date.holiday.ObservedHolidays;

import lombok.Getter;
import lombok.Setter;

/**
 * <h3>TestForecastDate</h3>
 * 
 * @author Kelly Willard
 */
@Getter
@Setter
@ExtendWith(value = { SpringExtension.class })
@ContextConfiguration(locations= {"/test-holiday-application-context.xml"})
public class TestForecastDate {
	
	@Autowired
	private ForecastDate federalForecast;

	SimpleDateFormat sdf = new SimpleDateFormat("EEEEE MMMMM dd yyyy");
	
	/**
	 * constructor
	 */
	public TestForecastDate() {
	}

	/**
	 * <p>Generate a future date for a specific day of the week.</p>
	 * @param date
	 * @param dayOfWeek
	 * @return Calendar date 1 year from now positioned at the day of the week specified.
	 */
	private Calendar generateFutureDayOfWeek(Calendar date,int dayOfWeek) {
		int iyear = date.get(Calendar.YEAR) + 1;
		int imonth = date.get(Calendar.MONTH);
		int iday = date.get(Calendar.DAY_OF_MONTH);
		
		Calendar today = Calendar.getInstance();
		
		today.set(iyear, imonth, iday);
		
		boolean condition = false;
		
		do {
			if(today.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
				condition = true;
			}
			else {
				today.add(Calendar.DAY_OF_MONTH, 1);
			}
		} while(!condition);
		
		return today;
	}
	
	/**
	 * <p>Generate a future holiday date.</p>
	 * @param holiday specify which holiday enumeration value.
	 * @param yearIncrement add number of years beyond the current year.
	 * @return Calendar future holiday date
	 */
	private Calendar generateFutureHolidayDate(HolidayEnum holiday, int yearIncrement) {
		Calendar response = Calendar.getInstance();
		
		int iyear = response.get(Calendar.YEAR) + yearIncrement;
		
		ObservedHolidays oh = federalForecast.getObservedHolidays().clone(iyear);
		
		ForecastDate fd = new ForecastDate(oh);

		switch (holiday) {
		case CHRISTMAS_DAY:
			response = fd.getObservedHolidays().getChristmasDay();
			break;
		case COLUMBUS_DAY:
			response = fd.getObservedHolidays().getColumbusDay();
			break;
		case INDEPENDENCE_DAY:
			response = fd.getObservedHolidays().getIndependenceDay();
			break;
		case JUNETEENTH_DAY:
			response = fd.getObservedHolidays().getJuneteenthDay();
			break;
		case LABOR_DAY:
			response = fd.getObservedHolidays().getLaborDay();
			break;
		case MARTINLUTHERKINGJR_DAY:
			response = fd.getObservedHolidays().getMartinLutherKingJrDay();
			break;
		case MEMORIAL_DAY:
			response = fd.getObservedHolidays().getMemorialDay();
			break;
		case NEWYEARS_DAY:
			response = fd.getObservedHolidays().getNewYearsDay();
			break;
		case PRESIDENTS_DAY:
			response = fd.getObservedHolidays().getPresidentsDay();
			break;
		case THANKSGIVING_DAY:
			response = fd.getObservedHolidays().getThanksgivingDay();
			break;
		case VETERANS_DAY:
			response = fd.getObservedHolidays().getVeteransDay();
			break;
		default:
			break;
		}
		
		return response;
	}

	@Test
	public void testAvoidHolidayCriteriaAll() {
		assertTrue(federalForecast.avoidHoliday(ForecastCriteriaEnum.ALL_CRITERIA),"Holiday not avoided.");
	}

	@Test
	public void testAvoidHolidayCriteriaHoliday() {
		assertTrue(federalForecast.avoidHoliday(ForecastCriteriaEnum.HOLIDAY),"Holiday not avoided.");
	}

	@Test
	public void testAvoidHolidayCriteriaNull() {
		assertFalse(federalForecast.avoidHoliday(null),"Holiday avoided.");
	}

	@Test
	public void testAvoidHolidayCriteriaSaturday() {
		assertFalse(federalForecast.avoidHoliday(ForecastCriteriaEnum.SATURDAY),"Holiday avoided.");
	}

	@Test
	public void testAvoidHolidayCriteriaSunday() {
		assertFalse(federalForecast.avoidHoliday(ForecastCriteriaEnum.SUNDAY),"Holiday avoided.");
	}

	@Test
	public void testAvoidHolidayCriteriaWeekend() {
		assertFalse(federalForecast.avoidHoliday(ForecastCriteriaEnum.WEEKEND),"Holiday avoided.");
	}

	@Test
	public void testAvoidWeekendCriteriaAll() {
		assertTrue(federalForecast.avoidWeekend(ForecastCriteriaEnum.ALL_CRITERIA),"Weekend not avoided.");
	}

	@Test
	public void testAvoidWeekendCriteriaHoliday() {
		assertFalse(federalForecast.avoidWeekend(ForecastCriteriaEnum.HOLIDAY),"Weekend avoided.");
	}
	
	@Test
	public void testAvoidWeekendCriteriaNull() {
		assertFalse(federalForecast.avoidWeekend(null),"Weekend avoided.");
	}	

	@Test
	public void testAvoidWeekendCriteriaSaturday() {
		assertTrue(federalForecast.avoidWeekend(ForecastCriteriaEnum.SATURDAY),"Weekend not avoided.");
	}

	@Test
	public void testAvoidWeekendCriteriaSunday() {
		assertTrue(federalForecast.avoidWeekend(ForecastCriteriaEnum.SUNDAY),"Weekend not avoided.");
	}

	@Test
	public void testAvoidWeekendCriteriaWeekend() {
		assertTrue(federalForecast.avoidWeekend(ForecastCriteriaEnum.WEEKEND),"Weekend not avoided.");
	}
	
	@Test
	public void testForecastHoliday() {		
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.CHRISTMAS_DAY, 3);
		
		Calendar availableDay = federalForecast.forecastDate(today);
		
		assertTrue(today.after(availableDay), "Today and Forecast date are the same day.");
	}
	
	@Test
	public void testForecastHolidayAfter() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.CHRISTMAS_DAY, 3);
		
		Calendar availableDay = federalForecast.forecastDate(today, ForecastCriteriaEnum.ALL_CRITERIA, ForecastDirectionEnum.AFTER);
		
		assertTrue(today.before(availableDay), "Today and Forecast date are the same day.");
	}
	
	@Test
	public void testForecastHolidayOverPeriod() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.CHRISTMAS_DAY, 3);
		
		List<Calendar> list = federalForecast.forecastDateOverPeriod(today);
		
		list.forEach(it -> {
			assertTrue(it.get(Calendar.MONTH) == Calendar.DECEMBER && 25 != it.get(Calendar.DAY_OF_MONTH), "Date is Christmas.");			
		});
	}
		
	@Test
	public void testForecastNullDate() {
		Calendar date = federalForecast.forecastDate(null);
		
		assertTrue(date == null, "Forecast date is not null.");
	}
	
	@Test
	public void testForecastNullHolidayList() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.INDEPENDENCE_DAY, 1);
		
		ForecastDate fd = new ForecastDate(new ObservedHolidays());
		
		Calendar date = fd.forecastDate(today);
		
		assertTrue(today.after(date), "Today and Forecast date are the same day.");
	}
	
	@Test
	public void testForecastNullHolidays() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.LABOR_DAY, 1);
		
		ForecastDate fd = new ForecastDate(null);
		
		Calendar date = fd.forecastDate(today);
		
		assertTrue(today.after(date), "Today and Forecast date are the same day.");
	}
	
	@Test
	public void testForecastPastDate() {
		Calendar today = federalForecast.getObservedHolidays().getNewYearsDay();
		
		today.add(Calendar.YEAR, -1);
		
		Calendar availableDay = federalForecast.forecastDate(today);
		
		assertFalse(today.after(availableDay), "Today and Forecast date are not the same day.");
	}	
	
	@Test
	public void testForecastPastDateOverPeriod() {
		Calendar today = federalForecast.getObservedHolidays().getNewYearsDay();
		
		today.add(Calendar.YEAR, -5);
		
		List<Calendar> list = federalForecast.forecastDateOverPeriod(today);
		
		list.forEach(it -> {
			assertTrue(it.get(Calendar.MONTH) == Calendar.JANUARY && it.get(Calendar.DAY_OF_MONTH) == 1, "Date is not January 1.");			
		});
	}	
	
	@Test
	public void testForecastPeriodLesserDefault() {
		Calendar today = Calendar.getInstance();
		
		int periodSize = 3;
		
		federalForecast.setForecastPeriodInYears(periodSize);
		
		List<Calendar> list = federalForecast.forecastDateOverPeriod(today);
		
		// Reset for other tests.
		federalForecast.setForecastPeriodInYears(5);
		
		assertTrue(list.size() == periodSize, "No forecast list available.");
	}
	
	/* */
	@Test
	public void testForecastPeriodMaximum() {
		Calendar today = Calendar.getInstance();
		
		federalForecast.setForecastPeriodInYears(100);
		
		List<Calendar> list = federalForecast.forecastDateOverPeriod(today);
		
		// Reset for other tests.
		federalForecast.setForecastPeriodInYears(5);
		
		assertTrue(list.size() == federalForecast.getMaxPeriod(), "No forecast list available.");
	}
	
	/* */
	@Test
	public void testForecastPeriodMaximumExcess() {
		Calendar today = Calendar.getInstance();

		federalForecast.setForecastPeriodInYears(101);
		
		List<Calendar> list = federalForecast.forecastDateOverPeriod(today);
		
		assertTrue(list.size() == federalForecast.getDefaultPeriod(), "No forecast list available.");
	}
	
	@Test
	public void testForecastPeriodNullDateAndNullHolidays() {
		ForecastDate fd = new ForecastDate(null);
		
		List<Calendar> list = fd.forecastDateOverPeriod(null);
		
		assertTrue(list == null, "Forecast list available.");
	}
	
	@Test
	public void testForecastPeriodNullHolidays() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.THANKSGIVING_DAY, 1);
		
		ForecastDate fd = new ForecastDate(null);
		
		List<Calendar> list = fd.forecastDateOverPeriod(today);
		
		assertTrue(!list.isEmpty(), "No forecast list available.");
	}

	@Test
	public void testForecastSaturday() {
		Calendar today = this.generateFutureDayOfWeek(Calendar.getInstance(),Calendar.SATURDAY);
		
		today = federalForecast.getObservedHolidays().deleteTimestamp(today);
		
		Calendar availableDay = federalForecast.forecastDate(today, ForecastCriteriaEnum.SATURDAY);
		
		assertTrue(today.after(availableDay),"Today and Forecast date are the same day.");
	}
	
	@Test
	public void testForecastSunday() {
		Calendar today = this.generateFutureDayOfWeek(Calendar.getInstance(),Calendar.SUNDAY);
		
		today = federalForecast.getObservedHolidays().deleteTimestamp(today);
		
		Calendar availableDay = federalForecast.forecastDate(today, ForecastCriteriaEnum.SUNDAY);
		
		assertTrue(today.after(availableDay),"Today and Forecast date are the same day.");		
	}
	
	@Test
	public void testForecastWeekend() {
		Calendar today = this.generateFutureDayOfWeek(Calendar.getInstance(),Calendar.SUNDAY);
		
		today = federalForecast.getObservedHolidays().deleteTimestamp(today);
		
		Calendar availableDay = federalForecast.forecastDate(today, ForecastCriteriaEnum.WEEKEND);
		
		assertTrue(today.after(availableDay),"Today and Forecast date are the same day.");				
	}
}