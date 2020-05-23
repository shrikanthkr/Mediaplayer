package com.em.app


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.em.app.activities.BaseActivity
import com.em.ui.albums.AlbumsFragment
import com.em.ui.artist.ArtistsFragment
import com.em.ui.search.SearchFragment
import com.em.ui.songs.SongsFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
@FlowPreview
class ViewPagerAdapter(activity: BaseActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                SearchFragment.newInstance(position)
            }
            1 -> {
                SongsFragment.newInstance(position)
            }
            2 -> {
                AlbumsFragment.newInstance(position)
            }
            3 -> {
                ArtistsFragment.newInstance(position)
            }
            else -> error("Unknown adapter position")
        }
    }
}