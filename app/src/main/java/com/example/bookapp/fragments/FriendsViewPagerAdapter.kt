package com.example.bookapp.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FriendsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val PAGES = 2
    override fun getItemCount(): Int {
        return PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatsFragment()
            else -> FriendRequestFragment()
        }
    }


}