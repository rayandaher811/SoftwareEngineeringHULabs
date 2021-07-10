package org.sertia.client;

import org.sertia.contracts.price.change.ClientTicketType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Constants {
    public static final String HOME_ONLINE_SCREENING = "צפיה ביתית ע\"י לינק";
    public static final String COMING_SOON = "בקרוב בסרטיה";
    public static final String CURRENTLY_AVAILABLE = "מופיע כעת";
    public static final String MOVIE_DETAILS = "פרטי הסרט";
    public static final String HELLO_SENTENCE = "ברוכה הבאה למערכת הסרטייה";
    public static final String BRANCH_MANAGER = "מנהלת הסניף";
    public static final String CINEMA_MANAGER = "מנהלת הרשת";
    public static final String MEDIA_MANAGER = "מנהלת המדיה";
    public static final String CUSTOMER_SUPPORT = "מנהלת שירות הלקוחות";
    public static final String BUY_STREAMING_LINK = "רכישת קישור";
    public static final String PRICE_CHANGE_APPROVAL = "אישור בקשות שינוי מחירים";
    public static final String STATISTICS_VIEW = "צפיה בדוחות";
    public static final String UPDATE_TAV_SAGOL_REGULATIONS = "עדכון מדיניות תו סגול";
    public static final String HADNLE_COMLAINTS = "טיפול בתלונות";
    public static final String REFUND_EMOUNT = "סכום לזיכוי";
    public static final String RESOLVE_COMPLAINT_BTN_TXT = "קבל תלונה";
    public static final String REJECT_COMPLAINT_BTN_TXT = "דחה תלונה";
    public static final String CANCEL_REGULATIONS = "ביטול הנחיות";
    public static final String ADD_OR_DELETE_SCREENING = "הוספה / הסרה של סרט";
    public static final String ADD_OR_DELETE_STREAMING = "הוספה / הסרה של חבילת צפיה ביתית";
    public static final String EDIT_PLAYING_TIME = "עדכון / הוספת מועדי הקרנה";
    public static final String EDIT_TICKETS_PRICE = "עדכון מחירים";
    public static final String REMOVE_STREAMING_LINK = "מחיקת חבילת צפיה";
    public static final String ADD_STREAMING_LINK = "הוספת חבילת צפיה";
    public static final String ADD_SCREENING = "הוספת הקרנה";
    public static final String APPROVE_CHANGE_REQUEST = "אשר בקשת שינוי";
    public static final String DENY_CHANGE_REQUEST = "דחה בקשה";
    public static final String MOVIE_NAME = "שם הסרט: ";
    public static final String REQUEST_ID = "מזהה בקשה: ";
    public static final String NEW_PRICE = "מחיר חדש: ";
    public static final String TICKET_TYPE = "סוג כרטיס: ";
    public static final String TAV_SAGOL_LABEL_TXT = "אין תקנות פעילות כעת, תוכלי להגדירן כעת";
    public static final String START_TAV_SAGOL_REGULATIONS_TXT = "החלי מדיניות תו סגול";
    public static final String SUCCESSFUL_CANCEL_PURCHASE_MSG = "ביטול הרכישה הסתיים בהצלחה, להלן סכום הזיכוי: ";
    public static final String CANCEL_PURCHASE_TITLE = "ביטול רכישה";
    public static final String SPCIFY_PURCHASE_TYPE = "אנא ציין סוג רכישה";
    public static final String NO_NEW_REQUESTS = "אין בקשות נוספות לאישור";
    public static final String SCREENING_TIME = "שעת הקרנה: ";
    public static final String HEBREW_MOVIE_NAME = "שם הסרט בעברית: ";
    public static final String RESULT_NUMBER = "תוצאה מספר: ";
    public static final String BRANCH_NAME = "שם סניף: ";
    public static final String CHOOSE_VALID_HOUR = "בחר בבקשה שעת התחלה מתוך האפשרויות";
    public static final String CHOOSE_VALID_MIN = "בחר בבקשה דקות בשעה";
    public static final String VIEW_WORKSTATION = "ברוכה הבאה לתחנת קצה בסניף ";
    public static final String AVAILABLE_MOVIES_IN_BRANCH = "סרטים המוקרנים בסניף";

    public static final Set<Integer> HOURS = IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toSet());
    public static final Set<Integer> MINUTES = IntStream.rangeClosed(0, 59).boxed().collect(Collectors.toSet());

    public static final HashMap<String, String> TICKET_TYPE_TRANSLATIONS = new HashMap<>(){{
            put(ClientTicketType.Screening.name(), "הקרנה");
            put(ClientTicketType.Streaming.name(), "חבילת צפיה");
            put(ClientTicketType.Voucher.name(), "כרטיסיה");
    }};
}
