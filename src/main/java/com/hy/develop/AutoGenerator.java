package com.hy.develop;

import com.hy.common.tool.Utils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class AutoGenerator {
    private VelocityEngine velocityEngine;
    private VelocityContext velocityContext;
    private ModuleInfo module;

    public AutoGenerator(String moduleName) {
        this(moduleName, null);
    }

    public AutoGenerator(String moduleName, String tableName) {
        velocityEngine = new VelocityEngine();
        Properties props = new Properties();
        props.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
        props.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        props.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        velocityEngine.init(props);

        initModuleInfo(moduleName, tableName);
        velocityContext = new VelocityContext();
        velocityContext.put("module", module);
    }

    private void initModuleInfo(String name, String tableName) {
        if (tableName == null) {
            module = new ModuleInfo(name);
        } else {
            module = new ModuleInfo(name, tableName);
        }
        DBHelper helper = new DBHelper();
        List<FieldInfo> fields = helper.getTableFields(module.getTableName());
        module.setTableFields(fields);
    }

    private void generateFiles() {
        generateEntityJava();
        generateMapperJava();
        generateMapperXml();
        generateServiceJava();
        generateServiceImplJava();
        generateControllerJava();
    }

    private void generateEntityJava() {
        String templateFile = "/templates/entity.java.vm";
        String outFile = Utils.ensureDirExist(module.getEntityDir()) + module.getCapitalName() + ".java";
        generateFile(templateFile, outFile);
    }

    private void generateMapperJava() {
        String templateFile = "/templates/mapper.java.vm";
        String outFile = Utils.ensureDirExist(module.getMapperJavaDir()) + module.getCapitalName() + "Mapper.java";
        generateFile(templateFile, outFile);
    }

    private void generateMapperXml() {
        String templateFile = "/templates/mapper.xml.vm";
        String outFile = Utils.ensureDirExist(module.getMapperXmlDir()) + module.getCapitalName() + "Mapper.xml";
        generateFile(templateFile, outFile);
    }

    private void generateServiceJava() {
        String templateFile = "/templates/service.java.vm";
        String outFile = Utils.ensureDirExist(module.getServiceDir()) + module.getCapitalName() + "Service.java";
        generateFile(templateFile, outFile);
    }

    private void generateServiceImplJava() {
        String templateFile = "/templates/service.impl.java.vm";
        String outFile = Utils.ensureDirExist(module.getServiceImplDir()) + module.getCapitalName() + "ServiceImpl.java";
        generateFile(templateFile, outFile);
    }

    private void generateControllerJava() {
        String templateFile = "/templates/controller.java.vm";
        String outFile = Utils.ensureDirExist(module.getControllerDir()) + module.getCapitalName() + "Controller.java";
        generateFile(templateFile, outFile);
    }

    private void generateFile(String templateFile, String outFile) {
        Template template = velocityEngine.getTemplate(templateFile);
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(outFile));
            BufferedWriter writer = new BufferedWriter(osw);
            template.merge(velocityContext, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(osw);
        }
    }

    public static void main(String[] args) {
//        AutoGenerator generator = new AutoGenerator("group", "t_eval_group");
//        generator.generateFiles();
    }
}
