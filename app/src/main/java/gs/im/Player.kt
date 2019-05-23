package gs.im

class Player( _name: String, var healthPoints: Int = 100, val isBlessed: Boolean, private val isImmortal: Boolean)  {
    var name = _name
        get() = "${field.capitalize()} of $hometown"
        private set( value) {
            field = value.trim()
        }

    val hometown by lazy{ selectHometown() }

    var currentPosition = Coordinate( 0, 0 )

    init{
        require( healthPoints >0, {"healthPoints는 0보다 커야 합니다."})
        require( name.isNotBlank(), {"플레이어는 이름이 있어야 합니다."})
    }

    constructor( name: String) : this( name, isBlessed = true, isImmortal = false ) {
        if( name.toLowerCase()?.equals("kar") ){
            healthPoints = 40
        }
    }

//    Neversummer
//    Abelhaven
//    Phandoril
//    Tampa
//    Sanorith
//    Trell
//    Zan'tro
//    Hermi Hermi
//    Curlthistle Forest

    private fun selectHometown()
            = listOf("Neversummer", "Abelhaven", "Phandoril", "Tampa", "Sanorith", "Trell", "Zan'tro", "Hermi Hermi", "Curlthistle Forest" )
            .shuffled()
            .first()

    fun castFireball( numFireballs: Int = 2 ) =
            println( "한 덩어리의 파이어볼이 나타난다. (x$numFireballs)")

    fun auraColor(): String {
        val auraVisible = isBlessed && healthPoints > 50 || isImmortal
        val auraColor = if (auraVisible) "GREEN" else "NONE"
        return auraColor
    }

    fun formatHealthStatus() =
        when (healthPoints) {
            100 -> " 최상의 상태임!"
            in 90..99 -> " 최상의 상태임!"
            in 75..89 ->
                if (isBlessed) {
                    " 경미한 상처지만 빨리 치유됨"
                } else {
                    " 경미한 상처"
                }
            in 15..74 -> " 많이 다침"
            else -> "최악"
        }

}