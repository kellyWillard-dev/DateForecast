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
@ExtendWith(value = { SpringExtension.class })
@ContextConfiguration(locations= {"/test-holiday-application-context.xml"})
public class TestForecastDateCorp {
	
	@Autowired
	@Getter @Setter private ForecastDate forecastCorpDate;
	
	@SuppressWarnings("unused")
	private SimpleDateFormat sdf = new SimpleDateFormat("EEEEE MMMMM dd yyyy HH:mm:SS");
	
	/**
	 * constructor
	 */
	public TestForecastDateCorp() {
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
		
		ObservedHolidays oh = this.getForecastCorpDate().getObservedHolidays().clone(iyear);
		
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
	public void testForecastColumbusDay() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.COLUMBUS_DAY, 1);
		
		Calendar availableDay = this.getForecastCorpDate().forecastDate(today);
		
		assertFalse(today.after(availableDay), "Today and Forecast date are not the same day.");
	}
	
	@Test
	public void testForecastColumbusDayOverPeriod() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.COLUMBUS_DAY, 1);
		
		List<Calendar> list = this.getForecastCorpDate().forecastDateOverPeriod(today);
		
		list.forEach(it -> {
			// System.out.println(String.format("%s", this.getSdf().format(it.getTime())));
			assertTrue(it.get(Calendar.MONTH) == Calendar.OCTOBER && 15 > it.get(Calendar.DAY_OF_MONTH), "Date is Columbus day.");			
		});
	}
	
	@Test
	public void testForecastJuneteenth() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.JUNETEENTH_DAY, 1);
		
		Calendar availableDay = this.getForecastCorpDate().forecastDate(today);
		
		assertFalse(today.after(availableDay), "Today and Forecast date are not the same day.");
	}
	
	@Test
	public void testForecastJuneteenthOverPeriod() {		
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.JUNETEENTH_DAY, 1);
		
		List<Calendar> list = this.getForecastCorpDate().forecastDateOverPeriod(today, ForecastCriteriaEnum.HOLIDAY);
		
		list.forEach(it -> {
			assertTrue(it.get(Calendar.MONTH) == Calendar.JUNE && 19 == it.get(Calendar.DAY_OF_MONTH), "Date is not Juneteenth day.");			
		});
	}
	
	@Test
	public void testForecastMartinLutherKingJr() {		
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.MARTINLUTHERKINGJR_DAY, 1);
		
		Calendar availableDay = this.getForecastCorpDate().forecastDate(today);
		
		assertFalse(today.after(availableDay), "Today and Forecast date are not the same day.");
	}	
	
	@Test
	public void testForecastMartinLutherKingJrOverPeriod() {		
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.MARTINLUTHERKINGJR_DAY, 1);
		
		List<Calendar> list = this.getForecastCorpDate().forecastDateOverPeriod(today);
		
		list.forEach(it -> {
			assertTrue(it.get(Calendar.MONTH) == Calendar.JANUARY && 22 > it.get(Calendar.DAY_OF_MONTH), "Date is MLK Jr. day.");			
		});
	}	
	
	@Test
	public void testForecastPresidentsDay() {		
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.PRESIDENTS_DAY, 1);
		
		Calendar availableDay = this.getForecastCorpDate().forecastDate(today);
		
		assertFalse(today.after(availableDay), "Forecast date is not the same day.");
	}
	
	@Test
	public void testForecastPresidentsDayPeriod() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.PRESIDENTS_DAY, 1);
		
		List<Calendar> list = this.getForecastCorpDate().forecastDateOverPeriod(today);
		
		list.forEach(it -> {
			assertTrue(it.get(Calendar.MONTH) == Calendar.FEBRUARY && 22 > it.get(Calendar.DAY_OF_MONTH), "Date is Presidents day.");			
		});
	}
	
	@Test
	public void testForecastVeteransDay() {		
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.VETERANS_DAY, 1);
		
		Calendar availableDay = this.getForecastCorpDate().forecastDate(today);
		
		assertFalse(today.after(availableDay), "Forecast date is not the same day.");
	}
	
	@Test
	public void testForecastVeteransDayPeriod() {
		Calendar today = this.generateFutureHolidayDate(HolidayEnum.VETERANS_DAY, 1);
		
		List<Calendar> list = this.getForecastCorpDate().forecastDateOverPeriod(today, ForecastCriteriaEnum.HOLIDAY);
		
		list.forEach(it -> {
			assertTrue(it.get(Calendar.MONTH) == Calendar.NOVEMBER && 11 == it.get(Calendar.DAY_OF_MONTH), "Date is not Veterans day.");			
		});
	}
}