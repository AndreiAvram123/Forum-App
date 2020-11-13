package com.andrei.kit.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SocialMediaViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val pages  = 3
    override fun getItemCount(): Int  = pages


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatsFragment()
            1 ->ReceivedFriendRequestsFragment.getInstance()
            else -> SentFriendRequestsFragment.getInstance()
        }
    }


}