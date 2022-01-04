//#include <iostream>
#include <string.h>
#include <stdlib.h>
#include "jni.h"
#include <jvmti.h>
#include <jni_md.h>

// 加密方法
void encode(char *str) {
    unsigned int len = strlen(str);
    for (int i = 0; i < len; i ++) {
        str[i] = str[i] ^ 0x07;
    }
}

JNIEXPORT jbyteArray JNICALL Java_com_wyhw_plugin_JarEncryptUtil_encrypt(JNIEnv * env, jclass cls, jbyteArray bytes) {
    char* dst = (char *) ((*env) -> GetByteArrayElements(env, bytes, 0));
    encode(dst);
    (*env) -> SetByteArrayRegion(env, bytes, 0, strlen(dst), (jbyte *) dst);
    return bytes;
}

// class 文件解密
void JNICALL ClassDecryptHook(jvmtiEnv *jvmti_env, JNIEnv* jni_env, jclass class_being_redefined, jobject loader, const char* name,
      jobject protection_domain, jint class_data_len, const unsigned char* class_data, jint* new_class_data_len, unsigned char** new_class_data) {

	//printf("ClassDecryptHook Files: %s\n", (char *) name);
    *new_class_data_len = class_data_len;
    (*jvmti_env) -> Allocate(jvmti_env, class_data_len, new_class_data);
    unsigned char* _data = *new_class_data;
	if (name && strncmp(name, "com/zk/sdk", 10) == 0 && strstr(name, "$$") == NULL) {
		//printf("\n\nhit: %s\n", (char *) name);
		//printf("len=%d\n\n", class_data_len);
		for (int i = 0; i < class_data_len; ++i) {
			_data[i] = class_data[i];
		}
		encode(_data);
	} else {
		for (int i = 0; i < class_data_len; ++i) {
			_data[i] = class_data[i];
		}
	}
}

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *vm, char *options, void *reserved) {
    jvmtiEnv *jvmtiEnv;
    jint ret = (*vm) -> GetEnv(vm, (void **)&jvmtiEnv, JVMTI_VERSION);
    if (JNI_OK != ret) {
        printf("ERROR: Fail get jvmtiEvn!\n");
        return ret;
    }
    jvmtiCapabilities jvmtiCapabilities;
    (void) memset(&jvmtiCapabilities, 0, sizeof(jvmtiCapabilities));
    jvmtiCapabilities.can_generate_all_class_hook_events = 1;
    jvmtiCapabilities.can_tag_objects = 1;
    jvmtiCapabilities.can_generate_object_free_events = 1;
    jvmtiCapabilities.can_get_source_file_name = 1;
    jvmtiCapabilities.can_get_line_numbers = 1;
    jvmtiCapabilities.can_generate_vm_object_alloc_events = 1;

    jvmtiError error = (*jvmtiEnv) -> AddCapabilities(jvmtiEnv, &jvmtiCapabilities);
    if (JVMTI_ERROR_NONE != error) {
        printf("ERROR: Unable to AddCapabilities JVMTI!\n");
        return error;
    }

    jvmtiEventCallbacks callbacks;
    (void) memset(&callbacks, 0, sizeof(callbacks));

    callbacks.ClassFileLoadHook = &ClassDecryptHook;
    error = (*jvmtiEnv) -> SetEventCallbacks(jvmtiEnv, &callbacks, sizeof(callbacks));
    if (JVMTI_ERROR_NONE != error) {
        printf("ERROR: Fail setEventCallBacks!\n");
        return error;
    }

    error = (*jvmtiEnv) -> SetEventNotificationMode(jvmtiEnv, JVMTI_ENABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
    if (JVMTI_ERROR_NONE != error) {
        printf("ERROR: Fail SetEventNotificationMode!\n");
        return error;
    }
    return JNI_OK;
}
