package com.fuli.user.utils;

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
     * 获取字符串拼音的第一个字母
     * @param chinese
     * @return
     */
    public static String toFirstChar(String chinese){
        String pinyinStr = "";
        if (!StringUtils.isBlank(chinese)){
            //转为单个字符
            char[] newChar = chinese.replaceAll("[（）]","").toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < newChar.length; i++) {
                if (newChar[i] > 128) {
                    try {
                        pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0].charAt(0);
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                }else{
                    pinyinStr += newChar[i];
                }
            }
        }
        return pinyinStr;
    }

    /**
     * 汉字转为拼音
     * @param chinese
     * @return
     */
    public static String toPinyin(String chinese){
        String pinyinStr = "";
        try {
            char[] newChar = chinese.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            for (int i = 0; i < newChar.length; i++) {
                if (newChar[i] == 183) {
                    pinyinStr += String.valueOf(newChar[i]);
                }else if (newChar[i] > 128 && ! String.valueOf(newChar[i]).equals(".")) {
                    try {
                        pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                }else{
                    pinyinStr += newChar[i];
                }
            }
        }catch (Exception e){}

        /** 判断如果带：号的，并且没有e的变成v*/
        if (StringUtils.isNotBlank(pinyinStr) && pinyinStr.contains(":")){
            if (pinyinStr.contains("u:e") || pinyinStr.contains("u:n") ){
                pinyinStr = pinyinStr.replaceAll(":","");
            }else if (pinyinStr.contains("u:")){
                pinyinStr = pinyinStr.replaceAll("u:","v");
            }else {
                pinyinStr = pinyinStr.replaceAll(":","");
            }
        }
        return pinyinStr;
    }
}
