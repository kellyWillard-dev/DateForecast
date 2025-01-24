# Date Forecast API

## Overview

The Date Forecast API provides the ability to forecast a future date to avoid holidays, weekends or both.

## Description

The **ForecastDate** class provides the ability to forecast a future date and modify it by day(s) either subtracting before or adding after.
- forecastDate() *method* will forecast a date to avoid holidays and/or weekends depending on specified criteria.
- forecastDateOverPeriod() *method* will forecast a date over a period of years to avoid holidays and/or weekends depending on specified criteria.

### Forecast Criteria
- ForecastCriteriaEnum.HOLIDAY&emsp;&emsp;&emsp;&ensp;(avoid HOLIDAY)
- ForecastCriteriaEnum.SATURDAY&emsp;&emsp;&ensp;(avoid SATURDAY) 
- ForecastCriteriaEnum.SUNDAY&emsp;&emsp;&emsp;&ensp;&ensp;(avoid SUNDAY)
- ForecastCriteriaEnum.WEEKEND&emsp;&emsp;&emsp;(avoid WEEKEND)
- ForecastCriteriaEnum.ALL_CRITERIA&emsp;(avoid HOLIDAY and WEEKEND)
- ForecastCriteriaEnum.NONE 

### Forecast Direction
- ForecastDirectionEnum.BEFORE (adjust date to avoid before the specified date)
- ForecastDirectionEnum.AFTER  (adjust date to avoid after the specified date) 

### Forecast Example
```
Format:
forecastDate(Calendar date, ForecastCriteriaEnum criteria, ForecastDirectionEnum direction)

Make a payment to avoid Christmas by forecasting date to appear before the holiday or weekend.

int iyear = 2025
Calendar date = Calendar.getInstance();
date.set(iyear, Calendar.DECEMBER, 25);

Calendar adjustedDate = ForecastDate.forecastDate(date, ForecastCriteriaEnum.ALL_CRITERIA, ForecastDirectionEnum.BEFORE);
- the adjustedDate generated is before the Christmas date and weekend.

Make a payment over a period of years.
forecastDateOverPeriod(Calendar date, ForecastCriteriaEnum criteria, ForecastDirectionEnum direction,int numberOfYears)

A list of Calendar dates is generated all avoiding the holiday and weekend.
```

### Requires
Download the USHoliday API from [Holiday](https://github.com/kellyWillard-dev/Holiday.git) and Maven build it locally.


### Author: Kelly Willard<br/>&nbsp;&nbsp;Email: wrk.kelly.willard@gmail.com