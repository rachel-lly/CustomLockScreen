package com.example.customlockscreen.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.*
import com.example.customlockscreen.adapter.LabelGridAdapter
import com.example.customlockscreen.adapter.LabelLinearAdapter
import com.example.customlockscreen.databinding.FragmentNoteListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val LIST_STATE = "LIST_STATE"



/**
 * A simple [Fragment] subclass.
 * Use the [NoteListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var state:String?=null
    private lateinit var labelList :ArrayList<Label>

    private var isFirst:Boolean = true

    private lateinit var binding: FragmentNoteListBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentNoteListBinding.inflate(LayoutInflater.from(this.context))

        setHasOptionsMenu(true)



        labelList= ArrayList<Label>()
        labelList.add(Label("星期六",8,"2020-08-04"))
        labelList.add(Label("星期五",9,"2021-01-04"))
        labelList.add(Label("距离考试",18,"2021-05-04"))
        labelList.add(Label("哈哈哈哈哈哈",118,"2020-11-30"))

        binding.homeRecyclerview.adapter = this.context?.let { LabelLinearAdapter(it, labelList) }
        binding.homeRecyclerview.layoutManager = GridLayoutManager(this.context, 1)



        arguments?.let {
            state = it.getString(LIST_STATE)

        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_list_fragment_toolbar,menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO: 2021/4/16 菜单选择控制
        when(item.itemId){
            R.id.grid_note -> {

                if(isFirst){


                    binding.homeRecyclerview.layoutManager = GridLayoutManager(this.context, 2)
                    binding.homeRecyclerview.adapter = this.context?.let { LabelGridAdapter(it,labelList) }

                    binding.headerLayout.visibility = View.GONE
                    isFirst = false

                }else{

                    binding.homeRecyclerview.layoutManager = GridLayoutManager(this.context, 1)
                    binding.homeRecyclerview.adapter = this.context?.let { LabelLinearAdapter(it,labelList) }
                    binding.headerLayout.visibility = View.VISIBLE

                    isFirst = true
                }

            }

            R.id.add_note ->{
                val intent = Intent(context, AddNoteActivity::class.java)
                context?.startActivity(intent)
            }
        }

        return false
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NoteListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(listState:String) =
            NoteListFragment().apply {
                arguments = Bundle().apply {
                    putString(listState,state)
                }

            }


    }

}




