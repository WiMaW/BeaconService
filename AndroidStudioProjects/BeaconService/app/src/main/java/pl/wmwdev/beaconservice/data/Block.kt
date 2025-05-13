package pl.wmwdev.beaconservice.data

sealed class Block (
    open val height: Int,
    open val width: Int,
    open val color: Int,
    open var content: String
)

data class Block1 (
    override var content: String = ""
) : Block(height = 350, width = 660, color = 0xFFF3D7E1.toInt(), content = content)

data class Block2 (
    override var content: String = ""
) : Block(height = 350, width = 320, color = 0xFFF3D7E1.toInt(), content = content)

data class Block3 (
    override var content: String = ""
) : Block(height = 80, width = 660, color = 0xFFEAE4D5.toInt(), content = content)