package dev.suresh.ads

import kotlin.math.abs

private const val ASPECT_RATIO_TOLERANCE = 0.1
//private val log = LoggerFactory.getLogger(Ad::class.java)

fun Ad.fitsIn(ppvSlot: PpvSlot): Boolean {

    if (dimensionsAreNotValid(ppvSlot)) {
//        log.error("Unable to calculate aspect ratio for dimensions. Ad($width, $height) Slot(${ppvSlot.width}, ${ppvSlot.height}")
        return false
    }

    val slotAspectRatio: Double = ppvSlot.width.toDouble() / ppvSlot.height.toDouble()
    val adAspectRatio: Double = width.toDouble() / height.toDouble()

    return abs(slotAspectRatio - adAspectRatio) <= slotAspectRatio * ASPECT_RATIO_TOLERANCE
}

private fun Ad.dimensionsAreNotValid(ppvSlot: PpvSlot) = ppvSlot.width <= 0 || ppvSlot.height <= 0 || width <= 0 || height <= 0

fun List<Ad>.adsFor(links: List<Link>?): List<Ad> {
    return links?.flatMap { link -> this.filter { it.id == link.linkedAdId } } ?: emptyList()
}

