package cu.wilb3r.iptvplayerdemo.ui.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cu.wilb3r.iptvplayerdemo.data.M3UItem
import cu.wilb3r.iptvplayerdemo.db.M3U
import cu.wilb3r.iptvplayerdemo.repos.PrincipalRepo
import kotlinx.coroutines.launch

class HomeViewModel  @ViewModelInject constructor (
    val repo: PrincipalRepo
) : ViewModel() {

    val playListLiveData = repo.liveData
    val errorLiveData = repo.liveError

    fun download(url: String) = viewModelScope.launch {
        repo.download(url)
    }

    fun addToFavorite(item: M3UItem) = viewModelScope.launch {
        repo.insertItem(M3U(item.mDuration, item.mStreamURL, item.mLogoURL, item.mGroupTitle, item.mChannel))
    }


}