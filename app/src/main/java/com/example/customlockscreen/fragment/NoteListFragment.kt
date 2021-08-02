package com.example.customlockscreen.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.*
import com.example.customlockscreen.Util.SharedPreferenceCommission
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
import java.text.SimpleDateFormat
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

    private val format = SimpleDateFormat("yyyy-MM-dd-EE", Locale.CHINESE)

    val targetTimeComparator = kotlin.Comparator { o1:Label, o2:Label ->
        return@Comparator o1.targetDate.compareTo(o2.targetDate)
    }

    val addTimeComparator = kotlin.Comparator { o1:Label, o2:Label ->
        return@Comparator o1.addNoteTime.compareTo(o2.addNoteTime)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        
        binding = FragmentNoteListBinding.inflate(LayoutInflater.from(this.context))

        refreshRoomLabelListDay()

        setHasOptionsMenu(true)

        labelList = ArrayList()


        val style by SharedPreferenceCommission(context!!,"sortStyle","按事件时间")

        dataViewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        dataViewModel.getAllLabelsByObserve().observe(this,{
            labelList = it

            if(labelList.size == 0){
                binding.listNullLogo.visibility = View.VISIBLE
            }else{
                binding.listNullLogo.visibility = View.GONE
            }

            if(style.equals("按事件时间")){
                Collections.sort(labelList,targetTimeComparator)
            }else{
                Collections.sort(labelList, addTimeComparator)
            }

            refreshTopLabel()
            refreshList()

        })

        if(style.equals("按事件时间")){
            Collections.sort(labelList,targetTimeComparator)
        }else{
            Collections.sort(labelList, addTimeComparator)
        }

        labelLinearAdapter = this.context?.let { LabelLinearAdapter(it, labelList,false) }!!
        labelGridAdapter = this.context?.let { LabelGridAdapter(it, labelList) }!!


        binding.homeRecyclerview.adapter = labelLinearAdapter
        binding.homeRecyclerview.layoutManager = GridLayoutManager(this.context, 1)

        labelLinearAdapter.notifyDataSetChanged()
        labelGridAdapter.notifyDataSetChanged()

        binding.homeSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.homeSwipeRefreshLayout.setOnRefreshListener {
            refreshList()
            binding.homeSwipeRefreshLayout.isRefreshing = false
        }

        refreshTopLabel()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_list_fragment_toolbar,menu)
    }


    @RequiresApi(Build.VERSION_CODES.M)
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

            R.id.sort_by_event_time_home , R.id.sort_by_add_time_home->{

                val sortStyle :String = item.title.toString()

                EventBus.getDefault().post(sortStyle.let { MessageEvent(it) })

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

    @RequiresApi(Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent:MessageEvent){

        val msg = messageEvent.msg

        var isChange = false

        when(msg){

            "按添加时间" ->{
                isChange = true
                Collections.sort(labelList, addTimeComparator)
            }

            "按事件时间" ->{
                isChange = true
                Collections.sort(labelList,targetTimeComparator)

            }
            "全部" ->{
                labelList = labelDao.getAllLabels()
            }


            else ->{
                labelList = labelDao.getSameSortNoteLabelList(msg)
            }
        }

        if(!isChange){
            val sortStyle by SharedPreferenceCommission(context!!,"sortStyle","按事件时间")

            if(sortStyle.equals("按添加时间")){
                Collections.sort(labelList, addTimeComparator)
            }else{
                Collections.sort(labelList,targetTimeComparator)
            }

        }
        refreshTopLabel()
        refreshList()
    }

    fun refreshList(){
        labelLinearAdapter.labelList = labelList
        labelLinearAdapter.notifyDataSetChanged()

        labelGridAdapter.labelList = labelList
        labelGridAdapter.notifyDataSetChanged()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    fun refreshTopLabel(){

        val topEventName by SharedPreferenceCommission(context!!,"topLabelName","-1")

        if(topEventName.equals("-1")){
            defaultTopLabel()
        }else{
            val label: Label? = topEventName.let { labelDao.getLabelByName(it) }
            if(label!=null){
                setTopLabel(label)
            }else{
                defaultTopLabel()
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun defaultTopLabel(){

        if(!labelList.isEmpty()){

            val label = labelList.get(0)
            setTopLabel(label)
        }else{
            binding.homeHeaderTopText.text = "--"
            binding.homeHeaderTopDate.text = "----- --"
            binding.homeHeaderTopDay.text = "--"
        }
    }

    fun setTopLabel(label: Label){
        binding.homeHeaderTopText.text = label.text
        binding.homeHeaderTopDate.text = format.format(label.targetDate)

        val day = label.day

        binding.homeHeaderTopDay.text = Math.abs(day).toString()
        if(day>=0){
            binding.homeHeaderTopDateJustText.text = "目标："
        }else{
            binding.homeHeaderTopDateJustText.text = "开始："
        }
    }

    fun refreshRoomLabelListDay(){
        val allLabel : List<Label> = labelDao.getAllLabels()
        val nowTime: Long = System.currentTimeMillis()
        for(i in 0..allLabel.size - 1 ){
            val refreshLabel = allLabel.get(i)

            if(refreshLabel.endDate<=nowTime){

                labelDao.deleteLabel(refreshLabel)

            }else{

                refreshLabel.day =  (refreshLabel.targetDate-nowTime)/(1000*3600*24)
                labelDao.updateLabel(refreshLabel)

            }

        }
    }
}




