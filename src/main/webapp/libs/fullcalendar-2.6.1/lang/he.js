!function (a) {
    "function" == typeof define && define.amd ? define(["jquery", "moment"], a) : "object" == typeof exports ? module.exports = a(require("jquery"), require("moment")) : a(jQuery, moment)
}(function (a, b) {
    !function () {
        "use strict";
        var a = (b.defineLocale || b.lang).call(b, "he", {
            months: "ינואר_פברואר_מרץ_אפריל_מאי_יוני_יולי_אוגוסט_ספטמבר_אוקטובר_נובמבר_דצמבר".split("_"),
            monthsShort: "ינו׳_פבר׳_מרץ_אפר׳_מאי_יוני_יולי_אוג׳_ספט׳_אוק׳_נוב׳_דצמ׳".split("_"),
            weekdays: "ראשון_שני_שלישי_רביעי_חמישי_שישי_שבת".split("_"),
            weekdaysShort: "א׳_ב׳_ג׳_ד׳_ה׳_ו׳_ש׳".split("_"),
            weekdaysMin: "א_ב_ג_ד_ה_ו_ש".split("_"),
            longDateFormat: {
                LT: "HH:mm",
                LTS: "HH:mm:ss",
                L: "DD/MM/YYYY",
                LL: "D [ב]MMMM YYYY",
                LLL: "D [ב]MMMM YYYY HH:mm",
                LLLL: "dddd, D [ב]MMMM YYYY HH:mm",
                l: "D/M/YYYY",
                ll: "D MMM YYYY",
                lll: "D MMM YYYY HH:mm",
                llll: "ddd, D MMM YYYY HH:mm"
            },
            calendar: {
                sameDay: "[היום ב־]LT",
                nextDay: "[מחר ב־]LT",
                nextWeek: "dddd [בשעה] LT",
                lastDay: "[אתמול ב־]LT",
                lastWeek: "[ביום] dddd [האחרון בשעה] LT",
                sameElse: "L"
            },
            relativeTime: {
                future: "בעוד %s",
                past: "לפני %s",
                s: "מספר שניות",
                m: "דקה",
                mm: "%d דקות",
                h: "שעה",
                hh: function (a) {
                    return 2 === a ? "שעתיים" : a + " שעות"
                },
                d: "יום",
                dd: function (a) {
                    return 2 === a ? "יומיים" : a + " ימים"
                },
                M: "חודש",
                MM: function (a) {
                    return 2 === a ? "חודשיים" : a + " חודשים"
                },
                y: "שנה",
                yy: function (a) {
                    return 2 === a ? "שנתיים" : a % 10 === 0 && 10 !== a ? a + " שנה" : a + " שנים"
                }
            }
        });
        return a
    }(), a.fullCalendar.datepickerLang("he", "he", {
        closeText: "סגור",
        prevText: "&#x3C;הקודם",
        nextText: "הבא&#x3E;",
        currentText: "היום",
        monthNames: ["ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני", "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"],
        monthNamesShort: ["ינו", "פבר", "מרץ", "אפר", "מאי", "יוני", "יולי", "אוג", "ספט", "אוק", "נוב", "דצמ"],
        dayNames: ["ראשון", "שני", "שלישי", "רביעי", "חמישי", "שישי", "שבת"],
        dayNamesShort: ["א'", "ב'", "ג'", "ד'", "ה'", "ו'", "שבת"],
        dayNamesMin: ["א'", "ב'", "ג'", "ד'", "ה'", "ו'", "שבת"],
        weekHeader: "Wk",
        dateFormat: "dd/mm/yy",
        firstDay: 0,
        isRTL: !0,
        showMonthAfterYear: !1,
        yearSuffix: ""
    }), a.fullCalendar.lang("he", {
        defaultButtonText: {month: "חודש", week: "שבוע", day: "יום", list: "סדר יום"},
        weekNumberTitle: "שבוע",
        allDayText: "כל היום",
        eventLimitText: "אחר"
    })
});