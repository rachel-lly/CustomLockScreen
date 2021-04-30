package com.example.customlockscreen.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.RESULT_CODE
import com.example.customlockscreen.activity.SORT_NOTE_TEXT
import com.example.customlockscreen.adapter.SortNoteAdapter
import com.example.customlockscreen.adapter.SortNoteListAdapter
import com.example.customlockscreen.databinding.FragmentNoteListBinding
import com.example.customlockscreen.databinding.FragmentNoteSortBinding
import com.example.customlockscreen.model.SortNote

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoteSortFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteSortFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    private lateinit var binding: FragmentNoteSortBinding
    private var list = ArrayList<SortNote>()

    private lateinit var onClickListener: SortNoteListAdapter.ClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }

        binding = FragmentNoteSortBinding.inflate(LayoutInflater.from(this.context))

        list.add(SortNote("全部",resources.getResourceEntryName(R.mipmap.all_color)))
        list.add(SortNote("纪念日",resources.getResourceEntryName(R.mipmap.anniverity_color)))
        list.add(SortNote("工作",resources.getResourceEntryName(R.mipmap.work_color)))
        list.add(SortNote("生活",resources.getResourceEntryName(R.mipmap.life_color)))

        onClickListener =object: SortNoteListAdapter.ClickListener{
            override fun onClick(sortNoteName: String) {
                // TODO: 2021/4/30 获取点击的分类本
//                var intent = Intent()
//                intent.putExtra(SORT_NOTE_TEXT,sortNoteName)
//                setResult(RESULT_CODE,intent)
//                finish()
            }

        }
        
        
        var adapter = context?.let { SortNoteListAdapter(it,list,onClickListener) }
        binding.fragmentSortNoteRecycleview.adapter = adapter
        binding.fragmentSortNoteRecycleview.layoutManager = GridLayoutManager(context,1)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NoteSortFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoteSortFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}