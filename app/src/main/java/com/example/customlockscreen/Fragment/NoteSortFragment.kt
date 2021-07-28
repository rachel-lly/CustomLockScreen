package com.example.customlockscreen.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.AddSortNoteActivity
import com.example.customlockscreen.adapter.SortNoteListAdapter
import com.example.customlockscreen.databinding.FragmentNoteSortBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataViewModel


class NoteSortFragment : Fragment() {


    private lateinit var binding: FragmentNoteSortBinding
    private lateinit var list:List<SortNote>
    private lateinit var labelList:List<Label>

    private lateinit var adapter:SortNoteListAdapter

    private lateinit var dataViewModel: DataViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setHasOptionsMenu(true)

        binding = FragmentNoteSortBinding.inflate(LayoutInflater.from(this.context))

        list = ArrayList()
        labelList = ArrayList()

        adapter = context?.let { SortNoteListAdapter(it,list,labelList) }!!
        binding.fragmentSortNoteRecycleview.adapter = adapter
        binding.fragmentSortNoteRecycleview.layoutManager = GridLayoutManager(context,1)


        dataViewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        dataViewModel.getAllSortNotesByObserve().observe(this,{
            adapter.sortNoteList = it
            adapter.notifyDataSetChanged()
        })

        dataViewModel.getAllLabelsByObserve().observe(this,{
            adapter.labelList = it
            adapter.notifyDataSetChanged()
        })

        binding.sortNoteListSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.sortNoteListSwipeRefreshLayout.setOnRefreshListener {
            adapter.notifyDataSetChanged()
            binding.sortNoteListSwipeRefreshLayout.isRefreshing = false
        }


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
        return binding.root
    }


}