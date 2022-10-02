package com.example.a22_2_sejong_project.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.a22_2_sejong_project.DTO.BoardContentDTO
import com.example.a22_2_sejong_project.MainActivity
import com.example.a22_2_sejong_project.R

import com.example.a22_2_sejong_project.databinding.FragmentBoardMainBinding
import com.example.a22_2_sejong_project.utils.AddBoardArticleFragment
import com.example.a22_2_sejong_project.utils.BoardDetailFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_board_main.*
import kotlinx.android.synthetic.main.fragment_board_main.view.*
import kotlinx.android.synthetic.main.item_board_main.*
import kotlinx.android.synthetic.main.item_board_main.view.*


class BoardFragment : Fragment() {
    var fragmentView: View? = null
    var firestore: FirebaseFirestore? = null
    var uid: String? = null
    var tapSelected: Int? = null
    var collectionPath: String? = null

    private var _binding: FragmentBoardMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardMainBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        binding.root.board_main_recyclerView.adapter = BoardRecyclerViewAdapter()
        binding.root.board_main_recyclerView.layoutManager = LinearLayoutManager(activity)

        binding.root.add_article_activity_button.setOnClickListener {
            (activity as MainActivity).replaceFragment(AddBoardArticleFragment())
        }

//        // 게시글 간 구분선 추가
//        val dividerItemDecoration =
//            DividerItemDecoration(binding.root.board_main_recyclerView.context, LinearLayoutManager(context).orientation)
//        binding.root.board_main_recyclerView.addItemDecoration(dividerItemDecoration)


        binding.root.board_tap.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tapSelected = tab!!.position
                when(tapSelected) {
                    0 -> collectionPath = "boardCapstoneContents"
                    1 -> collectionPath = "boardStudyContents"
                    2 -> collectionPath = "boardContestContents"
                    3 -> collectionPath = "boardMentorContents"
                }
            }
        })


        return binding.root
    }

    inner class BoardRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var boardContentDTOs: ArrayList<BoardContentDTO> = arrayListOf()
        var contentUIdList: ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("boardContents")?.orderBy("timestamp", Query.Direction.DESCENDING)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    boardContentDTOs.clear()
                    contentUIdList.clear()


                    // Sometimes, This code return null of querySnapshot when it signout
                    if (querySnapshot == null) return@addSnapshotListener

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(BoardContentDTO::class.java)
                        boardContentDTOs.add(item!!)
                        contentUIdList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context)
                .inflate(com.example.a22_2_sejong_project.R.layout.item_board_main, parent, false)

            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            // 간격 설정
//            val layoutParams = holder.itemView.layoutParams
//            layoutParams.height = "wrap_content"
//            holder.itemView.requestLayout()

            var viewholder = (holder as CustomViewHolder).itemView

            // title
            viewholder.item_board_main_title.text = boardContentDTOs!![position].title

            // description
            viewholder.item_board_main_description.text = boardContentDTOs!![position].description

            // timestamp
            viewholder.item_board_main_timestamp.text = boardContentDTOs!![position].timestamp

            // commentCount
            viewholder.item_board_main_commentNum.text =
                boardContentDTOs!![position].commentCount.toString()

            // favoriteCount
            viewholder.item_board_main_heartNum.text =
                boardContentDTOs!![position].favoriteCount.toString()

            // nickname
            // viewholder.item_board_main_userName.text = boardContentDTOs!![position].nickname

            // content type
            var tmp = boardContentDTOs!![position].contentType
            if (tmp == 1) viewholder.item_board_main_contentType.text = "모집"
            else if (tmp == 2) viewholder.item_board_main_contentType.text = "참여"
            else if (tmp == 3) viewholder.item_board_main_contentType.text = "기타"

            // headcount
            var totalHeadCount_tmp = boardContentDTOs!![position].totalHeadCount
            var currentHeadCount_tmp = boardContentDTOs!![position].currentHeadCount
            if (totalHeadCount_tmp == -1) viewholder.item_board_main_headCount.text = ""
            else viewholder.item_board_main_headCount.text =
                currentHeadCount_tmp.toString() + "/" + totalHeadCount_tmp.toString()


            // 게시물 클릭시
            viewholder.item_board_main_object.setOnClickListener { v ->
                (activity as MainActivity).replaceFragment(BoardDetailFragment())


//                activity?.finish()
//                var intent = Intent(v.context, BoardDetailActivity::class.java)
////                intent.putExtra("contentUid", contentUIdList[position])
////                intent.putExtra("destinationUid", boardContentDTOs[position].uid)
//                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return boardContentDTOs.size
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}