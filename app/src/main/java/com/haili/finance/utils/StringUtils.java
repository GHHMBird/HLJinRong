package com.haili.finance.utils;


import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String text) {
        return text == null || "".equals(text) || "null".equals(text);
    }

    public static boolean isZZ(String psd) {//判断是否是包含大写或小写字母和数字的正则表达式
        String regex = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]";
        return !psd.matches(regex);
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    public static boolean isPhone(String strPhone) {
        String strPattern = "^((?:13\\d|15[\\d]|14[\\d]|17[\\d]|18[\\d])-?\\d{5}(\\d{3}|\\*{3}))$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strPhone);
        return m.matches();
    }

    public static boolean isLandlinePhone(String strPhone) {
        String strPattern = "(\\d{4}-|\\d{3}-)?(\\d{8}|\\d{7})";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strPhone);
        return m.matches();
    }


    public static boolean isAllChinses(String str) {
        boolean isMatches = true;
        for (int i = 0; i < str.length(); i++) {
            boolean b = str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+");
            if (!b) {
                isMatches = false;
                break;
            }
        }
        return isMatches;
    }

    public static boolean isContainChinese(String str) {
        boolean isMatches = false;
        for (int i = 0; i < str.length(); i++) {
            boolean b = str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+");
            if (b) {
                return b;
            }
//            if (!b) {
//                isMatches = false;
//                break;
//            }
        }
        return isMatches;
    }

    public static boolean containChinese(String str) {

        return !isEmpty(str) && str.matches("[\\u4e00-\\u9fa5]+");
    }

    public static boolean isEnName(String str) {
        return !isEmpty(str) && str.matches("^[a-zA-Z\\s]+$");
    }

    public static boolean isAllENChar(String str) {
        boolean isMatches = true;
        for (int i = 0; i < str.length(); i++) {
            boolean b = str.substring(i, i + 1).matches("^[a-zA-Z]+$");
            if (!b) {
                isMatches = false;
                break;
            }
        }
        return isMatches;
//        return !isEmpty(str) && str.matches("^[a-zA-Z]+$");
    }


    public static boolean isAllChinese(String str) {
        boolean isMatches = true;
        for (int i = 0; i < str.length(); i++) {
            boolean b = str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+");
            if (!b) {
                isMatches = false;
                break;
            }
        }
        return isMatches;
//        return !isEmpty(str) && str.matches("^[a-zA-Z]+$");
    }

    public static String formatEmptyString(String str) {

        if (isEmpty(str)) {
            return "";
        }

        return str;
    }

    /**
     * 规则：至少包含大小写字母及数字中的两种
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }


    //判断是否有特殊字符
    public static boolean checkSpeical(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (isCheckSpeicalSucess(String.valueOf(value.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCheckSpeicalSucess(String value) {
        return value.matches("[`~!@#$%^&*()+=|{}':;',[\\\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
    }

    public static int findFistEnCharInString(String str) {
        if (isEmpty(str)) {
            return -1;
        }

        for (int i = 0; i < str.length(); i++) {
            boolean b = str.substring(i, i + 1).matches("^[A-Za-z]+$");
            if (b) {
                return i;
            }
        }

        return -1;
    }


    public static String formatDouble(Double value) {

        DecimalFormat df = new DecimalFormat("00%");
        return df.format(value);

    }

    public static String formatDouble1(Double value) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(value);
    }

    public static String formatFloat(Float value) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(value);
    }

    public static int parseStringToInteger(String string) {
        if (string == null || "".equals(string)) {
            return 0;
        }
        int num;
        try {
            num = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
        return num < 0 ? 0 : num;
    }

    public static double parseStringToDouble(String string) {
        if (string == null || "".equals(string)) {
            return 0;
        }
        double num;
        try {
            num = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
        return num < 0 ? 0 : num;
    }

    public static String toStringHex(String s) {

        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String nameAddStar(String name) {
        String textString;
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        if (name.length() == 1) {
            return "*";
        }
        if (name.length() == 2) {
            textString = name.substring(0, 1) + "*";
        } else {
            textString = name.substring(0, 1);
            for (int i = 0; i < name.length() - 2; i++) {
                textString += "*";
            }
            textString += name.substring(name.length() - 1);
        }
        return textString;
    }
}
