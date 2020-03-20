package com.ruoyi.product.unit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenm
 * @create 2019-09-27 19:33
 */
public class PatternUtil {

    public static final String regChenese="[\\u4e00-\\u9fa5]"; //提取中文

    public static final String regNum = "[0-9]";               //  提出数字

    public static final String regEnglish= "[a-zA-Z]";         //  提取字母

    public static final String regEnglishNnm= "[a-zA-Z0-9]";   //  提取字母加数字

    public static String extract(String reg,String str){
        Pattern p  =  Pattern.compile(reg);
        Matcher m  =  p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String str = "……^1dsf  の  adS   DFASFSADF阿德斯防守对方asdfsadf37《？：？@%#￥%#￥%@#$%#@$%^><?1234";
        String regEx="[a-zA-Z0-9\\u4e00-\\u9fa5]";
        Pattern p  =  Pattern.compile(regEx);
        Matcher m  =  p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            sb.append(m.group());
        }
        System.out.println(sb);

        System.out.println("--------------------"+extract(regEnglish,"25kg"));
    }
}
