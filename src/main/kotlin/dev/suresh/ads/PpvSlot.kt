package dev.suresh.ads

data class PpvSlot(
    val id: String,
    val screenIds: List<String>,
    val width: Int,
    val height: Int
) {
    companion object
}
