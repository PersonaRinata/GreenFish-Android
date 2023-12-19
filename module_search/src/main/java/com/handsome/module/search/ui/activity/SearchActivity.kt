package com.handsome.module.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.handsome.lib.util.adapter.FragmentVpAdapter
import com.handsome.lib.util.base.BaseActivity
import com.handsome.lib.util.extention.gone
import com.handsome.lib.util.extention.visible
import com.handsome.lib.util.util.gsonSaveToSp
import com.handsome.lib.util.util.objectFromSp
import com.handsome.module.search.R
import com.handsome.module.search.databinding.SearchActivitySearchBinding
import com.handsome.module.search.databinding.SearchTabClassBinding
import com.handsome.module.search.ui.adapter.HistoryAdapter
import com.handsome.module.search.ui.fragment.FollowFragment
import com.handsome.module.search.ui.fragment.MessagePageFragment
import com.handsome.module.search.ui.fragment.VideoFlowFragment
import com.handsome.module.search.ui.viewmodel.SearchActivityViewModel

class SearchActivity : BaseActivity() {
    private val mBinding by lazy { SearchActivitySearchBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<SearchActivityViewModel>()
    private lateinit var mAdapter : HistoryAdapter
    private val historyList by lazy { LinkedHashSet<String>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initBack()
        initObserver()
        initHistory()
        initEditText()
        initVp()
        initTabLayout()
        initSearch()
    }

    private fun initBack() {
        val backCallBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mBinding.searchActivitySearchLinearHistory.visibility == View.GONE) {
                    showHistory()
                }else{
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallBack)
    }

    private fun initEditText() {
        mBinding.searchActivitySearchEtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initObserver() {
        mViewModel.searchContent.collectLaunch {
            if (!it.isNullOrEmpty()){
                historyList.add(it)
                setHistoryList()
            }
        }
    }

    private fun initHistory() {
        initAdapter()
        initRv()
        initData()
    }

    private fun initData() {
        val lastHistory = objectFromSp<LinkedHashSet<String>>(this.javaClass.name)
        if (!lastHistory.isNullOrEmpty()){
            historyList.addAll(lastHistory)
            setHistoryList()
        }
    }

    private fun setHistoryList(){
        mAdapter.submitList(historyList.toList())
    }

    private fun initRv() {
        val manager = FlexboxLayoutManager(this)
        manager.apply {
            // 附轴对齐方式 我这里让他沿着轴从头到尾排
            alignItems = AlignItems.FLEX_START
        }
        mBinding.searchActivitySearchRvHistory.layoutManager = manager
        mBinding.searchActivitySearchRvHistory.adapter = mAdapter
    }

    private fun initAdapter() {
        mAdapter = HistoryAdapter().apply {
            setOnClickItem {content ->
                mBinding.searchActivitySearchEtSearch.setText(content)
                sendSearchEvent(content)
            }
        }
    }

    private fun initSearch() {
        mBinding.searchActivitySearchTvSearch.setOnClickListener {
            doSearch()
        }
    }

    private fun doSearch(){
        val text = mBinding.searchActivitySearchEtSearch.text.toString()
        sendSearchEvent(text)
    }

    private fun sendSearchEvent(content : String){
        showResult()
        mViewModel.setSearchContent(content)
    }

    private fun showResult(){
        mBinding.searchActivitySearchLinearHistory.gone()
        mBinding.searchActivitySearchLinearResult.visible()
    }

    private fun showHistory(){
        mBinding.searchActivitySearchLinearResult.gone()
        mBinding.searchActivitySearchLinearHistory.visible()
    }

    private fun initVp() {
        val fragmentVpAdapter = FragmentVpAdapter(this)
        fragmentVpAdapter.add { VideoFlowFragment.newInstance() }
        fragmentVpAdapter.add { MessagePageFragment.newInstance() }
        fragmentVpAdapter.add { FollowFragment.newInstance() }
        mBinding.searchActivitySearchVp.adapter = fragmentVpAdapter
        mBinding.searchActivitySearchVp.isUserInputEnabled = false
        mBinding.searchActivitySearchVp.offscreenPageLimit = 1
    }

    private fun initTabLayout() {
        // 取消tabLayout的长按点击事件
        with(mBinding.searchActivitySearchTab) {
            isLongClickable = false
            // api 26 以上 设置空text
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.O) {
                tooltipText = ""
            }

            val titles = listOf(SEARCH_VIDEO, SEARCH_MESSAGE, SEARCH_USER)
            TabLayoutMediator(this, mBinding.searchActivitySearchVp) { tab, position ->
                val binding = SearchTabClassBinding.inflate(layoutInflater)
                binding.findTabMessageTvLabel.text = titles[position]
                tab.customView = binding.root
            }.attach()
            setTabColor(getTabAt(0), R.color.search_tab_select_text_color)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    setTabColor(tab,R.color.search_tab_select_text_color)
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    setTabColor(tab,R.color.search_tab_no_select_text_color)
                }
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun setTabColor(tab: TabLayout.Tab?,@ColorRes id:Int){
        val textView = tab?.customView?.findViewById<TextView>(R.id.find_tab_message_tv_label)
        textView?.setTextColor(resources.getColor(id,null))
    }

    override fun onDestroy() {
        super.onDestroy()
        gsonSaveToSp(historyList,this.javaClass.name)
    }

    companion object{
        const val SEARCH_VIDEO = "视频"
        const val SEARCH_MESSAGE = "资讯"
        const val SEARCH_USER = "用户"
        fun startAction(context: Context){
            val intent = Intent(context,SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}