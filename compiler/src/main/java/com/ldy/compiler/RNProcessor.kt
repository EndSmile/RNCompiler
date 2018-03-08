package com.ldy.compiler

import com.grosner.kpoet.*
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReactSyncHook
import com.squareup.javapoet.ParameterizedTypeName
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.collections.HashSet

/**
 * Created by ldy on 2017/5/11.
 */

class RNProcessor : AbstractProcessor() {

    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer
    private lateinit var messager: Messager

    @Synchronized override fun init(env: ProcessingEnvironment) {
        super.init(env)
        elementUtils = env.elementUtils
        filer = env.filer
        messager = env.messager

    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        roundEnvironment.getElementsAnnotatedWith(ReactMethod::class.java)
                .plus(roundEnvironment.getElementsAnnotatedWith(ReactSyncHook::class.java))
                .groupBy { it.enclosingElement as TypeElement }
                .forEach { classElement, annotationList ->
                    val packageName = elementUtils.getPackageOf(classElement).qualifiedName.toString()
                    val reactMethodList = annotationList.filter { it.getAnnotation(ReactMethod::class.java) != null }
                    val reactSyncHookList = annotationList.filter { it.getAnnotation(ReactSyncHook::class.java) != null }
                    javaFile(packageName){
                        `class`(classElement.simpleName.toString()+"\$\$Assist") {
                            modifiers(public, final)

                            `public static final field`(ParameterizedTypeName.get(Set::class.java,String::class.java), "reactMethodSet"){
                                javadoc("Don't modify it,otherwise will throw exception")
                            }
                            `public static final field`(ParameterizedTypeName.get(Set::class.java,String::class.java), "reactSyncHookSet"){
                                javadoc("Don't modify it,otherwise will throw exception")
                            }

                            `static block`{
                                statement("\$T<\$T> set1 = new \$T<\$T>(${reactMethodList.size});",Set::class.java,String::class.java,HashSet::class.java,String::class.java)
                                reactMethodList
                                        .map { it.simpleName.toString() }
                                        .forEach { statement("set1.add(${it.S})") }
                                statement("reactMethodSet = \$T.unmodifiableSet(set1)",Collections::class.java)

                                add("\n")

                                statement("\$T<\$T> set2 = new \$T<\$T>(${reactSyncHookList.size});",Set::class.java,String::class.java,HashSet::class.java,String::class.java)
                                reactSyncHookList
                                        .map { it.simpleName.toString() }
                                        .forEach { statement("set2.add(${it.S})") }
                                statement("reactSyncHookSet = \$T.unmodifiableSet(set2)",Collections::class.java)
                            }

                        }
                    }.writeTo(filer)
                }

        return true
    }


    override fun getSupportedAnnotationTypes(): Set<String>
            = hashSetOf(ReactMethod::class.java.canonicalName, ReactSyncHook::class.java.canonicalName)


    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

}
