package ps.sipnas.polbangtan.data.model

class DataImageSPJ {
    val data: List<Data>? = null

    data class Data(
            val imageUrl: String? = "",
            val id: String? = "",
            val ket: String? = "",
            val type: String? = "",
            val tglTrm: String? = "",
            val waktuTrm: String? = "",
            val rincian: String? = ""
    )
}
