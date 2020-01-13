package com.fuli.cloud.commons.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

/**
 * 使用pinyin4j实现汉字转拼音
 * @author AnJiao
 * @date 2019/6/5
 */
public class SpellHelperUtil {

    /**
     *   //toFirstChar("·_-/振业汽车服务_（潮安）")====》·_-/zyqcfw_ca
     */
    public static String toFirstChar(String chinese) {
        StringBuilder pinyinStr = new StringBuilder();
        if (!StringUtils.isBlank(chinese)) {
            //转为单个字符
            char[] newChar = chinese.replaceAll("[（）.()]", "").toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (char c : newChar) {
                // '·'的int值为183
                if (c == 183) {
                    pinyinStr.append(c);
                    continue;
                }
                if (c > 128) {
                    try {
                        pinyinStr.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0].charAt(0));
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else {
                    pinyinStr.append(c);
                }
            }
        }
        return pinyinStr.toString();
    }

    /**
     *  //toPinyin("·_-/汉字转换为拼音")====》·_-/hanzizhuanhuanweipinyin
     */
    public static String toPinyin(String chinese) {
        String pinyin = "";
        try {
            StringBuilder pinyinStr = new StringBuilder();
            char[] newChar = chinese.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (char c : newChar) {
                if (c == 183) {
                    pinyinStr.append(c);
                    continue;
                }
                if (c > 128) {
                    try {
                        pinyinStr.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]);
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else {
                    pinyinStr.append(c);
                }
            }
            pinyin = pinyinStr.toString();
        } catch (Exception e) {
        }

        /** 判断如果带：号的，并且没有e的变成v*/
        if (StringUtils.isNotBlank(pinyin) && pinyin.contains(":")) {
            if (pinyin.contains("u:e") || pinyin.contains("u:n")) {
                pinyin = pinyin.replaceAll(":", "");
            } else if (pinyin.contains("u:")) {
                pinyin = pinyin.replaceAll("u:", "v");
            } else {
                pinyin = pinyin.replaceAll(":", "");
            }
        }
        return pinyin;
    }
}
