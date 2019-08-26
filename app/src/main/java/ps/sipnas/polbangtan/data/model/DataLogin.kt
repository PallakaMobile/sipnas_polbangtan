package ps.sipnas.polbangtan.data.model

data class DataLogin(
        val accessToken: String? = "",
        val refreshToken: String? = "",
        val tokenType: String? = "",
        val expiresIn: Int? = 0
)
