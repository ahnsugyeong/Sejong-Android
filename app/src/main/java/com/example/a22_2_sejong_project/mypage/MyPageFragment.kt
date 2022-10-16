package com.example.a22_2_sejong_project.mypage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a22_2_sejong_project.DTO.UserDTO
import com.example.a22_2_sejong_project.databinding.FragmentMyPageBinding
import com.example.a22_2_sejong_project.login.SejongLoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    var auth : FirebaseAuth? = null
    var db : FirebaseFirestore? = null
    var userId : String? = null// 다른 사람 or 내가 선택한 uid
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userId = arguments?.getString("uid")

        if (userId == auth?.currentUser?.uid) {
            myInfo(auth?.currentUser?.uid)
        } else {
            othersInfo(userId)
        }



        // 로그아웃
        binding.logout.setOnClickListener {
            Toast.makeText(context,"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context,SejongLoginActivity::class.java))
        }
        getProfileImage()
        return binding.root
    }

    private fun getProfileImage() {
        db?.collection("profileImages")?.document(userId!!)?.addSnapshotListener { value, error ->
            if (value == null) {
                return@addSnapshotListener
            } else {
                val url = value.data!!["image"]
                activity?.apply {
                    Glide.with(requireContext()).load(url).apply(RequestOptions().circleCrop()).into(binding.mypageProfileImage)

                }
            }
        }
    }

    private fun othersInfo(uid: String?) {
        Log.d("TAG","다른 유저 정보")

    }

    private fun myInfo(uid: String?) {
        Log.d("TAG","내 정보")
        db?.collection("user")?.document(uid!!)?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {
                val userDTO = it.result.toObject(UserDTO::class.java)
                binding.mypageNicknameTv.text = userDTO?.nickname
                binding.mypageStdidTv.text = userDTO?.stdId
                binding.mypageNameTv.text = userDTO?.name
                binding.mypageCollegeTv.text = userDTO?.college
                binding.mypageMajorTv.text = userDTO?.major
                if (userDTO?.note == null) {
                    binding.mypageNoteTv.text = "본인만의 개성있는 소개를 남겨주세요 :)"
                } else {
                    binding.mypageNoteTv.text = userDTO?.note
                }
                if (userDTO?.profileUrl != null) {
                    Glide.with(requireContext()).load(userDTO.profileUrl).into(binding.mypageProfileImage)
                }
            }
        }
        binding.mypageProfileImage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            activity?.startActivityForResult(photoPickerIntent,0)
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
//            binding.mypageProfileImage.setImageURI(data?.data)
//            var map = mutableMapOf<String, Uri>()
//            map["profileUrl"] = data?.data!!
//            db?.collection("user")?.document(auth?.currentUser?.uid!!)?.update(map as Map<String, Any>)?.addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Log.d("TAG","성공")
//                }
//            }
//        }
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}