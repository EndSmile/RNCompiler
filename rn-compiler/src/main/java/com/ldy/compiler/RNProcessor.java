package com.ldy.compiler;

import com.ldy.annotation.ReactMethod;
import com.ldy.annotation.ReactSyncHook;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by endsmile on 2018/3/8.
 */

public class RNProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
        filer = env.getFiler();
        messager = env.getMessager();

    }
    
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes(){
        return new HashSet<String>(){{
            add(ReactMethod.class.getCanonicalName());
            add(ReactSyncHook.class.getCanonicalName());
        }};
    }


    @Override
    public SourceVersion getSupportedSourceVersion(){
        return SourceVersion.latestSupported();
    }
}
