package com.example.mesapp.exeptionHandler;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.Writer;

class MyTemplateExceptionHandler implements TemplateExceptionHandler {
    @Override
    public void handleTemplateException(TemplateException te, freemarker.core.Environment env, Writer out) throws TemplateException {
        try {
            out.write("[ERROR: " + te.getMessage() + "]");
        } catch (IOException e) {
            throw new TemplateException("Failed to print error message. Cause: " + e, env);
        }
    }
}
