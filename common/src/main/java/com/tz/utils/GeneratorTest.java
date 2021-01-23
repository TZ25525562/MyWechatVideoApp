package com.tz.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneratorTest {
    public void testGenerator() throws Exception {
        List<String> warnings=new ArrayList<String>();
        //指向配置文件　　
        File configFile=new File("D:\\仿抖音小程序\\myvideoapp\\mini-api\\src\\main\\resources\\generatorConfig.xml");
        ConfigurationParser cp= new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,callback,warnings);
        myBatisGenerator.generate(null);
    }

    public static void main(String[] args)throws Exception {
        GeneratorTest generatorTest=new GeneratorTest();
        generatorTest.testGenerator();
    }
}
