package cu.wilb3r.iptvplayerdemo.data

data class M3UItems (
    var name: String,
    var m3UItems: ArrayList<M3UItem>
){
    constructor(name: String) : this(
        name = name,
        m3UItems = ArrayList<M3UItem>()
    )
}