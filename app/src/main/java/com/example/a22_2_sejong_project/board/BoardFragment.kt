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
    var collectionPath: String = "boardCapstoneContents"

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
        val boardCategoryReturn = arguments?.getString("boardCategoryReturn")
        var position = 0
        if (boardCategoryReturn != null) {
            collectionPath = boardCategoryReturn
        }

        when (boardCategoryReturn) {
            "boardCapstoneContents" -> position = 0
            "boardStudyContents" -> position = 1
            "boardContestContents" -> position = 2
            "boardMentorContents" -> position = 3
            else -> 0
        }
        binding.root.board_tap.getTabAt(position)?.select()
        (binding.root.board_main_recyclerView.adapter as BoardRecyclerViewAdapter).makeInitialView()

        binding.root.add_article_activity_button.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("boardCategory", collectionPath)
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            val addBoardArticleFragment = AddBoardArticleFragment()
            addBoardArticleFragment.arguments = bundle
            transaction.replace(R.id.main_container_layout, addBoardArticleFragment)
                .commit()
        }


//        // 게시글 간 구분선 추가
//        val dividerItemDecoration =
//            DividerItemDecoration(binding.root.board_main_recyclerView.context, LinearLayoutManager(context).orientation)
//        binding.root.board_main_recyclerView.addItemDecoration(dividerItemDecoration)


        binding.root.board_tap.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tapSelected = tab!!.position
                when (tapSelected) {
                    0 -> collectionPath = "boardCapstoneContents"
                    1 -> collectionPath = "boardStudyContents"
                    2 -> collectionPath = "boardContestContents"
                    3 -> collectionPath = "boardMentorContents"
                    else -> collectionPath = "boardCapstoneContents"
                }
                (binding.root.board_main_recyclerView.adapter as BoardRecyclerViewAdapter).makeInitialView()
            }
        })



        return binding.root
    }

    inner class BoardRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var boardContentDTOs: ArrayList<BoardContentDTO> = arrayListOf()
        var contentUIdList: ArrayList<String> = arrayListOf()

        init {
            makeInitialView()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context)
                .inflate(com.example.a22_2_sejong_project.R.layout.item_board_main, parent, false)

            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHolder).itemView

            // userId
            viewholder.item_board_main_userName.text = boardContentDTOs!![position].nickname

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
                var fragment = BoardDetailFragment()
                var bundle = Bundle()

                bundle.putString("destinationContentUid", boardContentDTOs[position].contentId)
                bundle.putString("destinationUid", boardContentDTOs[position].uId)
                bundle.putString("userId", boardContentDTOs[position].userId)
                bundle.putString("boardCategory", collectionPath)

                fragment.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.main_container_layout,fragment).commit()
            }
        }

        override fun getItemCount(): Int {
            return boardContentDTOs.size
        }

        fun makeInitialView() {
            firestore?.collection(collectionPath)?.orderBy("timestamp", Query.Direction.DESCENDING)
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    boardContentDTOs.clear()
                    contentUIdList.clear()

                    if (querySnapshot == null) return@addSnapshotListener

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(BoardContentDTO::class.java)
                        boardContentDTOs.add(item!!)
                        contentUIdList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}