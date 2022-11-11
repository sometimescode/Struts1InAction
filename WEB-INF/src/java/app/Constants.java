package app;
public final class Constants {
/**
 * The session scope attribute under which the Username
 * for the currently logged in user is stored.
 */
public static final String USER_KEY = "user";
/**
 * The value to indicate debug logging.
 */
public static final int DEBUG = 1;
/**
 * The value to indicate normal logging.
 */
public static final int NORMAL = 0;
/** 
* The token that represents a nominal outcome
 * in an ActionForward.
 */
public static final String SUCCESS= "success";
/**
 * The token that represents the logon activity
 * in an ActionForward.
 */
public static final String LOGON = "logon";
/**
 * The token that represents the welcome activity
 * in an ActionForward.
 */
public static final String WELCOME = "welcome";
}