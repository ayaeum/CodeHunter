package org.jeecg.modules.system.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodExtractorUtil {

    public static void main(String[] args) throws IOException {
//        String filePath="F:\\jeecg-boot-master\\jeecg-boot-master\\jeecg-boot\\jeecg-boot-module-system\\src\\main\\java\\org\\jeecg\\modules\\system\\service\\impl\\SysDataSourceServiceImpl.java";
//        File file = new File(filePath);
        String javaCode ="";
        CompilationUnit cu = StaticJavaParser.parse(javacode);
        //1.获取方法名和参数
        new MethodVisitor().visit(cu, null);
        //2.分割方法
        System.out.println(new MethodVisitor().methodCutter(cu, javacode));
    }

    public static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            //获得方法名和参数
//            System.out.println("Method name: " + md.getName() + " Parameters: " + md.getParameters());
            super.visit(md, arg);
        }

        public JSONArray methodCutter(CompilationUnit cu,String filePath) throws IOException {
            //获取方法并写入文件
            // 获取所有的方法

            JSONArray jsonArray=new JSONArray();
            List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
            // 遍历每个方法
            for (MethodDeclaration method : methods) {
                // 获取方法名
                String methodName = method.getNameAsString();
                // 获取方法体
                BlockStmt methodBody = method.getBody().orElse(null);
                if (methodBody == null) {
                    continue;
                }
                // 生成方法代码
                String methodCode = method.toString();
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("filePath",filePath);
                jsonObject.put("methodName",methodName);
                jsonObject.put("methodLine",method.getRange().get().begin.line);
                jsonObject.put("methodCode",methodCode);
                jsonArray.add(jsonObject);

//                FileWriter fw = new FileWriter(methodName + ".java");
//                fw.write(methodCode);
//                fw.close();
            }
            return jsonArray;
        }
    }

    static String javacode="package org.jeecg.modules.system.util;\n" +
            "\n" +
            "import java.io.File;\n" +
            "import java.io.FileNotFoundException;\n" +
            "import java.io.FileWriter;\n" +
            "import java.io.IOException;\n" +
            "import java.util.List;\n" +
            "\n" +
            "import com.alibaba.fastjson.JSONArray;\n" +
            "import com.alibaba.fastjson.JSONObject;\n" +
            "import com.github.javaparser.StaticJavaParser;\n" +
            "import com.github.javaparser.ast.CompilationUnit;\n" +
            "import com.github.javaparser.ast.body.MethodDeclaration;\n" +
            "import com.github.javaparser.ast.stmt.BlockStmt;\n" +
            "import com.github.javaparser.ast.visitor.VoidVisitorAdapter;\n" +
            "\n" +
            "public class MethodExtractorUtil {\n" +
            "\n" +
            "    public static void main(String[] args) throws IOException {\n" +
            "//        String filePath=\"F:\\\\jeecg-boot-master\\\\jeecg-boot-master\\\\jeecg-boot\\\\jeecg-boot-module-system\\\\src\\\\main\\\\java\\\\org\\\\jeecg\\\\modules\\\\system\\\\service\\\\impl\\\\SysDataSourceServiceImpl.java\";\n" +
            "//        File file = new File(filePath);\n" +
            "        String javaCode =\"\";\n" +
            "        CompilationUnit cu = StaticJavaParser.parse(file);\n" +
            "        //1.获取方法名和参数\n" +
            "        new MethodVisitor().visit(cu, null);\n" +
            "        //2.分割方法\n" +
            "        new MethodVisitor().methodCutter(cu,filePath);\n" +
            "    }\n" +
            "\n" +
            "    public static class MethodVisitor extends VoidVisitorAdapter<Void> {\n" +
            "        @Override\n" +
            "        public void visit(MethodDeclaration md, Void arg) {\n" +
            "            //获得方法名和参数\n" +
            "            System.out.println(\"Method name: \" + md.getName() + \" Parameters: \" + md.getParameters());\n" +
            "            super.visit(md, arg);\n" +
            "        }\n" +
            "\n" +
            "        public JSONArray methodCutter(CompilationUnit cu,String filePath) throws IOException {\n" +
            "            //获取方法并写入文件\n" +
            "            // 获取所有的方法\n" +
            "\n" +
            "            JSONArray jsonArray=new JSONArray();\n" +
            "            List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);\n" +
            "            // 遍历每个方法\n" +
            "            for (MethodDeclaration method : methods) {\n" +
            "                // 获取方法名\n" +
            "                String methodName = method.getNameAsString();\n" +
            "                // 获取方法体\n" +
            "                BlockStmt methodBody = method.getBody().orElse(null);\n" +
            "                if (methodBody == null) {\n" +
            "                    continue;\n" +
            "                }\n" +
            "                // 生成方法代码\n" +
            "                String methodCode = method.toString();\n" +
            "                JSONObject jsonObject=new JSONObject();\n" +
            "                jsonObject.put(\"filePath\",filePath);\n" +
            "                jsonObject.put(\"methodName\",methodName);\n" +
            "                jsonObject.put(\"methodLine\",method.getRange().get().begin.line);\n" +
            "                jsonObject.put(\"methodCode\",methodCode);\n" +
            "                jsonArray.add(jsonObject);\n" +
            "                // 将方法代码写入文件(每一个方法一个文件)\n" +
            "//                System.out.println(\"方法名：\"+methodName);\n" +
            "//                System.out.println(\"方法内容：\");\n" +
            "//                System.out.println(methodCode);\n" +
            "//                System.out.println();\n" +
            "\n" +
            "//                FileWriter fw = new FileWriter(methodName + \".java\");\n" +
            "//                fw.write(methodCode);\n" +
            "//                fw.close();\n" +
            "            }\n" +
            "            return jsonArray;\n" +
            "        }\n" +
            "    }\n" +
            "}\n";
}
