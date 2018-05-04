package BirdGame;
/**
 * 1.找对象定义属性
 * 2.为4个类 BirdGame,Ground,Colunm,Bird分别添加构造方法，初始化属性变量
 * 3.设置窗口框架：编写BirdGame类中的main方法，设置窗口大小，居中，设置关闭按钮，和设置窗口可见
 * 4.绘制界面：在BirdGame类中重写print（）方法
 * 5.写类的运动方法step();在birdgame类中写action方法,在main方法中启用action方法
 * 6.实现鼠标事件控制小鸟能点击上升
 * 7.实现记分,画分
 * 8.写鸟类的碰撞方法检测(鸟碰地面和鸟碰撞柱子）显示游戏结束
 * 9.实现游戏状态：开始，结束，重新开始
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;//java.awt 包含用于创建用户界面和绘制图形图像的所有类。
import java.io.IOException;
import java.text.BreakIterator;
import java.util.Random;


import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;//BirdGame 继承 Jpanel 屏面






public class BirdGame extends JPanel {
    Bird bird;
    Ground ground;
    Column column1,column2;//有上下两根柱子
    BufferedImage background;
    int score;//记分

    /**游戏状态*/
// boolean gameOver;
    int state;
    public static final int START = 0;
    public static final int RUNNING = 1; //1
    public static final int GAME_OVER = 2; //2

    BufferedImage startImage;
    BufferedImage gameOverImage;


    /**为该类添加构造方法，初始化属性变量*/
    public BirdGame() throws Exception{
        state = START;
// gameOver=false;
        startImage = ImageIO.read(getClass().getResource("../img/start.png"));
        gameOverImage=ImageIO.read(getClass().getResource("../img/gameover.png"));
        score = 0;
        bird = new Bird();
        ground = new Ground();
        column1 = new Column(1);
        column2 = new Column(2);
        background = ImageIO.read(getClass().getResource("../img/bg.png"));
    }

    /**重写print()方法用于为对象类贴图上去*/
    public void paint(Graphics g){ //paint：画图，Graphics:图示符
        g.drawImage(background,0,0,null);//用graphics绘制的draw的方法绘制时，默认绘制起点坐标为0，0
        g.drawImage(column1.image,column1.x-column1.width/2,column1.y-column1.heigth/2,null);
        g.drawImage(column2.image,column2.x-column2.width/2,column2.y-column2.heigth/2,null);
        g.drawImage(ground.image,ground.x,ground.y,null);
        g.drawImage(bird.image, bird.x-bird.width/2,bird.y-bird.heigth/2,null);

// Graphics2D g2=(Graphics2D) g;
// g2.rotate(-bird.alpha,bird.x,bird.y);
// g2.rotate(bird.alpha,bird.x,bird.y);

//在paint方法中绘制分数算法
        Font f = new Font(Font.SANS_SERIF,Font.BOLD,20);//设置字体，粗细，大小
        g.setColor(Color.WHITE);
        g.setFont(f);
        g.drawString("score："+score,20,50);//画分数

//添加游戏结束状态代码
// if (gameOver) {
// g.drawImage(gameOverImage, 0,0,null);
// g.drawImage(gameOverImage, 0,0,null);
// }
        switch (state) {
            case START:
                g.drawImage(startImage,42,120,null);
                break;
            case GAME_OVER:
                g.drawImage(gameOverImage,42,120,null);
                break;
        }

    }

    /**启动软件的方法main */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();//画游戏窗口的类
        BirdGame game = new BirdGame();
        frame.add(game);
        frame.setSize(288,512); //之前是 440, 670
        frame.setLocationRelativeTo(null);//框架.设置 位置 相对应的 设置窗体初始位置(居中)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置 默认的 关闭 操作
        frame.setVisible(true);//设置 可见的

        game.action();
    }

    /**在BirdGame中添加Action方法（运动方法）*/
    public void  action() throws Exception{
        MouseAdapter l = new MouseAdapter(){ //鼠标注册事件监听
            public void mousePressed(MouseEvent e){ //定义鼠标按下时执行的方法
                bird.flappy();
                try{
                    switch (state) {
                        case GAME_OVER:
                            column1 = new Column(1);
                            column2 = new Column(2);
                            bird = new Bird();
                            score = 0;
                            state=START;
                            break;
                        case START:
                            state=RUNNING;
                        case RUNNING:
                            bird.flappy();
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        };
        addMouseListener(l);//将L挂接到当前面板（game）上




        while(true){
// if(!gameOver){
// ground.step();
// column1.step();
// column2.step();
// bird.step();
// }
// if (bird.hit(ground) || bird.hit(column1) || bird.hit(column2)) {
// gameOver=true;
// }
//
// //记分逻辑
// if(bird.x==column1.x || bird.x == column2.x){
// score++;
// }
            switch (state) {
                case START:
                    ground.step();
                    break;
                case RUNNING:
                    column1.step();
                    column2.step();
                    ground.step();
                    bird.step();
                    bird.hitTest(column1);
//记分
                    if(bird.x == column1.x || bird.x == column2.x){
                        score++;
                    }
                    if (bird.hit(ground) || bird.hit(column1) || bird.hit(column2)) {
                        state = GAME_OVER;
                    }
                    break;
            }

            repaint();//重画
            Thread.sleep(1000/80);
        }
    }



}




/**对象：地面Ground*/
class Ground{
    /**定义属性*/
    BufferedImage image;//该image属性是地面Gound类贴图
    int x,y;
    int width;
    int heigth;
    /**为该类添加构造方法，初始化属性*/
    public Ground() throws Exception{
        image = ImageIO.read(getClass().getResource("../img/ground.png"));
        width = image.getWidth();
        heigth = image.getHeight();
        x = 0;
        y = 374; //500  背景离地面的距离
    }
    /**地面类的移动方法*/
    public void step(){
        --x;
        if ( x == -288 ) { //288为两倍屏幕长度 滚一圈 重合
            x = 0; //复原操作 使背景重回初始状态
        }
    }

}




/**对象：柱子Column*/
class Column {
    /**定义属性*/
    BufferedImage image;//柱子Column 贴图
    int x,y;
    int width,heigth;
    int gap;//两柱子间隙（上下）
    int distance;//两柱子距离（左右）
    /**为该类添加构造方法，初始化属性变量*/
    Random random = new Random();
    public Column(int n) throws Exception{ //为Column类编写带参数的构造方法。构造器：初始化数据，n代表第几个柱子
        image=ImageIO.read(getClass().getResource("../img/column.png"));
        width = image.getWidth();
        heigth = image.getHeight();
//        System.out.println("柱子长" + heigth + "柱子宽" + width);
        gap = 202;//柱子上下间距 192  总背景长度 - 柱子 * 2
        distance = width + 120;//柱子左右间距 245
        x = 550 + (n - 1) * distance;
        y = random.nextInt((374-heigth/2) - (heigth/2) + 1) + (heigth/2); //; 75离顶最接近 295   y 轴 = 背景距离 - 柱子/2
    }
    /**柱子类的移动方法*/
    public void step(){
        --x;
        if (x == -width/2) {
            x = distance * 2 - width/2;
            y = random.nextInt((374-heigth/2) - (heigth/2) + 1) + (heigth/2); //75离顶最接近 rand.nextInt(MAX - MIN + 1) + MIN;
        }
    }
}




/**对象：鸟bird*/
class Bird{
    //定义属性
    BufferedImage image;
    int x,y;
    int width,heigth;
    int size;//鸟的大小用于碰撞检测

    //在bird类中添加属性，计算鸟的位置
    double g;//重力加速度
    double t;//两次位置的间隔时间
    double v0;//初始上抛速度
    double speed;//当前上抛速度
    double s;//经过时间t后的位移
    double alpha;//鸟的倾斜度

    /**为该类添加构造方法，初始化属性变量*/
    public Bird() throws Exception{
        image = ImageIO.read(getClass().getResource("../img/bird.png"));
        width = image.getWidth();
        heigth = image.getHeight();
//        System.out.println("鸟长" + heigth + "鸟宽" + width);
        x = 144 - 60; //144
        y = 256 - 30; //修改值256
        size = 24; //之后判定鸟长度
//为计算鸟的位置的属性初始化

        g = 4; //重力加速度
        v0 = 15;  //初始上抛速度
        t = 0.25;  //两次位置的间隔时间
        speed = v0;   //当前上抛速度
        s = 0; //经过时间t后的位移
        alpha = 0;  //鸟的倾斜度
    }
    /**小鸟类的移动方法*/
    public void step(){
        double v0 = speed;
        s=v0 * t + g*t*t/2;//计算上抛运动位移
        y = y-(int)s;//计算鸟的坐标位置
        double v = v0-g*t;//计算下次的速度
        speed = v;
// alpha=Math.atan(s/8);
    }

    public void flappy(){//重新设置初始速度，重新向上飞。
        speed = v0;
    }

    //检测鸟碰撞地面的方法
    public boolean hit(Ground ground){
        boolean top = (y - heigth/2) < 0; //碰到顶检测
        boolean bottom = (y + heigth/2) > ground.y; //size/2 碰到底检测
        boolean hit  = top || bottom; //被碰撞到顶 或 底 都为true
//        System.out.print(" " + y);
        if (hit) {
            y = y;//鸟放到当前位置 也就是 碰撞到的位置
// alpha=-3.14159/2;
        }
        return hit;
    }

//    public boolean hit(Column column){
//        if (x > column.x - column.width/2 - size/2 && x < column.x + column.width/2 + size/2) {//检测是否在柱子之间
//            if (y > column.y - column.gap/2 + size/2 && y < column.y + column.gap/2 - size/2) {//检测是否在缝隙之中
//                return false;
//            }
//            return false; //true
//        }
//        return false;
//    }
    public boolean hitTest(Column column) {
        System.out.print(" 鸟x " + (x + width/2) + " column.x " + column.x);
        return false;
    }

//    检测鸟碰撞柱子的方法
    public boolean hit(Column column){
        if((x + width/2) >= (column.x - column.width/2) && (x - width/2) <= (column.x + column.width/2)) {
            if((y + heigth/2)  < (column.y - column.heigth/2) || (y - heigth/2) > (column.y + column.heigth/2)){
                return false;
            }
            y = y;

            return true;
        }
        return false;
    }

}
