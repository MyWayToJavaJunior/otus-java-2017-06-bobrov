package com.lwerl.web.helper;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Created by lWeRl on 04.09.2017.
 */
public class TemplateHelper {

    private static final Configuration configuration = new Configuration();
    private static final String SUFFIX = ".ftl";

    static {
        try {
            configuration.setDirectoryForTemplateLoading(
                    new File(TemplateHelper.class.getClassLoader().getResource("template").getFile())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TemplateHelper() {
    }

    public static String getPage(String name, Map<String, Object> data) {
        try (Writer stream = new StringWriter()) {
            Template template = configuration.getTemplate(name + SUFFIX);
            template.process(data, stream);
            return stream.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
