#include "sw3d.h"

static jfieldID nativeIdField(JNIEnv *env, jobject owner) {
    jclass clazz = (*env)->GetObjectClass(env, owner);
    return (*env)->GetFieldID(env, clazz, "nativeid", "J");
}

jlong nativeIdOf(JNIEnv *env, jobject owner) {
    if (owner == NULL) {
        return 0;
    }

    jfieldID field = nativeIdField(env, owner);
    return field == NULL ? 0 : (*env)->GetLongField(env, owner, field);
}

void setNativeId(JNIEnv *env, jobject owner, jlong value) {
    if (owner == NULL) {
        return;
    }

    jfieldID field = nativeIdField(env, owner);
    if (field != NULL) {
        (*env)->SetLongField(env, owner, field, value);
    }
}
