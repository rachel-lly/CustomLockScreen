package com.example.customlockscreen.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.*
import com.example.customlockscreen.activity.AddNoteActivity
import com.example.customlockscreen.adapter.LabelGridAdapter
import com.example.customlockscreen.adapter.LabelLinearAdapter
import com.example.customlockscreen.databinding.FragmentNoteListBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.bean.MessageEvent
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.model.db.DataViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList


class NoteListFragment : Fragment() {

    private lateinit var labelList : List<Label>

    private lateinit var dataViewModel: DataViewModel

    private val labelDao = DataBase.dataBase.labelDao()

    private var isFirst:Boolean = true

    private lateinit var binding: FragmentNoteListBinding

    private lateinit var labelLinearAdapter:LabelLinearAdapter

    private lateinit var labelGridAdapter:LabelGridAdapter

    private lateinit var sharedPreferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        
        binding = FragmentNoteListBinding.inflate(LayoutInflater.from(this.context))

        setHasOptionsMenu(true)

        sharedPreferences = this.context!!.getSharedPreferences("LABEL_EVENT", Context.MODE_PRIVATE)

        val style = sharedPreferences.getString("sortStyle","按事件时间")

        dataViewModel = ViewModelProvider(this).get(DataViewModel::class.java)



        dataViewModel.getAllLabelsByObserve().observe(this,{
            labelList = it

            if(style.equals("按事件时间")){

                Collections.sort(labelList, kotlin.Comparator { o1, o2 ->
                    return@Comparator o1.targetDate.compareTo(o2.targetDate)
                })

            }else{

                Collections.sort(labelList, kotlin.Comparator { o1, o2 ->
                    return@Comparator o1.addNoteTime.compareTo(o2.addNoteTime)
                })

            }

            labelLinearAdapter.labelList = labelList
            labelGridAdapter.labelList = labelList
            labelLinearAdapter.notifyDataSetChanged()
            labelGridAdapter.notifyDataSetChanged()
        })

        labelList = ArrayList()

        if(style.equals("按事件时间")){
            Collections.sort(labelList, kotlin.Comparator { o1, o2 ->
                return@Comparator o1.targetDate.compareTo(o2.targetDate)
            })
        }else{
            Collections.sort(labelList, kotlin.Comparator { o1, o2 ->
                return@Comparator o1.addNoteTime.compareTo(o2.addNoteTime)
            })
        }

        labelLinearAdapter = this.context?.let { LabelLinearAdapter(it, labelList,false) }!!
        labelGridAdapter = this.context?.let { LabelGridAdapter(it, labelList) }!!


        binding.homeRecyclerview.adapter = labelLinearAdapter
        binding.homeRecyclerview.layoutManager = GridLayoutManager(this.context, 1)

        labelLinearAdapter.notifyDataSetChanged()
        labelGridAdapter.notifyDataSetChanged()

        binding.homeSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.homeSwipeRefreshLayout.setOnRefreshListener {

            labelLinearAdapter.labelList = labelList
            labelLinearAdapter.notifyDataSetChanged()


            labelGridAdapter.labelList = labelList
            labelGridAdapter.notifyDataSetChanged()

            binding.homeSwipeRefreshLayout.isRefreshing = false
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_list_fragment_toolbar,menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.grid_note -> {

                if(isFirst){


                    binding.homeRecyclerview.layoutManager = GridLayoutManager(this.context, 2)

                    labelGridAdapter.labelList = labelList

                    binding.homeRecyclerview.adapter = labelGridAdapter

                    binding.headerLayout.visibility = View.GONE
                    isFirst = false

                }else{

                    binding.homeRecyclerview.layoutManager = GridLayoutManager(this.context, 1)

                    labelLinearAdapter.labelList = labelList

                    binding.homeRecyclerview.adapter = labelLinearAdapter
                    binding.headerLayout.visibility = View.VISIBLE

                    isFirst = true
                }

            }

            R.id.add_note ->{
                val intent = Intent(context, AddNoteActivity::class.java)
                context?.startActivity(intent)
            }
        }

        return true
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        EventBus.getDefault().register(this)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent:MessageEvent){

        val msg = messageEvent.msg

        var isChange = false

        when(msg){

            "按添加时间" ->{
                isChange = true
                labelList = labelDao.getAllLabels()
                Collections.sort(labelList, kotlin.Comparator { o1, o2 ->
                    return@Comparator o1.addNoteTime.compareTo(o2.addNoteTime)
                })
            }

            "按事件时间" ->{
                isChange = true
                labelList = labelDao.getAllLabels()

                Collections.sort(labelList, kotlin.Comparator { o1, o2 ->
                    return@Comparator o1.targetDate.compareTo(o2.targetDate)
                })

            }
            "全部" ->{
                labelList = labelDao.getAllLabels()
            }


            else ->{
                labelList = labelDao.getSameSortNoteLabelList(msg)
            }
        }

        if(!isChange){
            val sortStyle = sharedPreferences.getString("sortStyle","按添加时间")

            if(sortStyle.equals("按添加时间")){
                Collections.sort(labelList, kotlin.Comparator { o1, o2 ->
                    return@Comparator o1.addNoteTime.compareTo(o2.addNoteTime)
                })
            }else{
                Collections.sort(labelList, kotlin.Comparator { o1, o2 ->
                    return@Comparator o1.targetDate.compareTo(o2.targetDate)
                })
            }

        }



        labelLinearAdapter.labelList = labelList
        labelLinearAdapter.notifyDataSetChanged()

        labelGridAdapter.labelList = labelList
        labelGridAdapter.notifyDataSetChanged()
    }
}




