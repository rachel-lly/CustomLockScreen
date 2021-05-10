package com.example.customlockscreen.Fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.FragmentSettingBinding

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    
    private lateinit var binding:FragmentSettingBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
        
        binding = FragmentSettingBinding.inflate(layoutInflater)
        
        binding.settingSortLayout.setOnClickListener {
            showMenu(it,R.menu.fragment_setting_sort_style_menu)
        }
        
        binding.settingClockLayout.setOnClickListener {
            // TODO: 2021/5/9 提醒时间
        }
        
        binding.settingBackupDataLayout.setOnClickListener {
            // TODO: 2021/5/9 备份数据管理 
            
        }
        
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showMenu(v:View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context!!,v)
        popup.menuInflater.inflate(menuRes,popup.menu)
        popup.gravity = Gravity.RIGHT

        popup.setOnMenuItemClickListener (object :PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                var sortStyle = "按事件时间"
                when(menuItem?.itemId){
                    R.id.sort_by_add_time->{
                        sortStyle = "按添加时间"
                    }


                    R.id.sort_by_event_time->{
                        sortStyle =  "按事件时间"

                    }


                }

                binding.sortStyle.text = sortStyle
                popup.dismiss()

                return true
            }


        })


        popup.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SettingFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
                }
    }
}


