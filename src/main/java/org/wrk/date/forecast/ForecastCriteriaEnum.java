package org.wrk.date.forecast;

/**
 * <h3>ForecastCriteriaEnum</h3>enumeration provides the value for each type of avoidance criteria.
 * <p>Criteria determines whether to anticipate a holiday, weekend day (Saturday, Sunday or both) or all.</p>
 * <p>The order of the enum values must stay as designated.<br>
 * The Saturday, Sunday and Weekend array positions are aligned as even values and divisible by 2.</p>
 * <p>
 * <table style="width:50%;">
 * 	<tr>
 * 		<th>Criteria</th>
 * 		<th>Value</th>
 * 	</tr> 
 *	<tr>
 *		<td align="center">HOLIDAY</td>
 *		<td align="center">1</td>
 *	</tr>
 *	<tr>
 *		<td align="center">SATURDAY</td>
 *		<td align="center">2</td>
 *	</tr>
 *	<tr>
 *		<td align="center">SUNDAY</td>
 *		<td align="center">4</td>
 *	</tr>
 *	<tr>
 *		<td align="center">WEEKEND</td>
 *		<td align="center">6</td>
 *	</tr>
 *	<tr>
 *		<td align="center">ALL_CRITERIA</td>
 *		<td align="center">7</td>
 *	</tr>
 * </table>
 * </p>
 * <p></p>
 * @author Kelly Willard
 *
 */
public enum ForecastCriteriaEnum {
	HOLIDAY(1),
	SATURDAY(2),
	SUNDAY(4),
	WEEKEND(6),
	ALL_CRITERIA(7);
	
	private int value;
	
	ForecastCriteriaEnum(int value) {this.value = value;}
	public int getValue(){return value;};
}