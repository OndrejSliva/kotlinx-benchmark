package dev.suresh.ads


data class PpvSuggestedAd(
    val id: String,
    val slotId: String,
    val duration: Int?,
    val landingPageUrl: String,
    var adm: String? = null
)  {
    companion object {
        fun forAd(ad: Ad, slotId: String): PpvSuggestedAd {
            return PpvSuggestedAd(
                id = ad.id,
                slotId = slotId,
                duration = ad.duration,
                landingPageUrl = ad.landingPageUrl
            )
        }
    }
}
