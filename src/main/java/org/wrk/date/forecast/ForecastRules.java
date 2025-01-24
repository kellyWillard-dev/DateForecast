package org.wrk.date.forecast;

import java.util.Calendar;

/**
 * <h3>ForecastRules</h3>interface implements ForecastDate logic.
 * <h4>Functionality</h4>
 * <li>avoidHoliday determines if the holiday criteria has been set.  Returns true to avoid holidays else false.
 * <li>avoidWeekend determines if the weekend criteria has been set.  Returns true to avoid weekend else false.
 * <li>isSaturday returns true if date occurs on Saturday else false.
 * <li>isSunday returns true if date occurs on Sunday else false.
 * <li>weekEndCriteria returns which weekend criteria has been set.  Either SATURDAY, SUNDAY or WEEKEND.
 * <li>isWeekEnd returns true if date occurs on weekend else false.
 * <p></p>
 * @author Kelly Willard
 */
public interface ForecastRules {

	/**
	 * <p>Determine if HOLIDAY criteria was set.</p>
	 * @param criteria
	 * @return boolean true if HOLIDAY forecast criteria is set else false.
	 * @see org.wrk.date.forecast.ForecastCriteriaEnum
	 */
	default public boolean avoidHoliday(ForecastCriteriaEnum criteria) {
		return criteria != null ? 1 == (criteria.getValue() % 2) : false;
	}
	
	/**
	 * <p>Determine if SATURDAY, SUNDAY or WEEKEND forecast criteria was set.</p>
	 * @param criteria
	 * @return boolean true if either SATURDAY, SUNDAY or WEEKEND criteria is set else false.
	 * @see org.wrk.date.forecast.ForecastCriteriaEnum
	 */
	default public boolean avoidWeekend(ForecastCriteriaEnum criteria) {
		boolean response = false;
		
		// Is criteria valid?
		if(criteria != null && 0 < criteria.getValue()) {
			// Assign criteria to condition.
			int condition = criteria.getValue();
			
			// Determine if ForecastCriteriaEnum.HOLIDAY is within the condition.
			if(0 < condition % 2) {
				// Remove ForecastCriteriaEnum.HOLIDAY
				condition = condition ^ ForecastCriteriaEnum.HOLIDAY.getValue();
			}
			
			// Determine if SATURDAY, SUNDAY or WEEKEND value is within the condition.
			response = 0 < condition ? 0 == (condition % 2) : false;
		}
		
		return  response;
	}
	
	/**
	 * <p>Determine if criteria was set.</p>
	 * @param criteria
	 * @return boolean true if criteria is set else false if not set.
	 * @see org.wrk.date.forecast.ForecastCriteriaEnum
	 */
	default public boolean criteriaNone(ForecastCriteriaEnum criteria) {
		return criteria == null || 0 >= criteria.getValue(); 
	}
		
	/**
	 * <p>Remove the timestamp from the calendar date.</p> 
	 * @param date
	 * @return Calendar
	 */
	default public Calendar deleteTimestamp(Calendar date) {
		if(date != null) {
			date.set(Calendar.HOUR, 0);
			
			date.set(Calendar.HOUR_OF_DAY, 0);
			
			date.set(Calendar.MINUTE, 0);
			
			date.set(Calendar.SECOND, 0);
			
			date.set(Calendar.MILLISECOND, 0);
		}
		
		return date;
	}
	
	/**
	 * <p>Is the date Saturday?</p>
	 * @param date
	 * @return true if date is Saturday else false.
	 */
	default public boolean isSaturday(Calendar date) {
		return date != null ? date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY : false;
	}
	
	/**
	 * <p>Is the date Sunday?</p>
	 * @param date
	 * @return true if date is Sunday else false.
	 */
	default public boolean isSunday(Calendar date) {
		return date != null ? date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY : false;
	}
	
	/**
	 * <p>Does the date occur on a weekend?</p>
	 * @param date
	 * @return true if weekend date else false.
	 */
	default public boolean isWeekEnd(Calendar date) {
		return this.isSaturday(date) || this.isSunday(date);
	}
	
	/**
	 * <p>Determine if SATURDAY, SUNDAY or WEEKEND forecast criteria was set.</p>
	 * @param criteria
	 * @return ForecastCriteriaEnum value for either SATURDAY, SUNDAY or WEEKEND criteria else null if not set.
	 * @see org.wrk.date.forecast.ForecastCriteriaEnum
	 */
	default public ForecastCriteriaEnum weekEndCriteria(ForecastCriteriaEnum criteria) {
		ForecastCriteriaEnum response = null;
		if(criteria != null && this.avoidWeekend(criteria)) {
			// Does criteria have the HOLIDAY set?
			if(0 < criteria.getValue() % 2) {
				// Mask out the HOLIDAY value and determine which weekend criteria was set.
				int weekend = (criteria.getValue() ^ ForecastCriteriaEnum.HOLIDAY.getValue()) / 2;
				// Get the weekend value from ForecastCriteriaEnum.
				response = ForecastCriteriaEnum.values()[weekend];
			}
			else {
				response = criteria;
			}
		}
		
		return response;
	}
}