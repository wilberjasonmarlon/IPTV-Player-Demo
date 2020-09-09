package cu.wilb3r.iptvplayerdemo.data

data class M3UItem (
    val mDuration: Int,
    val mStreamURL: String,
    val mLogoURL: String,
    val mGroupTitle: String,
    val mChannel: String
) {
}