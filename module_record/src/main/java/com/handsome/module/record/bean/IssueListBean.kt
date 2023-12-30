package com.handsome.module.record.bean

import java.io.Serializable

data class IssueListBean(
    val issue_list: IssueList,
    val status_code: Int,
    val status_msg: String
) : Serializable {
    data class IssueList(
        val age: Int,
        val body_info: BodyInfo?,
        val disease_relation: DiseaseRelation?,
        val gender: Boolean,
        val username: String
    )  : Serializable{
        data class BodyInfo(
            val blood_pressure: String,
            val blood_sugar: String,
            val heart_rate: String,
            val height: String,
            val update_time: Long,
            val weight: String
        ) : Serializable

        data class DiseaseRelation(
            val disease_introduction: String,
            val family_diseases: String,
            val history_diseases: List<HistoryDisease>?
        )  : Serializable{
            data class HistoryDisease(
                val department: String,
                val medicines: List<String>,
                val symptom: String,
                val update_time: Long
            ) : Serializable
        }
    }
}