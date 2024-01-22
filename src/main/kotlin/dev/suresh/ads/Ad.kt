package dev.suresh.ads

import kotlin.math.abs


data class Ad(val id: String, val landingPageUrl: String, val duration: Int?, val width: Int, val height: Int, val links: List<Link>?){
//    private val log = LoggerFactory.getLogger(Ad::class.java)

    fun fitsIn(ppvSlot: PpvSlot): Boolean {
        if (dimensionsAreNotValid(ppvSlot)) {
//            log.error("Unable to calculate aspect ratio for dimensions. Ad($width, $height) Slot(${ppvSlot.width}, ${ppvSlot.height}")
            return false
        }
        val slotAspectRatio: Double = ppvSlot.width.toDouble() / ppvSlot.height.toDouble()
        val adAspectRatio: Double = width.toDouble() / height.toDouble()
        return abs(slotAspectRatio - adAspectRatio) <= slotAspectRatio * Companion.ASPECT_RATIO_TOLERANCE
    }

    private fun dimensionsAreNotValid(ppvSlot: PpvSlot) = ppvSlot.width <= 0 || ppvSlot.height <= 0 || width <= 0 || height <= 0

    companion object {
        private const val ASPECT_RATIO_TOLERANCE = 0.1
    }
}
