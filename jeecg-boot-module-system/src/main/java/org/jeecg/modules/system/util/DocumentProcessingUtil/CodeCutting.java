package org.jeecg.modules.system.util.DocumentProcessingUtil;

import org.jeecg.modules.system.model.Base64Decode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2022年09月15日 16:11
 */
public class CodeCutting {

    public static String[] codecutting(String code){
        ArrayList<String> result = new ArrayList<String>();
        code = Base64Decode.decode(code);
        return code.split("\n");
    }
}
