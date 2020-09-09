package cu.wilb3r.iptvplayerdemo.ui.vm

import androidx.lifecycle.ViewModel
import cu.wilb3r.iptvplayerdemo.repos.PrincipalRepo
import cu.wilb3r.iptvplayerdemo.utils.Coroutines
import kotlinx.coroutines.Job

class HomeViewModel(
    private val repo: PrincipalRepo
) : ViewModel() {
    private var job: Job? = null

    val playListLiveData = repo.liveData
    val errorLiveData = repo.liveError

    fun download(url: String) {

        job = Coroutines.io {
            repo.download(url)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}