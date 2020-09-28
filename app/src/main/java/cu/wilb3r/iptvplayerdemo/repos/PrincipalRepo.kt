package cu.wilb3r.iptvplayerdemo.repos

import android.content.Context
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cu.wilb3r.iptvplayerdemo.BuildConfig
import cu.wilb3r.iptvplayerdemo.data.*
import cu.wilb3r.iptvplayerdemo.db.M3U
import cu.wilb3r.iptvplayerdemo.db.M3UDao
import cu.wilb3r.iptvplayerdemo.utils.Coroutines
import cu.wilb3r.iptvplayerdemo.utils.Event
import cu.wilb3r.iptvplayerdemo.utils.initKtorClient
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.flow.collect
import java.io.*
import java.util.*
import javax.inject.Inject

class PrincipalRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val m3UDao: M3UDao
)  {

    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "lista.m3u")
    val uri = context.let {
        FileProvider
            .getUriForFile(it, "${BuildConfig.APPLICATION_ID}.provider", file)
    }

    private val _liveData = MutableLiveData<M3UItems>()
    val liveData: LiveData<M3UItems> get() = _liveData

    private val _liveError = MutableLiveData<Event<String>>()
    val liveError: LiveData<Event<String>> get() = _liveError

    fun download(urlFile: String) {
        if(!file.exists())
            file.createNewFile()
        Coroutines.io {
            initKtorClient().download(file, urlFile).collect { result ->
                Coroutines.main {
                    when (result) {
                        is Result.Success -> {
                            BufferedReader(InputStreamReader(FileInputStream(file)))
                                .readText().also {
                                    _liveData.postValue(M3UParser.parse(it))
                                }
                        }
                        is Result.Error -> {
                            _liveError.postValue(Event(result.message))
                            BufferedReader(InputStreamReader(context.assets.open("iptvsample")))
                                .readText().also {
                                    _liveData.postValue(M3UParser.parse(it))
                                }
                        }
                    }
                }
            }
        }
    }

    suspend fun insertItem(item: M3U){
        m3UDao.inserM3U(item)
    }
    fun inputStringToString(inputStream: InputStream): String? {
        return try {
            inputStream.bufferedReader().use(BufferedReader::readText)
        } catch (e: NoSuchElementException) {
            ""
        }
    }


}