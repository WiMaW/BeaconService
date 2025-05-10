package pl.wmwdev.beaconservice.data

sealed class Block (
    open val height: Int,
    open val width: Int,
    open val color: Int,
    open var content: String
)

data class Block1 (
    override var content: String = ""
) : Block(height = 250, width = 660, color = 0xFFF3D7E1.toInt(), content = content)

data class Block2 (
    override var content: String = ""
) : Block(height = 250, width = 300, color = 0xFFF3D7E1.toInt(), content = content)