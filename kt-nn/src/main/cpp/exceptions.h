#ifndef POCKETNN_CORE_H
#define POCKETNN_CORE_H

//
// Created by viveret steele (viveret.amant@gmail.com) on 1/23/19.
//

#include <jni.h>
#include "tiny_dnn/util/nn_error.h"

inline jint throwNoClassDefError( JNIEnv *env, const char *message )
{
    jclass exClass;
    const char *className = "java/lang/NoClassDefFoundError";

    exClass = env->FindClass(className);
    if (exClass == nullptr) {
        return throwNoClassDefError( env, className );
    }

    return env->ThrowNew( exClass, message );
}


inline jint throwOutOfMemoryError( JNIEnv *env, const char *message )
{
    jclass exClass;
    const char *className = "java/lang/OutOfMemoryError" ;

    exClass = env->FindClass( className );
    if ( exClass == nullptr) {
        return throwNoClassDefError( env, className );
    }

    return env->ThrowNew( exClass, message );
}

inline jint throwNoSuchMethodError(
        JNIEnv *env, char *className, char *methodName, char *signature )
{

    jclass exClass;
    const char *exClassName = "java/lang/NoSuchMethodError" ;
    char* msgBuf;
    jint retCode;
    size_t nMallocSize;

    exClass = env->FindClass( exClassName );
    if ( exClass == nullptr) {
        return throwNoClassDefError( env, exClassName );
    }

    nMallocSize = strlen(className)
                  + strlen(methodName)
                  + strlen(signature) + 8;

    msgBuf = (char*) malloc( nMallocSize );
    if ( msgBuf == nullptr) {
        return throwOutOfMemoryError
                ( env, "throwNoSuchMethodError: allocating msgBuf" );
    }
    memset( msgBuf, 0, nMallocSize );

    strcpy( msgBuf, className );
    strcat( msgBuf, "." );
    strcat( msgBuf, methodName );
    strcat( msgBuf, "." );
    strcat( msgBuf, signature );

    retCode = env->ThrowNew( exClass, msgBuf );
    free ( msgBuf );
    return retCode;
}

inline jint throwNoSuchFieldError( JNIEnv *env, char *message )
{
    jclass exClass;
    const char *className = "java/lang/NoSuchFieldError" ;

    exClass = env->FindClass( className );
    if (exClass == nullptr) {
        return throwNoClassDefError( env, className );
    }

    return env->ThrowNew( exClass, message );
}

inline jint throwNNError( JNIEnv *env, char *message )
{
    jclass exClass;
    const char *className = "com/viveret/tinydnn/error/NNException" ;

    exClass = env->FindClass( className );
    if (exClass == nullptr) {
        return throwNoClassDefError( env, className );
    }

    return env->ThrowNew( exClass, message );
}

template <typename TFunc, typename TRet> bool jniTryCatch(JNIEnv* env, TFunc fn, TRet& ret) {
    try
    {
        ret = fn();
        return true;
    }
    catch (const tiny_dnn::nn_error& e)
    {
        jclass jc = env->FindClass("com/viveret/tinydnn/error/NNException");
        if(jc) env->ThrowNew (jc, e.what());
        /* if null => NoClassDefFoundError already thrown */
    }
    catch (const std::bad_alloc& e)
    {
        /* OOM exception */
        jclass jc = env->FindClass("java/lang/OutOfMemoryError");
        if(jc) env->ThrowNew (jc, e.what());
    }
    catch (const std::ios_base::failure& e)
    {
        /* IO exception */
        jclass jc = env->FindClass("java/io/IOException");
        if(jc) env->ThrowNew (jc, e.what());
    }
    catch (const std::exception& e)
    {
        /* unknown exception */
        jclass jc = env->FindClass("java/lang/Error");
        if(jc) env->ThrowNew (jc, e.what());
    }
    catch (...)
    {
        /* Oops I missed identifying this exception! */
        jclass jc = env->FindClass("java/lang/Error");
        if(jc) env->ThrowNew (jc, "unexpected exception");
    }

    return false;
}

#endif //POCKETNN_CORE_H
