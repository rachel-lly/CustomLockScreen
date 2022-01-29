package com.example.customlockscreen.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.view.activity.AddSortNoteActivity
import com.example.customlockscreen.adapter.SortNoteListAdapter
import com.example.customlockscreen.databinding.FragmentNoteSortBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.model.db.DataViewModel
import com.example.customlockscreen.model.db.LabelDao
import com.example.customlockscreen.model.db.SortNoteDao
import com.example.customlockscreen.util.SharedPreferenceCommission
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NoteSortFragment : Fragment() {


    private lateinit var binding: FragmentNoteSortBinding
    private lateinit var list:List<SortNote>
    private lateinit var labelList:List<Label>

    private lateinit var adapter:SortNoteListAdapter

    private lateinit var dataViewModel: DataViewModel

    private lateinit var labelDao: LabelDao
    private lateinit var sortNoteDao: SortNoteDao

    private lateinit var deleteListener:SortNoteListAdapter.deleteOnClickListener

    companion object {

        private val fragment = NoteSortFragment()

        @JvmStatic
        fun newInstance() = fragment

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)


        sortNoteDao = DataBase.dataBase.sortNoteDao()
        labelDao = DataBase.dataBase.labelDao()

        list = ArrayList()
        labelList = ArrayList()



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.sort_note_fragment_toolbar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       //新建分类本
        when(item.itemId){

            R.id.add_sort_note ->{
                val intent = Intent(context, AddSortNoteActivity::class.java)
                context?.startActivity(intent)
            }
        }

        return false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.fragment_note_sort,container,false)

        deleteListener = object : SortNoteListAdapter.deleteOnClickListener {
            override fun delete(sortNote: SortNote) {
                MaterialAlertDialogBuilder(context!!)
                    .setTitle("提醒")
                    .setMessage("确定删除这个分类本?")
                    .setPositiveButton(context!!.resources.getString(R.string.accept)) { _, _ ->
                        if(labelDao.getSameSortNoteLabelList(sortNote.name).isNotEmpty()){
                            context!!.toast("该分类本下有事件，删除失败")
                        }else{
                            sortNoteDao.deleteSortNote(sortNote)
                            context!!.toast("删除分类本成功")
                        }
                    }
                    .show()
            }
        }

        adapter = context?.let { SortNoteListAdapter(it,list,labelList,deleteListener) }!!
        binding.fragmentSortNoteRecycleview.adapter = adapter
        binding.fragmentSortNoteRecycleview.layoutManager = GridLayoutManager(context,1)

        var isFirst by SharedPreferenceCommission(requireContext(), "isFirst", true)

        if(isFirst){
            sortNoteDao.insertSortNote(SortNote("生活",resources.getResourceEntryName(R.mipmap.cat)))
            sortNoteDao.insertSortNote(SortNote("纪念日",resources.getResourceEntryName(R.mipmap.anniverity_color)))
            sortNoteDao.insertSortNote(SortNote("学习",resources.getResourceEntryName(R.mipmap.cactus)))
        }

        dataViewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        dataViewModel.getAllSortNotesByObserve().observe(viewLifecycleOwner,{
            adapter.sortNoteList = it
            adapter.notifyDataSetChanged()
            if(isFirst){
                isFirst = false
            }
        })

        dataViewModel.getAllLabelsByObserve().observe(viewLifecycleOwner,{
            adapter.labelList = it
            adapter.notifyDataSetChanged()
        })

        binding.sortNoteListSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.sortNoteListSwipeRefreshLayout.setOnRefreshListener {
            adapter.notifyDataSetChanged()
            binding.sortNoteListSwipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }


}