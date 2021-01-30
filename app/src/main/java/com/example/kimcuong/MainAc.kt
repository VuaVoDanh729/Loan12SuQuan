package com.example.kimcuong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.example.kimcuong.factory.GameFactory

class MainAc : AppCompatActivity() {
    /* instanceBoard là một matrix thứ 2 của sàn đấu, mục đích lưu các giá trị của các Item (trong Drawble). Việc sử dụng matrix này nhằm nhanh chóng lấy ra các ảnh Item để tiến hành đổi chỗ
       Chúng ta không đổi chỗ các ImageButton mà chỉ đổi chỗ ảnh của ImageButton. Do vậy 'instanceBoard' sẽ tham chiếu đến matrix 'boardGame' chứa các ImageButton
       (cụ thể là mang giá trị ảnh của ImageButton vị trí tương ứng)
    */
    lateinit var instanceBoard: Array<Array<Int>>

    // Tạo một matrix các ImageButton để lưu lại các ô đã được hiển thị trên màn hình. Giá trị Image này sẽ được tham chiếu và ràng buộc đến các biến trong 'instanceBoard'
    lateinit var boardGame: Array<Array<ImageButton>>

    lateinit var tableLayout: TableLayout

    val heightBoard: Int = 7
    val widthBoard: Int = 7

    // Sử dụng array này để lưu lại ID
    lateinit var arrTemp: Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main2)
        initGame()
    }

    fun restart(view: View) {
        var count = tableLayout.childCount
        tableLayout.removeAllViews()
        createBoard()
        loadData()
//        for (i in 0..count-1){
//            var child: View = tableLayout.getChildAt(i)
//            tableLayout.removeAllViews()
//        }
    }
    fun initGame() {
        createBoard()
        loadData()
        arrTemp = arrayOf(0, 0)
    }

    fun createBoard() {
        tableLayout = findViewById(R.id.board)
        boardGame = Array(heightBoard, {
            Array(widthBoard, {
                ImageButton(this)
            })
        })
    }

    /*
    *  Mỗi lần đổi chỗ 2 ô, phải click lần lượt từng ô một. biến imgBtnChoose sẽ lưu ô đầu tiên click vào
    * biến isSecondClick sẽ cho biết lần click đó là lần đầu hay làn 2
    */
    lateinit var imgBtnChose: ImageButton
    var isSecondClick: Boolean = false

    // Dùng để gán ID cho tất cả Image Button
    var buttonID: Int = 0

    fun loadData() {
        var factory: GameFactory = GameFactory()
        instanceBoard = factory.board
        for (i in 0..instanceBoard.size - 1) {
            var rows: TableRow = TableRow(this)
            var pr: TableRow.LayoutParams = TableRow.LayoutParams(140, 140)
            for (j in 0..instanceBoard.get(0).size - 1) {
                var imgV: ImageButton = ImageButton(this)
                imgV.id = buttonID
                buttonID++
                imgV.layoutParams = pr
                imgV.setImageResource(instanceBoard[i][j])
                imgV.setOnClickListener {
                    if (!isSecondClick) {
                        imgBtnChose = imgV
                        isSecondClick = true
                        // Khi chọn một ô bất kì, ô đó sẽ hiện viền màu trắng để đánh dấu là đã chọn
                        imgBtnChose.setBackgroundResource(R.drawable.choosen_chess)
                    } else {
                        changeImageButton(imgV, imgBtnChose)
                        imgBtnChose.setBackgroundResource(R.drawable.custom_buttom_vuong)
                        removeContiniousChess(imgV, imgBtnChose)
                        isSecondClick = false

                    }
                }
                boardGame[i][j] = imgV
                rows.addView(boardGame[i][j])
            }
            tableLayout.addView(rows)
        }
    }

    /*
    * check 2 ô xem có bên cạnh nhau không.
    * Dùng tránh việc người chơi không đổi chỗ 2 liền nhau
    */
    fun isNext(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        if (x1 == x2) {
            if (y1 - y2 == 1 || y2 - y1 == 1) {
                return true
            }
        }
        if (y1 == y2) {
            if (x1 - x2 == 1 || x2 - x1 == 1) {
                return true
            }
        }
        return false
    }

    // Vì ID của các ImageButton tăng từ 0 -> từ ID ta có thể lấy được vị trí của Button
    fun getPossButtonByID(id: Int): Array<Int> {
        var pointOf: Array<Int> = arrayOf(0, 0)
        var xImg1: Int = id / heightBoard
        var yImg1 = id % widthBoard
        pointOf[0] = xImg1
        pointOf[1] = yImg1
        return pointOf
    }

    fun changeImageButton(img1: ImageButton, img2: ImageButton) {
        var id1: Int = img1.id
        var id2: Int = img2.id

        var point1: Array<Int> = getPossButtonByID(id1)
        var point2: Array<Int> = getPossButtonByID(id2)
        var xImg1 = point1[0]
        var yImg1 = point1[1]
        var xImg2 = point2[0]
        var yImg2 = point2[1]

        if (!isNext(xImg1, yImg1, xImg2, yImg2)) {
            return;
        }

        var temp: Int = instanceBoard[xImg1][yImg1]
        instanceBoard[xImg1][yImg1] = instanceBoard[xImg2][yImg2]
        instanceBoard[xImg2][yImg2] = temp
        img1.setImageResource(instanceBoard[xImg1][yImg1])
        img2.setImageResource(instanceBoard[xImg2][yImg2])
    }


    fun removeContiniousChess(point1: ImageButton, point2: ImageButton) {
        deleteNode(point1)
        deleteNode(point2)
    }

    fun deleteNode(point1: ImageButton){
        var positionOfChess1: Array<Int> = getPossButtonByID(point1.id);
        var x1: Int = positionOfChess1[0]
        var y1: Int = positionOfChess1[1]
        var listverti: ArrayList<Int> = findByVertical(x1,y1)
        var listHori: ArrayList<Int> = findByHori(x1,y1)

        if(listverti.size>1){
            removeListChess(listverti)
            removeChessByID(point1.id)
        }
        if(listHori.size > 1){
            removeListChess(listHori)
            removeChessByID(point1.id)
        }
    }

    fun removeListChess(list: ArrayList<Int>){
        for (i in 0..list.size-1){
            removeChessByID(list.get(i))
        }
    }
    fun findByVertical(x1: Int, y1: Int) : ArrayList<Int>{
        var count: Int = 0
        var tempArr = ArrayList<Int>()
        if (x1 > 0) {
            for (i in (x1 - 1) downTo 0) {
                if (instanceBoard[i][y1] == instanceBoard[x1][y1]) {
                    count++
                    tempArr.add(boardGame[i][y1].id)
                } else break;
            }
            for (i in (x1 + 1)..widthBoard - 1) {
                if (instanceBoard[i][y1] == instanceBoard[x1][y1]) {
                    count++
                    tempArr.add(boardGame[i][y1].id)
                } else break;
            }
        } else if (x1 == 0){
            for (i in 1..widthBoard - 1) {
                if (instanceBoard[i][y1] == instanceBoard[x1][y1]) {
                    count++
                    tempArr.add(boardGame[i][y1].id)
                } else break;
            }
        }
        if(count>= 2){
            return tempArr;
        }
        return ArrayList(0)
    }

    fun findByHori(x1: Int, y1: Int): ArrayList<Int>{
        var count: Int = 0
        var tempArr = ArrayList<Int>()
        if (y1 > 0) {
            for (i in (y1 - 1) downTo 0) {
                if (instanceBoard[x1][1] == instanceBoard[x1][y1]) {
                    count++
                    tempArr.add(boardGame[x1][i].id)
                } else break;
            }
            for (i in (y1 + 1)..widthBoard - 1) {
                if (instanceBoard[x1][i] == instanceBoard[x1][y1]) {
                    count++
                    tempArr.add(boardGame[x1][i].id)
                } else break;
            }
        } else if (y1 == 0){
            for (i in 1..widthBoard - 1) {
                if (instanceBoard[x1][i] == instanceBoard[x1][y1]) {
                    count++
                    tempArr.add(boardGame[x1][i].id)
                } else break;
            }
        }
        if(count>= 2){
            return tempArr;
        }
        return ArrayList(0)
    }


    fun removeChessByID(id: Int){
        var positionOfChess1: Array<Int> = getPossButtonByID(id);
        var x1: Int = positionOfChess1[0]
        var y1: Int = positionOfChess1[1]
        instanceBoard[x1][y1] = 0;
        boardGame[x1][y1].setImageResource(R.color.black)
    }


    fun getScreen() {
        val displayMetrics = DisplayMetrics()
        val windowsManager = applicationContext.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceWidth = displayMetrics.widthPixels
        val deviceHeight = displayMetrics.heightPixels
    }




}