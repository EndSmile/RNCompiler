package com.ldy.compiler

import com.grosner.kpoet.CodeMethod
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeSpec
import kotlin.reflect.KClass

/**
 * Created by endsmile on 2018/3/7.
 */
val KClass<*>.className: ClassName
    get() = ClassName.get(this.java)

fun TypeSpec.Builder.`static block`(codeMethod: CodeMethod)=addStaticBlock(codeMethod(CodeBlock.builder()).build())