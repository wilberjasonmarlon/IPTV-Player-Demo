package cu.wilb3r.iptvplayerdemo.data

object M3UParser {
    private const val TAG_PLAYLIST_HEADER = "#EXTM3U"
    private const val TAG_METADATA = "#EXTINF:"
    private const val TAG_PLAYLIST_NAME = "#PLAYLIST"
    private const val ATTR_LOGO = "tvg-logo"
    private const val ATTR_GROUP_TITLE = "group-title"


    //#EXTM3U
    //#EXTINF:-1 tvg-logo="https://i.imgur.com/62H9un9.png" group-title="+ IPTV Playlist",+ IPTV Playlist
    //https://10.10.12.15:8080/asdasd

    fun parse(_string: String): M3UItems? {
        val playlist = M3UItems("")
        var lines = _string.split(TAG_METADATA.toRegex()).toTypedArray()
        for (_line in lines) {
            if (!_line.contains(TAG_PLAYLIST_HEADER)) {
                //meta + url
                var entry = _line.split("\n".toRegex()).toTypedArray()
                if(entry.size > 1){
                    var meta = entry[0]
                    val cache = StringBuilder()

                    while (meta.isNotEmpty()) {
                        val char = meta[0]
                        if (char.isDigit()|| char == '-' || char == '+') {
                            cache.append(char)
                            meta = meta.substring(1)
                        } else {
                            break
                        }
                    }

                    var duration = cache.toString().toIntOrNull()
                    meta = meta.trim()
                    var logo = ""
                    if (meta.contains(ATTR_LOGO)) {
                        val start = meta.indexOf(ATTR_LOGO) + ATTR_LOGO.length + 2
                        var end = meta.substring(start)
                        logo = end.substring(0, end.indexOf("\"")).trim()

                    }
                    var groupTitle = ""
                    if (meta.contains(ATTR_GROUP_TITLE)) {
                        val start = meta.indexOf(ATTR_GROUP_TITLE) + ATTR_GROUP_TITLE.length + 2
                        var end = meta.substring(start)
                        groupTitle = end.substring(0, end.indexOf("\"")).trim()
                    }

                    var title = meta.substring(meta.indexOfLast { it == ',' } + 1)
                    playlist.m3UItems.add(M3UItem(duration!!, entry[1].trim(), logo, groupTitle, title))
                } else {
                    playlist.m3UItems.add(M3UItem(0, entry[0].trim(), "", "No title", ""))
                }
            }
        }
        return playlist
    }


}