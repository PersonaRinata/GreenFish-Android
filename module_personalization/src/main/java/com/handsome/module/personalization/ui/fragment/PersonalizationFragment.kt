package com.handsome.module.personalization.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.handsome.lib.api.server.MAIN_PERSONALIZATION
import com.handsome.lib.util.base.BaseFragment
import com.handsome.module.personalization.databinding.PersonalizationFramentPersonalizationBinding

@Route(name = MAIN_PERSONALIZATION, path = MAIN_PERSONALIZATION)
class PersonalizationFragment : BaseFragment() {
    private val mBinding by lazy { PersonalizationFramentPersonalizationBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}