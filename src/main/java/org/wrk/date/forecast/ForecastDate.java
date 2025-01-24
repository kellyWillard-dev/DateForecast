package org.wrk.date.forecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.wrk.date.holiday.ObservedHolidays;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * <h3>ForecastDate</h3>
 * <p>
 * This class provides the ability to forecast a date so that it will avoid a holiday, weekend or both.<br>
 * The date will be adjusted from the day specified by the criteria.
 * </p>
 * <h4>Methods</h4>
 * <li>forecastDate
 * <li>forecastDateOverPeriod
 * <h4>Example:</h4>
 * <p>
 * A client makes an end of year tax payment to the government.<br/>
 * The forecastDate method will determine if a payment can be made on the specified date.<br/> 
 * A new date is generated to occur before or after the specified date depending on direction and criteria.
 * </p>
 * <p>.defaultPeriod</p>
 * @author Kelly Willard
 * @see org.wrk.date.forecast.ForecastCriteriaEnum
 * @see org.wrk.date.forecast.ForecastDirectionEnum
 */
public class ForecastDate implements ForecastRules {
	@Getter @Setter(AccessLevel.NONE) private int defaultPeriod = 5;
	
	@Getter @Setter(AccessLevel.NONE) private int maxPeriod = 100;
	
	@Getter @Setter(AccessLevel.NONE) private int forecastPeriodInYears = defaultPeriod;
	
	@Getter @Setter(AccessLevel.NONE) private ObservedHolidays observedHolidays = null;
	
	/**
	 * <p>constructor</p>
	 */
	public ForecastDate() {
		this.setObservedHolidays(this.observedHolidays);
	}
	
	/**
	 * <p>constructor w/param</p>
	 * @param ObservedHolidays
	 */
	public ForecastDate(ObservedHolidays observedHolidays) {
		this.setObservedHolidays(observedHolidays);
	}
	
	/**
	 * <p>Adjust the date parameter to avoid the forecast criteria.</p>
	 * <p></p>
	 * @param date
	 * @param criteria
	 * @param direction
	 * @return date parameter adjusted if criteria is met.
	 */
	private Calendar forecastAdjustment(Calendar date, ForecastCriteriaEnum criteria, ForecastDirectionEnum direction) {
		// Get the observed holiday object for the date year.
		ObservedHolidays holidays = observedHolidays.clone(date.get(Calendar.YEAR));
		
		// Create a Calendar object that is used to adjust the date if criteria is met.
		Calendar now = Calendar.getInstance();
		
		// Assign the current date.
		now.setTime(date.getTime());
		
		// Remove the time stamp.
		now = holidays.deleteTimestamp(now);

		// Adjust date according to criteria.
		while(this.isCriteriaMet(holidays, now, criteria)) {
			// Which direction to avoid forecast?
			if(direction.equals(ForecastDirectionEnum.BEFORE)) {
				// Subtract 1 day if criteria is met.
				now.add(Calendar.DAY_OF_MONTH, -1);				
			}
			else {
				// Add 1 day if criteria is met.
				now.add(Calendar.DAY_OF_MONTH, 1);				
			}
		}
		
		return now;
	}
	
	/**
	 * <p>
	 * Forecast the parameter date, avoiding the specified forecast criteria.<br/>
	 * The date is adjusted, if needed, by subtracting or adding (depending on direction) one or more days from the parameter date.<br/><br/>
	 * Method defaults criteria to ForecastCriteriaEnum.ALL_CRITERIA.  Date will avoid both holiday and weekend<br/>
	 * Method defaults direction to ForecastDirectionEnum.BEFORE.  Adjustments will subtract from the date.<br/>
	 * </p>
	 * @param date
	 * @return Calendar date adjusted to avoid specified criteria if needed.
	 * @see org.wrk.holiday.ForecastCriteriaEnum
	 */
	public Calendar forecastDate(Calendar date) {
		return date != null ? this.forecastDate(date, ForecastCriteriaEnum.ALL_CRITERIA, ForecastDirectionEnum.BEFORE) : date;
	}
	
	/**
	 * <p>Forecast the parameter date, avoiding the specified forecast criteria.<br/>
	 * The date is adjusted, if needed, by subtracting or adding (depending on direction) one or more days from the parameter date.<br/><br/>
	 * Method defaults direction to ForecastDirectionEnum.BEFORE.  Adjustments will subtract from the date.<br/></p>
	 * @param date
	 * @param criteria
	 * 
	 * @return Calendar date adjusted to avoid specified criteria if needed.
	 */
	public Calendar forecastDate(Calendar date, ForecastCriteriaEnum criteria) {
		return date != null && criteria != null ? this.forecastDate(date, criteria, ForecastDirectionEnum.BEFORE) : date;
	}
	
	/**
	 * <p>
	 * Forecast the parameter (future) date, avoiding the specified forecast criteria.<br/>
	 * The date is adjusted, if needed, by subtracting or adding (depending on direction) one or more days from the parameter date.
	 * </p>
	 * @param date
	 * @param criteria
	 * @param direction
	 * @return Calendar date adjusted to avoid specified criteria if needed.
	 */
	public Calendar forecastDate(Calendar date, ForecastCriteriaEnum criteria, ForecastDirectionEnum direction) {
		return date != null 
				&& date.after(this.deleteTimestamp(Calendar.getInstance()))
				&& criteria != null 
				&& direction != null 
				? this.forecastAdjustment(date, criteria, direction) : date;
	}
		
	/**
	 * <p>
	 * Forecast the parameter date over a period of years, avoiding the specified forecast criteria.<br/>
	 * Each year the date is adjusted, if needed, by subtracting or adding (depending on direction) one or more days before the parameter date.<br/><br/>
	 * Method defaults criteria to ForecastCriteriaEnum.ALL_CRITERIA.  Date will avoid both holiday and weekend<br/>
	 * Method defaults direction to ForecastDirectionEnum.BEFORE.  Adjustments will subtract from the date.<br/>
	 * Method uses default forecastPeriodInYears.
	 * </p>
	 * @param date
	 * @return List of Calendar dates that avoid occurring on criteria specified.
	 */
	public List<Calendar> forecastDateOverPeriod(Calendar date) {
		return date != null ? this.forecastDateOverPeriod(date, ForecastCriteriaEnum.ALL_CRITERIA, ForecastDirectionEnum.BEFORE, forecastPeriodInYears): null;
	}
	
	/**
	 * <p>
	 * Forecast the parameter date over a period of years, avoiding the specified forecast criteria.<br/>
	 * Each year the date is adjusted, if needed, by subtracting or adding (depending on direction) one or more days before the parameter date.<br/><br/>
	 * Method defaults direction to ForecastDirectionEnum.BEFORE.  Adjustments will subtract from the date.<br/>
	 * Method uses default forecastPeriodInYears.
	 * </p>
	 * @param date
	 * @param criteria
	 * @return List of Calendar dates that avoid occurring on criteria specified.
	 */
	public List<Calendar> forecastDateOverPeriod(Calendar date, ForecastCriteriaEnum criteria) {
		return date != null ? this.forecastDateOverPeriod(date, criteria, ForecastDirectionEnum.BEFORE, forecastPeriodInYears): null;
	}
	
	/**
	 * <p>
	 * Forecast the parameter date over a period of years, avoiding the specified forecast criteria.<br/>
	 * Each year the date is adjusted, if needed, by subtracting or adding (depending on direction) one or more days before the parameter date.<br/><br/>
	 * Method uses default forecastPeriodInYears.
	 * </p>
	 * @param date
	 * @param criteria
	 * @return List of Calendar dates that avoid occurring on criteria specified.
	 */
	public List<Calendar> forecastDateOverPeriod(Calendar date, ForecastCriteriaEnum criteria, ForecastDirectionEnum direction) {
		return date != null ? this.forecastDateOverPeriod(date, criteria, direction, forecastPeriodInYears): null;
	}
	
	/**
	 * <p>
	 * Forecast the parameter date over a period of years, avoiding the specified forecast criteria.<br/>
	 * Each year the date is adjusted, if needed, by subtracting or adding (depending on direction) one or more days before the parameter date.
	 * </p>
	 * @param date
	 * @param criteria
	 * @param direction
	 * @param period
	 * @return List of Calendar dates that avoid occurring on criteria specified.
	 */
	public List<Calendar> forecastDateOverPeriod(Calendar date, ForecastCriteriaEnum criteria, ForecastDirectionEnum direction, int period) {
		List<Calendar> response = new ArrayList<>();
		
		// Get the forecast period.
		period = 0 < period && period <= maxPeriod ? period : forecastPeriodInYears;
		
		// Is the date, criteria and direction valid?
		if(date != null && criteria != null && direction != null) {
			// Iterate through the period of years.
			for(int index=0; index < period; index++) {
				// Generate a new Calendar instance.
				Calendar forecastDate = Calendar.getInstance();
				
				// Set the date with to the same month and day but increment the year.
				forecastDate.set(date.get(Calendar.YEAR)+index, date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
				
				// Was forecast criteria specified?
				if(0 < criteria.getValue()) {
					// Adjust the date if needed and add to the list.
					response.add(this.forecastDate(forecastDate, criteria, direction));
				}
				else {
					// Add date to the list unadjusted.
					response.add(forecastDate);
				}
			}

			if(!response.isEmpty()) {
				// Sort the dates.
				Collections.sort(response);				
			}
		}
		
		return response;
	}

	/**
	 * <p>Determine if the forecast request has met its forecast criteria.</p>
	 * <p></p>
	 * @param holidays
	 * @param avoidDate
	 * @param criteria
	 * @return boolean true if the criteria has been met else false.
	 */
	private boolean isCriteriaMet(ObservedHolidays holidays, Calendar avoidDate, ForecastCriteriaEnum criteria) {
		boolean isMet = false;
		
		// Is forecast criteria set?
		if(!this.criteriaNone(criteria)) {
			// Is holiday criteria set?
			if(this.avoidHoliday(criteria)) {
				try {
					// Determine if date is a holiday.
					isMet = holidays.isHoliday(avoidDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// Was the holiday criteria met?  Is weekend criteria set?
			if(!isMet && this.avoidWeekend(criteria)) {
				// Determine which weekend criteria is set.
				ForecastCriteriaEnum switchValue = this.weekEndCriteria(criteria);
				
				// Was the weekend criteria determined?
				if(switchValue != null) {
					switch(switchValue) {
					case SATURDAY:
						isMet = this.isSaturday(avoidDate);
						break;
					case SUNDAY:
						isMet = this.isSunday(avoidDate);
						break;
					case WEEKEND:
						isMet = this.isWeekEnd(avoidDate);
						break;
					default:
						break;
					}							
				}
			}			
		}
		
		return isMet;
	}
	
	/**
	 * 
	 * @param forecastPeriodInYears
	 */
	public void setForecastPeriodInYears(int forecastPeriodInYears) {
		this.forecastPeriodInYears = 0 < forecastPeriodInYears && forecastPeriodInYears <= maxPeriod ? forecastPeriodInYears : defaultPeriod;
	}
	
	/**
	 * @param observedHolidays the observedHolidays to set
	 */
	public void setObservedHolidays(ObservedHolidays observedHolidays) {
		// Is the parameter null?
		if(observedHolidays != null) {
			// Assign the parameter to the observed holidays.
			this.observedHolidays = observedHolidays;
			
			// Determine if the assigned holidays have been initialized.
			if(0 >= this.observedHolidays.toHolidays().length) {
				this.observedHolidays.init();	
			}			
		}
		else {
			// Create default holidays to use.
			this.observedHolidays = new ObservedHolidays();
			
			// Initialize the default holidays.
			this.observedHolidays.init();
		}
	}
}