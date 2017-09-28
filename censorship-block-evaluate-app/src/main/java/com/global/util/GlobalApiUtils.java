package com.global.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.TimeZone;

/**
 * The Class FlightUtils.
 *
 * @author ankit
 */
public class GlobalApiUtils {

	/** The random. */
	public static Random random = new Random();
	
	/** The Constant FORMATTER. */
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/** The Constant sdf. */
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


	/**
	 * Instantiates a new flight utils.
	 */
	public GlobalApiUtils() {
	}

	/**
	 * Gets the lattitude.
	 *
	 * @return the lattitude
	 */
	public static double getLattitude() {
		return Double.parseDouble(new DecimalFormat("##.####").format(((random.nextDouble() * -180.0) + 90.0)));
	}

	/**
	 * Gets the langitude.
	 *
	 * @return the langitude
	 */
	public static double getLangitude() {
		return Double.parseDouble(new DecimalFormat("##.####").format(((random.nextDouble() * -360.0) + 180.0)));
	}
	
	/**
	 * Gets the random date.
	 *
	 * @param minRange the min range
	 * @return the random date
	 */
	public static String getRandomDate(String minRange){
		LocalDate date = LocalDate.parse(minRange);
		LocalDate newDate = date.plusDays(getRandomNumber());
		return newDate.toString();
	}

	/**
	 * Gets the random number.
	 *
	 * @return the random number
	 */
	public static int getRandomNumber() {
		return new Random().ints(1, 0, 999).findFirst().getAsInt();
	}
	
	/**
	 * Now time formatter.
	 *
	 * @return the string
	 */
	public static String nowTimeFormatter() {
		return ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT).split("T")[0];
	}
	
	/**
	 * Utc time zone.
	 *
	 * @param timeZone the time zone
	 * @return the string
	 */
	public static String utcTimeZone(final String timeZone){
		String utcTimeZone = "UTC";
		TimeZone zone = TimeZone.getTimeZone(timeZone);
		ZonedDateTime time = ZonedDateTime.now(zone.toZoneId());
		return utcTimeZone + time.getOffset();
	}
	
	public static String getUTCOffset(String timeZone){
		String offset = timeZone.replace("UTC","");
		return offset;
	}
	
	public static float timeZoneDifference(final String toTimeZone, final String fromTimeZone){
		ZonedDateTime toTime = ZonedDateTime.now(ZoneId.of(toTimeZone));
		ZonedDateTime fromTime = ZonedDateTime.now(ZoneId.of(fromTimeZone));
		ZoneOffset toOffset = toTime.getOffset();
		ZoneOffset fromOffset = fromTime.getOffset();
		float offset = Float.valueOf(toOffset.compareTo(fromOffset) / 3600f);
		return offset;
	}
}
