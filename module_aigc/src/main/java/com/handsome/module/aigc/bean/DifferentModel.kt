package com.handsome.module.aigc.bean

enum class DifferentModel(val chEnValue: Pair<String,String>) {
    COMMON("普通" to "common"),
    COMMON_DISEASE("普通疾病" to "disease"),
    HEART_INNER_DISEASE("心内科" to "cardiology"),
    EYE_DISEASE("眼科" to "eye"),
    GIRL_DISEASE("妇产科" to "woman"),
    CANCER_DISEASE("癌症" to "cancer"),
    MEDICINE("药理学" to "medicine"),
    MEDICAL_DEVICES("医疗器械" to "device");

    fun getChineseName() : String{
        return chEnValue.first
    }

    fun getEnglishName() : String{
        return chEnValue.second
    }
}
