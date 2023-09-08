package org.jeecg.modules.system.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodExtractor {

    public static void main(String[] args) throws IOException {

        File file = new File("D:\\INSOFTLAB\\JavaRuleDetectorV1.3\\testfile\\tfName.java");
        CompilationUnit cu = StaticJavaParser.parse(file);
        //1.获取方法名和参数
        new MethodVisitor().visit(cu, null);
        //2.分割方法到文件
        new MethodVisitor().methodCutter(cu);

    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            //获得方法名和参数
//            System.out.println("Method name: " + md.getName() + " Parameters: " + md.getParameters());
            super.visit(md, arg);
        }

        public void methodCutter(CompilationUnit cu) throws IOException {
            //获取方法并写入文件
            // 获取所有的方法
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
                // 将方法代码写入文件(每一个方法一个文件)
                FileWriter fw = new FileWriter(methodName + ".java");
                fw.write(methodCode);
                fw.close();
            }
        }
    }
}
