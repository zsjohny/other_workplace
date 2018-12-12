package com.jiuy.pcweb.controller;

import com.squareup.javawriter.JavaWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("com.jiuy.pcweb.controller.MyAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)) {
            String objectType = element.getSimpleName().toString();

            MyAnnotation annotation = element.getAnnotation(MyAnnotation.class);


        }
        System.out.println("__________________________");


        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.jiuy.pcweb.test.GeneratedClass");


            Writer writer = source.openWriter();
            JavaWriter javaWriter = new JavaWriter(writer);
            javaWriter.
            writer.write("hello");
            writer.flush();
            writer.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }


        return true;
    }

//    private Types typeUtils;
//    private Elements elementUtils;
//    private Filer filer;
//    private Messager messager;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        typeUtils = processingEnv.getTypeUtils();
//        elementUtils = processingEnv.getElementUtils();
//        filer = processingEnv.getFiler();
//        messager = processingEnv.getMessager();
//    }

    private void error(Element e, String msg, Object... args) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                "小伙子", e);
    }
}

