package com.github.andy.im.command.util;

import java.lang.reflect.*;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 16:39
 */
public class ReflectUtil {

    public static Class<?> find(Object object, Class<?> parameterizedSuperclass, String typeParamName) {
        Class thisClass = object.getClass();
        Class currentClass = thisClass;

        do {
            while(currentClass.getSuperclass() != parameterizedSuperclass) {
                currentClass = currentClass.getSuperclass();
                if(currentClass == null) {
                    return fail(thisClass, typeParamName);
                }
            }

            int typeParamIndex = -1;
            TypeVariable[] typeParams = currentClass.getSuperclass().getTypeParameters();

            for(int genericSuperType = 0; genericSuperType < typeParams.length; ++genericSuperType) {
                if(typeParamName.equals(typeParams[genericSuperType].getName())) {
                    typeParamIndex = genericSuperType;
                    break;
                }
            }

            if(typeParamIndex < 0) {
                throw new IllegalStateException("unknown type parameter \'" + typeParamName + "\': " + parameterizedSuperclass);
            }

            Type var11 = currentClass.getGenericSuperclass();
            if(!(var11 instanceof ParameterizedType)) {
                return Object.class;
            }

            Type[] actualTypeParams = ((ParameterizedType)var11).getActualTypeArguments();
            Type actualTypeParam = actualTypeParams[typeParamIndex];
            if(actualTypeParam instanceof ParameterizedType) {
                actualTypeParam = ((ParameterizedType)actualTypeParam).getRawType();
            }

            if(actualTypeParam instanceof Class) {
                return (Class)actualTypeParam;
            }

            if(actualTypeParam instanceof GenericArrayType) {
                Type v = ((GenericArrayType)actualTypeParam).getGenericComponentType();
                if(v instanceof ParameterizedType) {
                    v = ((ParameterizedType)v).getRawType();
                }

                if(v instanceof Class) {
                    return Array.newInstance((Class)v, 0).getClass();
                }
            }

            if(!(actualTypeParam instanceof TypeVariable)) {
                return fail(thisClass, typeParamName);
            }

            TypeVariable var12 = (TypeVariable)actualTypeParam;
            currentClass = thisClass;
            if(!(var12.getGenericDeclaration() instanceof Class)) {
                return Object.class;
            }

            parameterizedSuperclass = (Class)var12.getGenericDeclaration();
            typeParamName = var12.getName();
        } while(parameterizedSuperclass.isAssignableFrom(thisClass));

        return Object.class;
    }

    private static Class<?> fail(Class<?> type, String typeParamName) {
        throw new IllegalStateException("cannot determine the type of the type parameter \'" + typeParamName + "\': " + type);
    }
}
