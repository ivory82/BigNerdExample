package gs.im

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

const val MAX_EXPERIENCE: Int = 5000
const val TAVERN_NAME = "Evan's Folly"

val patronList = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf( "Ironfoot", "Fernsworth", "Baggins", "Istory", "Roose")
val uniquePatrons = mutableSetOf<String>()
val FILE_NAME = "tavern-menu-items.txt"

val patronGold = mutableMapOf<String, Double>()

class Dice(){
    val rolledValue
        get() = (1..6).shuffled().first()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main()
        sandbox()
        tavern()
        displayWallet()

    }

    private fun displayWallet() {
        var menuList: MutableList<String> = arrayListOf()

        try {
            menuList = assets.open(FILE_NAME).bufferedReader().use {
                it.readText().split("\r\n").toMutableList()
            }
        } catch (e: Exception) {
            println(e.toString())
        }

        (0..9).forEach {
            val first = patronList.shuffled().first()
            val last = lastName.shuffled().first()
            val name = "$first $last"
            uniquePatrons += name
        }
        uniquePatrons.forEach {
            patronGold[it] = 6.0
        }

        var orderCount = 0
        while (orderCount <= 9) {
            placeOrder(uniquePatrons.shuffled().first(), menuList.shuffled().first())
            orderCount++
        }

        displayPatronBalances()
    }

    fun performPurchase( price: Double, patronName: String ){
        val totalPurse = patronGold.getValue( patronName )
        patronGold[ patronName ] = totalPurse - price
    }

    private fun displayPatronBalances(){
        for( it in patronGold ){
            println("${it.key}, blance: ${"%.2f".format(it.value)}")
        }
    }

    private fun placeOrder( patronName: String, menuData: String ) {
        val indexOfApostrophe = TAVERN_NAME.indexOf('\'')
        val tavernMaster = TAVERN_NAME.substring( 0 until  indexOfApostrophe )
        println( "$patronName 은 $tavernMaster 에게 주문한다." )


        val ( type, name, price ) = menuData.split( ',' )
        val message = "$patronName 은 금화 $price 로 $name ($type)를 구입한다. "
        println( message )

        performPurchase( price.toDoubleOrNull() ?: 0.0, patronName )

        val phrase = if( name == "Dragon's Breath"){
            "$patronName 이 감탄한다.: ${toDragonSpeak("와, $name 진짜 좋구나!")}"
        } else{
            "$patronName 이 말한다. $name"
        }
        println( phrase )

    }

    private fun toDragonSpeak( phrase: String ) =
            phrase.replace( Regex("[aeiou]")){
                when( it.value){
                    "a"->"4"
                    "e"->"3"
                    "i"->"1"
                    "o"->"0"
                    "u"->"|_|"
                    else -> it.value
                }
            }

    fun tavern(){

        var beverage = readLine()
        println( "$beverage ")

        var swordsJuggling: Int? = null
        val isJugglingProficient = (1..3).shuffled().last() == 3

        if( isJugglingProficient ){
            swordsJuggling = 2
        }

        try{
            proficiencyCheck( swordsJuggling )
            swordsJuggling = swordsJuggling!!.plus( 1 )
        } catch ( e: Exception ){
            println( e )
        }

        println("$swordsJuggling 개의 칼로 저글링 합니다. ")
    }


    fun proficiencyCheck( swordsJuggling: Int? ){
        checkNotNull( swordsJuggling, {"플레이어가 저글링을 할수 없음"})
    }

    fun sandbox() {
        runSimulation()
    }

    fun runSimulation(){
        val greetingFunction = cfgGreetingFunction()
        println( greetingFunction("김선달") )
    }

    fun cfgGreetingFunction(): (String) -> String{
        val structureType = "병원"
        var numBuildings = 5
        return {
            playerName: String->
            val currentYear = 2019
            numBuildings += 1
            println( "$numBuildings 채의 $structureType 이 추가됨")
            "SimVillage 방문을 환영합니다, $playerName! (Copyright $currentYear)"

        }

    }

    fun main() {



        Game.play()
    }

    object Game{

        val player = Player( "Madrigal" )
        var currentRoom: Room = TownSquare()

        init{
            println("방문을 환영합니다.")
            player.castFireball()
        }

        fun play(){
            while (true){
                println( currentRoom.description())
                println( currentRoom.load() )

                printPlayerStatus( player )

                print("> 명령을 입력하세요: ")
                try{
                    println( "최근 명령: ${readLine()}")
                    println( GameInput(readLine()).processCommand())
                } catch ( e : Exception ){
                    e.toString()
                }

            }
        }

        private fun printPlayerStatus( player: Player) {
            println("(Aura: ${player.auraColor()} ) " + """(Bleesed: ${if (player.isBlessed) "YES" else "NO"})""")
            println("${player.name} ${player.formatHealthStatus()}")
        }

        private class GameInput( arg: String?){
            private val input = arg ?: ""
            val command = input.split(" ")[ 0 ]
            val argument =  input.split(" ").getOrElse(1, {""})

            fun processCommand() = when( command.toLowerCase()){
                "move"-> goDirecation()
                else -> commandNotFound()
            }

            private fun goDirecation() = "가자!"
            private fun commandNotFound() = "적합하지 않은 명령입니다."

        }
    }

}


















