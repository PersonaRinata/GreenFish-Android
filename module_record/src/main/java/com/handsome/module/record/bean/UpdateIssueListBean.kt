package com.handsome.module.record.bean

import java.io.Serializable

data class UpdateIssueListBean(
    val issueList: IssueListBean.IssueList
) : Serializable