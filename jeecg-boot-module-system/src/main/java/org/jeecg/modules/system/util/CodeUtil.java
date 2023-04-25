package org.jeecg.modules.system.util;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.stereotype.Component;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年04月08日 22:22
 */
@Component
public class CodeUtil {

    /**
     * 代码格式化方法：把代码分割生成字符串数组的形式，并给代码加上行号
     * @param code
     * @return
     */
    public String[] codeDivideToLine(String code){
        String[] split = code.split("\n");
        int length = String.valueOf(split.length).length();
        int index=1;
        for (int i = 0; i < split.length; i++) {
            split[i]=index+"  "+split[i];
            int count=String.valueOf(index).toString().length();
            while(count<length){
                split[i]=" "+split[i];
                count++;
            }
            index++;
        }
        return split;
    }

    /**
     * 代码格式化方法：把代码分割生成字符串数组的形式，并给代码加上行号
     * @param code
     * @return
     */
    public String codeDivideToLineWithStartLine(String code,int startLine){

        String[] split = code.split("\n");
        int length = String.valueOf(startLine+split.length).length();
        int index=startLine;

        String res="";
        for (int i = 0; i < split.length; i++) {
            split[i]=String.valueOf(index)+"  "+split[i];
            int count=String.valueOf(index).toString().length();
            while(count<length){
                split[i]=" "+split[i];
                count++;
            }
            index++;
            res=res+split[i]+"\n";
        }
        return res;
    }
}
