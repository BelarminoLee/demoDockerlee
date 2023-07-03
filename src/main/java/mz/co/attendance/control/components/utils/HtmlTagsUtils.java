package mz.co.attendance.control.components.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

public class HtmlTagsUtils {

    public static String removeHtmlTags(String html) {
        if (StringUtils.isNotBlank(html)) {
            return Jsoup.parse(html).text();
        }
        return "";
    }
}
